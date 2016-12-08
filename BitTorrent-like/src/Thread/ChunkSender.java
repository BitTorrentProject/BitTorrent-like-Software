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
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author admin
 */
public class ChunkSender implements Runnable{
    private DatagramSocket socket;
    private int DestPort;
    private MainInterface Interface;
    private Thread thread;
    
    public ChunkSender(MainInterface Interface, DatagramSocket sock) {
        thread = new Thread(this);
        this.Interface = Interface;
        socket = sock;
    }
    
    @Override
    public void run() {
        while (true) {
            try {  
                // (0) get message from a machine
                DatagramPacket RequestPacket = new DatagramPacket(new byte[1024], 1024);
                socket.receive(RequestPacket);
                
                int message = (int) TypeConverter.deserialize(RequestPacket.getData());
                
                // a machine wants to search a file in your machine (message = 1)
                if (message == 1) {
                    // (1) receive file's name to search
                    DatagramPacket FileNamePacket = new DatagramPacket(new byte[1024], 1024);
                    socket.receive(FileNamePacket);
                    
                    // searching the file in local
                    Vector<String> FoundFiles = this.SearchFile((String)TypeConverter.deserialize(FileNamePacket.getData()));
                    
                    // (2) receive IP src
                    DatagramPacket IpSrcPacket = new DatagramPacket(new byte[1024], 1024);
                    socket.receive(IpSrcPacket);
                    
                    // (3) receiving src port
                    DatagramPacket PortSrcPacket = new DatagramPacket(new byte[1024], 1024);
                    socket.receive(PortSrcPacket);
                    DestPort = (int)TypeConverter.deserialize(PortSrcPacket.getData());
               
                    // (4) reply this message to confirm that I received the message
                    byte[] reply = TypeConverter.serialize(message);
                    socket.send(new DatagramPacket(reply, reply.length, (InetAddress) TypeConverter.deserialize(IpSrcPacket.getData()),DestPort));
                    
                    Vector<byte[]> FoundFilesPacket = DataPartition.SeparateObjectByteArray(TypeConverter.serialize(FoundFiles));
                    
                    // (5) sending packet length of found files
                    socket.send(new DatagramPacket(TypeConverter.serialize(FoundFilesPacket.size()), TypeConverter.serialize(FoundFilesPacket.size()).length, (InetAddress)TypeConverter.deserialize(IpSrcPacket.getData()),DestPort));
                    
                    // (6) send found files
                    for (byte[] Temp : FoundFilesPacket) {
                        socket.send(new DatagramPacket(Temp, Temp.length, (InetAddress)TypeConverter.deserialize(IpSrcPacket.getData()),DestPort));
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(ChunkSender.class.getName()).log(Level.SEVERE, null, ex);
                socket.close();
                this.thread.interrupt();
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ChunkSender.class.getName()).log(Level.SEVERE, null, ex);
                socket.close();
                this.thread.interrupt();
            }
        }
    }
    
    private Vector<String> SearchFile(String FileName) {
        File folder = new File("BitTorrent");
        Vector<String> FoundFiles = new Vector<String>();
        
        for (final UploadingFile fileEntry : this.Interface.GetMachine().GetFiles()) {
            if (fileEntry.getName().matches("(.*)" + FileName + "(.*)")
                 && !fileEntry.getName().endsWith(".torrent")) {
                FoundFiles.addElement(fileEntry.getName());
            }
        }
        
        return FoundFiles;
    }
    
    public void start(){
        this.thread.start();
    }
}