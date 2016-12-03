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
    protected byte[] ReceivedData;
    protected final int DestPort = 6060;
    protected int Request;
    protected MainInterface Interface;
    protected InetAddress IPDest;
    protected Thread thread;
    
    public ChunkReceiver(String IPNeighbor, MainInterface Interface, DatagramSocket sock) throws IOException{
        thread = new Thread(this);
        this.Interface = Interface;
        ReceivedData = new byte[1024];
        socket = sock;
        
        // destination IP
        IPDest = InetAddress.getByName(IPNeighbor);
        System.out.println(IPDest.toString());
    }
    
    @Override
    public void run() {
        // sending request
        this.SendRequest();

        try {
            DatagramPacket packet = new DatagramPacket(ReceivedData, ReceivedData.length);

            // (2) other machine reply and we catch the message
            socket.receive(packet);
            
            synchronized (packet) {
                // convert data in received packet to int
                int receivedMessage = (int) TypeConverter.deserialize(packet.getData());
                
                // the replied message is 0 : searching files
                if (receivedMessage == 1) {
                    // (3) receiving vector of found files
                    Vector<byte[]> VectorObjectByteArray = new Vector<>();
                    while (true) {
                        // (3)
                        socket.receive(packet);
                        if (packet == null)
                            break;
                        VectorObjectByteArray.addElement(packet.getData());
                    }
                    
                    // convert the VectorObjectByteArray to Vector<UploadingFiles 
                    Vector<UploadingFile> FoundFiles = (Vector<UploadingFile>) TypeConverter.deserialize(DataPartition.Assemble(VectorObjectByteArray));
                    
                    // inserting item (Found files 'name) to table interface
                    DefaultTableModel model = (DefaultTableModel) this.Interface.GetTableDownloadProcess().getModel();
                    model.getDataVector().removeAllElements();
                    for (UploadingFile File : FoundFiles) {
                        model.addRow(new Object[]{File.getName(), File.getSize(), ""});
                        this.Interface.GetMachine().getFoundFiles().addElement(File);
                    }
                    this.Interface.GetTableDownloadProcess().setModel(model);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ChunkReceiver.class.getName()).log(Level.SEVERE, null, ex);
            this.socket.close();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ChunkReceiver.class.getName()).log(Level.SEVERE, null, ex);
            this.socket.close();
        }
    }

    public void SetRequest(int message) {
        Request = message;
    }
    
    private void SendRequest(){
        try {
            byte[] request = TypeConverter.serialize(Request);
            DatagramPacket packet = new DatagramPacket(request, request.length, IPDest, DestPort);
            System.out.println("request " + packet.getData().length + " = " + request.length);
            // (0) send message to other machines
            socket.send(packet);
            
            if (Request == 1) {
                byte[] FileNameByte = TypeConverter.serialize(this.Interface.GettfSearch().getText());
                packet = new DatagramPacket(FileNameByte, FileNameByte.length, IPDest, DestPort);
                System.out.println("Filename : " + FileNameByte.length);
                
                // (1) sending file name to search
                socket.send(packet);
                
                byte[] LocalIPByteArray = TypeConverter.serialize(this.Interface.GetMachine().getIPAddr());
                packet = new DatagramPacket(LocalIPByteArray, LocalIPByteArray.length, IPDest, DestPort);
                // (-1) sending local IP
                socket.send(packet);
                System.out.println("LocalIPByteArray " + LocalIPByteArray.length);
            }
            
            //System.out.println(Request);
        } catch (IOException ex) {
            Logger.getLogger(ChunkReceiver.class.getName()).log(Level.SEVERE, null, ex);
            socket.close();
        }
    }
    
    private void ReceiveChunks(){
        
    }
    
    // starting the thread
    public void start() {
        thread.start();
    }
}
