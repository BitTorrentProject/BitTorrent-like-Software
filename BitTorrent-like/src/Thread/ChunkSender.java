/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Thread;

import BusinessLogic.UploadingFile;
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
    private byte[] Data;
    private final int port = 9090;
    private MainInterface Interface;
    protected Thread thread;
    
    public ChunkSender(MainInterface Interface, DatagramSocket sock) {
        thread = new Thread(this);
        this.Interface = Interface;
        Data = new byte[1024];
        socket = sock;
    }
    
    @Override
    public void run() {
        while (true) {
            try {
                // (0) get message from a machine
                DatagramPacket MessagePacket = new DatagramPacket(Data, Data.length);
                socket.receive(MessagePacket);
                
                int message = (int) TypeConverter.deserialize(MessagePacket.getData());
                
                // a machine wants to search a file in your machine (message = 1)
                if (message == 1) {
                    DatagramPacket FileNamePacket = new DatagramPacket(Data, Data.length);
                    // (1) receive file's name to search
                    socket.receive(FileNamePacket);
                    
                    // searching the file in local
                    Vector<UploadingFile> FoundFiles = this.SearchFile((String)TypeConverter.deserialize(FileNamePacket.getData()));
                    
                    DatagramPacket IpSrcPacket = new DatagramPacket(Data, Data.length);
                    // (-1) receive file's name to search
                    socket.receive(IpSrcPacket);
                    
                    // reply this message to confirm that I received the message
                    // (2)
                    byte[] reply = TypeConverter.serialize(message);
                    DatagramPacket replyPacket = new DatagramPacket(reply, reply.length, (InetAddress) TypeConverter.deserialize(IpSrcPacket.getData()),port);
                    socket.send(replyPacket);
                    
                    // (3) send found files
                    DatagramPacket FoundFilesPacket = new DatagramPacket(TypeConverter.serialize(FoundFiles), TypeConverter.serialize(FoundFiles).length, (InetAddress) TypeConverter.deserialize(TypeConverter.serialize(IpSrcPacket)),port);
                    socket.send(replyPacket);
                }
            } catch (IOException ex) {
                Logger.getLogger(ChunkSender.class.getName()).log(Level.SEVERE, null, ex);
                socket.close();
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ChunkSender.class.getName()).log(Level.SEVERE, null, ex);
                socket.close();
            }
        }
    }
    
    private Vector<UploadingFile> SearchFile(String FileName) {
        File folder = new File("BitTorrent");
        Vector<UploadingFile> FoundFiles = new Vector<>();
        
        for (final UploadingFile fileEntry : this.Interface.GetMachine().GetFiles()) {
            if (fileEntry.getName().matches("(.*)" + FileName + "(.*)")) {
                FoundFiles.addElement(fileEntry);
            }
        }
        
        return FoundFiles;
    }
    
    public void start(){
        this.thread.start();
    }
}
