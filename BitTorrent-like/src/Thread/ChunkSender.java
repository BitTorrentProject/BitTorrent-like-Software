/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Thread;

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
    private byte[] SentData;
    private DatagramPacket packet;
    private final int port = 9090;
    public ChunkSender(String IP) {
        try {
            SentData = new byte[1024];
            socket = new DatagramSocket();
            InetAddress IPAddress = InetAddress.getByName(IP);
            //packet = new DatagramPacket(SentData, SentData.length, IPAddress, port);
            
        } catch (SocketException ex) {
            Logger.getLogger(ChunkSender.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
            socket.close();
        } catch (UnknownHostException ex) {
            Logger.getLogger(ChunkSender.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
            socket.close();
        }
    }
    
    @Override
    public void run() {
        while (true) {
            ObjectInputStream is = null;
            try {
                byte[] recvBuf = new byte[5000];
                ByteArrayInputStream byteStream = new ByteArrayInputStream(recvBuf);
                is = new ObjectInputStream(new BufferedInputStream(byteStream));
                
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                ObjectOutputStream os = new ObjectOutputStream(outputStream);
        
                // get message from a machine
                int message = is.readInt();
                
                // a machine wants to search a file in your machine (message = 1)
                if (message == 1) {
                    String FileName = is.readUTF();
                    Vector<String> FoundFiles = this.SearchFile(FileName);
                    
                    // reply this message to confirm that I received the message
                    os.writeInt(message);
                    
                    // send found files
                    os.writeObject(FoundFiles);
                }
            } catch (IOException ex) {
                Logger.getLogger(ChunkSender.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    is.close();
                } catch (IOException ex) {
                    Logger.getLogger(ChunkSender.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    private Vector<String> SearchFile(String FileName) {
        File folder = new File("BitTorrent");
        Vector<String> FoundFiles = new Vector<>();
        
        for (final File fileEntry : folder.listFiles()) {
            if (!fileEntry.isDirectory()) {
                if (fileEntry.getName().matches("(.*)" + FileName + "(.*)")) {
                    FoundFiles.addElement(fileEntry.getName());
                }
            }
        }
        
        return FoundFiles;
    }
}
