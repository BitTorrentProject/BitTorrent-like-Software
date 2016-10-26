/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Thread;

import BusinessLogic.Machine;
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
public class ThreadMachine implements Runnable{
    private Machine m;
    
    public void run(){
        try {
            byte[] receiveData = new byte[100 * 1024 * 1024];
            byte[] sendData = new byte[100 * 1024 * 1024];
            DatagramSocket serverSocket = new DatagramSocket(9876);
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        } catch (SocketException ex) {
            Logger.getLogger(ThreadMachine.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
