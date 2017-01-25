/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Thread;

import Converter.TypeConverter;
import GraphicInterface.MainInterface;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author admin
 */
public class ChunkReceiver implements Runnable{

    public DatagramSocket getSocket() {
        return socket;
    }

    public Thread getThread() {
        return thread;
    }
    protected DatagramSocket socket;
    protected final int DestPort = 6060;
    protected int SrcPort;
    protected int Request;
    protected MainInterface Interface;
    protected InetAddress IPDest;
    protected Thread thread;
    
    public ChunkReceiver(String IPNeighbor, MainInterface Interface, DatagramSocket sock, int LocalPort){
        try {
            thread = new Thread(this);
            this.Interface = Interface;
            socket = sock;
            SrcPort = LocalPort;
            
            // destination IP
            IPDest = InetAddress.getByName(IPNeighbor);
            System.out.println(IPDest.toString());
        } catch (UnknownHostException ex) {
            Logger.getLogger(ChunkReceiver.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void run() {
        // sending request
        this.SendRequest();

        try {
            byte[] ReceivedData = new byte[1024];
            
            // (4) other machine reply and we catch the message
            DatagramPacket DataPacket = new DatagramPacket(ReceivedData, ReceivedData.length);
            socket.setSoTimeout(2000);
            socket.receive(DataPacket);
            
            // convert data in received packet to int
            int receivedMessage = (int) TypeConverter.deserialize(DataPacket.getData());
            
            synchronized(this.Interface) {    
                // the replied message is 1 : searching files
                if (receivedMessage == 1) {
                    // (5) receiving packet size of vector of byte arrays
                    socket.setSoTimeout(2000);
                    socket.receive(DataPacket);
                    long FileLength = (long)TypeConverter.deserialize(DataPacket.getData());
                    
                    // there is one file found
                    if (FileLength >= 0) {
                        // (6) receiving result from server : the file name to search, and the size
                        // so we have found the file in system
                        socket.setSoTimeout(2000);
                        socket.receive(DataPacket);
                        String fileName = (String)TypeConverter.deserialize(DataPacket.getData());
                        
                        // inserting item (Found files 'name) to table interface
                        System.out.println("---------------------------------");
                        DefaultTableModel model = (DefaultTableModel) this.Interface.GetTableDownloadProcess().getModel();
                        model.addRow(new Object[]{fileName, FileLength});
                        //this.Interface.GetMachine().getFoundFiles().addElement(File);
                        this.Interface.GetTableDownloadProcess().setModel(model);
                        Integer result = Integer.parseInt(this.Interface.getLbNumberResult().getText()) + 1;
                        this.Interface.getLbNumberResult().setText(result.toString());
                    }
                }
                // the replied message is 2 : sending torrent content of the file you want to download
                else if (receivedMessage == 2) {
                    // (7) receiving message informing if that machine has the file you want to download
                    socket.setSoTimeout(2000);
                    socket.receive(DataPacket);
                    long message = (long)TypeConverter.deserialize(DataPacket.getData());
                    
                    // message , 0: file not found
                    // message >= 0 : length of file you want to search
                    // if the file you want to download is detected in that machine
                    if (message >= 0) {
                        // (8) receiving the packet containing IP of that machine
                        socket.setSoTimeout(2000);
                        socket.receive(DataPacket);
                        InetAddress IPdest = (InetAddress)TypeConverter.deserialize(DataPacket.getData());
                        this.Interface.AddrContainingFile.addElement(IPdest);
                        
                        // (9) receiving the name of file you want to download again.
                        socket.setSoTimeout(2000);
                        socket.receive(DataPacket);
                        String fileName = (String)TypeConverter.deserialize(DataPacket.getData());
                        
                        // inserting file info to table
                        System.out.println("---------------------------------");
                        DefaultTableModel model = (DefaultTableModel) this.Interface.GetTableDownloadProcess().getModel();
                        model.addRow(new Object[]{fileName, message});
                        
                        this.Interface.GetTableDownloadProcess().setModel(model);
                        Integer result = Integer.parseInt(this.Interface.getLbNumberResult().getText()) + 1;
                        this.Interface.getLbNumberResult().setText(result.toString());
                        // đưa ra 2 biến value, maxsize= size file, 1 size đã đc down
                    }
                    else {
                        System.out.println("Error");
                    }
                }
                else{
                    
                }
            }
        } catch (IOException  | ClassNotFoundException ex) {
            System.out.println("There is an error connection : the message can not be tranfered !");
        }
        //Logger.getLogger(ChunkReceiver.class.getName()).log(Level.SEVERE, null, ex);
         finally {
            System.out.println("Ending thread");
            this.socket.close();
            this.thread.interrupt();
        }
    }

    public void SetRequest(int message) {
        Request = message;
    }
    
    protected void SendRequest(){
         try {
            // (0) send message to other machines
            DatagramPacket RequestPacket = new DatagramPacket(TypeConverter.serialize(Request), TypeConverter.serialize(Request).length, IPDest, DestPort);
            socket.send(RequestPacket);
            
            // searching
            if (Request == 1) {
                // (1) sending file name to search
                DatagramPacket FileNamePacket = new DatagramPacket(TypeConverter.serialize(this.Interface.GettfSearch().getText()), TypeConverter.serialize(this.Interface.GettfSearch().getText()).length, IPDest, DestPort);
                socket.send(FileNamePacket);
            }
            // adding torrent
            else if (Request == 2) {
                // (1') sending file name to search
                DatagramPacket FileNamePacket = new DatagramPacket(TypeConverter.serialize(this.Interface.StaticFileTorrent.getName()), TypeConverter.serialize(this.Interface.StaticFileTorrent.getName()).length, IPDest, DestPort);
                socket.send(FileNamePacket);
                
                // khai báo bảng byte = kích thước của con trỏ file
                byte[] bytes = new byte[(int)this.Interface.StaticFileTorrent.length()];
                String path = this.Interface.StaticFileTorrent.getPath();
                FileInputStream fis=new FileInputStream(path);
                fis.read(bytes);
                fis.close();
                
                // (1) sending local torrent file content (byte array)
                DatagramPacket TorrentBytePacket = new DatagramPacket(bytes, bytes.length, IPDest, DestPort);
                socket.send(TorrentBytePacket);
            }
            // downloading
            else if (Request == 3) {
                String name = (String)this.Interface.GetTableDownloadProcess().getModel().getValueAt(0, 0);
                
                // (1) sending file name to download
                DatagramPacket FileNamePacket = new DatagramPacket(TypeConverter.serialize(name), TypeConverter.serialize(name).length, IPDest, DestPort);
                socket.send(FileNamePacket);
            }
            
            // (2) sending local IP
            DatagramPacket IPSrcPacket = new DatagramPacket(TypeConverter.serialize(this.Interface.GetMachine().getIPAddr()), TypeConverter.serialize(this.Interface.GetMachine().getIPAddr()).length, IPDest, DestPort);
            socket.send(IPSrcPacket);

            // (3) sending port
            DatagramPacket PortPacket = new DatagramPacket(TypeConverter.serialize(SrcPort), TypeConverter.serialize(SrcPort).length, IPDest, DestPort);
            socket.send(PortPacket);
        } catch (IOException ex) {
            //Logger.getLogger(ChunkReceiver.class.getName()).log(Level.SEVERE, null, ex);
            socket.close();
            this.thread.interrupt();
        }
    }
    
    // starting the thread
    public void start() {
        thread.start();
    }
}