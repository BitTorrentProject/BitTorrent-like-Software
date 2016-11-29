/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Thread;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author admin
 */
public class ChunkReceiver implements Runnable{
    private DatagramSocket socket;
    private byte[] ReceivedData;
    private DatagramPacket packet;
    private final int port = 9090;
    private int Request;
    private ObjectInputStream is;
    private ObjectOutputStream os;
    public ChunkReceiver() throws IOException{
        try {
            ReceivedData = new byte[1024];
            socket = new DatagramSocket(port);
            //packet = new DatagramPacket(ReceivedData, ReceivedData.length);
            
            byte[] recvBuf = new byte[5000];
            ByteArrayInputStream byteStream = new ByteArrayInputStream(recvBuf);
            is = new ObjectInputStream(new BufferedInputStream(byteStream));
            
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            os = new ObjectOutputStream(outputStream);
        } catch (SocketException ex) {
            Logger.getLogger(ChunkReceiver.class.getName()).log(Level.SEVERE, null, ex);
            socket.close();
        }
        
    }
    
    @Override
    public void run() {
        
        try {
            this.SendRequest();
            
            int receivedMessage = is.readInt();
            
            // the replied message is 0 : 
            if (receivedMessage == 0) {
                
            }
        } catch (IOException ex) {
            Logger.getLogger(ChunkReceiver.class.getName()).log(Level.SEVERE, null, ex);
            socket.close();
        }
        
    }
    
    public void SetRequest(int message) {
        Request = message;
    }
    
    private synchronized void SendRequest(){
        try {
            // send message to other machines
            os.writeInt(Request);
        } catch (IOException ex) {
            Logger.getLogger(ChunkReceiver.class.getName()).log(Level.SEVERE, null, ex);
            socket.close();
        }
    }
    
    private void ReceiveChunks(){
        
    }
}
