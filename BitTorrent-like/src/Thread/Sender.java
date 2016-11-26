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
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author admin
 */
public class Sender implements Runnable{
    private DatagramSocket socket;
    private byte[] SentData;
    private DatagramPacket packet;
    private final int port = 9090;
    public Sender() {
        try {
            SentData = new byte[1024];
            socket = new DatagramSocket();
            InetAddress IPAddress = InetAddress.getByName("localhost");
            packet = new DatagramPacket(SentData, SentData.length, IPAddress, port);
        } catch (SocketException ex) {
            Logger.getLogger(Sender.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
        } catch (UnknownHostException ex) {
            Logger.getLogger(Sender.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
        }
    }
    
    @Override
    public void run() {
        
    }
}
