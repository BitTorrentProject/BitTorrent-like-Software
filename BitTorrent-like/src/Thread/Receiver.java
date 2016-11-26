/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Thread;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author admin
 */
public class Receiver implements Runnable{
    private DatagramSocket socket;
    private byte[] ReceivedData;
    private DatagramPacket packet;
    private final int port = 9090;
    
    public Receiver(){
        try {
            ReceivedData = new byte[1024];
            socket = new DatagramSocket(port);
            packet = new DatagramPacket(ReceivedData, ReceivedData.length);
            
        } catch (SocketException ex) {
            Logger.getLogger(Receiver.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    @Override
    public void run() {
        
        
    }   
}