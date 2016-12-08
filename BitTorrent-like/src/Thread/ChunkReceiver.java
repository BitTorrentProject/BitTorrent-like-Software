/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Thread;

import BusinessLogic.UploadingFile;
import Converter.DataPartition;
import Converter.TypeConverter;
import GraphicInterface.MainInterface;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author admin
 */
public class ChunkReceiver implements Runnable{
    protected DatagramSocket socket;
    protected final int DestPort = 6060;
    protected int SrcPort;
    protected int Request;
    protected MainInterface Interface;
    protected InetAddress IPDest;
    protected Thread thread;
    
    public ChunkReceiver(String IPNeighbor, MainInterface Interface, DatagramSocket sock, int LocalPort) throws IOException{
        thread = new Thread(this);
        this.Interface = Interface;
        socket = sock;
        SrcPort = LocalPort;
        
        // destination IP
        IPDest = InetAddress.getByName(IPNeighbor);
        System.out.println(IPDest.toString());
    }
    
    @Override
    public void run() {
        // sending request
        this.SendRequest();

        try {
            byte[] ReceivedData = new byte[1024];
            
            // (4) other machine reply and we catch the message
            DatagramPacket DataPacket = new DatagramPacket(ReceivedData, ReceivedData.length);
            socket.receive(DataPacket);
            
            synchronized(DataPacket) {
                // convert data in received packet to int
                int receivedMessage = (int) TypeConverter.deserialize(DataPacket.getData());
                
                // the replied message is 1 : searching files
                if (receivedMessage == 1) {
                    // (5) receiving packet size of vector of byte arrays
                    socket.receive(DataPacket);
                    int PacketLength = (int)TypeConverter.deserialize(DataPacket.getData());
                    
                    // (6) receiving vector of found files
                    Vector<byte[]> VectorObjectByteArray = new Vector<byte[]>();
                    for (int i = 0; i < PacketLength; i++) {
                        socket.receive(DataPacket);
                        VectorObjectByteArray.addElement(DataPacket.getData());
                    }
                    
                    // convert the VectorObjectByteArray to Vector<String> 
                    Vector<String> FoundFiles = 
                      (Vector<String>) TypeConverter.deserialize(DataPartition.Assemble(VectorObjectByteArray));
                    
                    // inserting item (Found files 'name) to table interface
                    System.out.println("---------------------------------");
                    DefaultTableModel model = (DefaultTableModel) this.Interface.GetTableDownloadProcess().getModel();
                    for (String File : FoundFiles) {
                        model.addRow(new Object[]{File, "", ""});
                        //this.Interface.GetMachine().getFoundFiles().addElement(File);
                    }
                    this.Interface.GetTableDownloadProcess().setModel(model);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ChunkReceiver.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ChunkReceiver.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            this.socket.close();
        }
    }

    public void SetRequest(int message) {
        Request = message;
    }
    
    private void SendRequest(){
         try {
            // (0) send message to other machines
            DatagramPacket RequestPacket = new DatagramPacket(TypeConverter.serialize(Request), TypeConverter.serialize(Request).length, IPDest, DestPort);
            socket.send(RequestPacket);
            
            if (Request == 1) {
                // (1) sending file name to search
                DatagramPacket FileNamePacket = new DatagramPacket(TypeConverter.serialize(this.Interface.GettfSearch().getText()), TypeConverter.serialize(this.Interface.GettfSearch().getText()).length, IPDest, DestPort);
                socket.send(FileNamePacket);
            }
            
            // (2) sending local IP
            DatagramPacket IPSrcPacket = new DatagramPacket(TypeConverter.serialize(this.Interface.GetMachine().getIPAddr()), TypeConverter.serialize(this.Interface.GetMachine().getIPAddr()).length, IPDest, DestPort);
            socket.send(IPSrcPacket);

            // (3) sending port
            DatagramPacket PortPacket = new DatagramPacket(TypeConverter.serialize(SrcPort), TypeConverter.serialize(SrcPort).length, IPDest, DestPort);
            socket.send(PortPacket);
        } catch (IOException ex) {
            Logger.getLogger(ChunkReceiver.class.getName()).log(Level.SEVERE, null, ex);
            socket.close();
            this.thread.interrupt();
        }
    }
    
    // starting the thread
    public void start() {
        thread.start();
    }
}
