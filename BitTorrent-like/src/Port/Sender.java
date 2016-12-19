/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Port;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author admin
 */
public class Sender {
    public static void main(String r[]) throws InterruptedException{
        DatagramSocket socket = null;
        try {
            byte [] a = new byte[65507];
            socket = new DatagramSocket(6060);
            DatagramPacket p = new DatagramPacket(a, a.length);
            /*for (int i = 0; i < 50; i++) {
                System.out.println("dsdassdasd");
            }*/
            System.out.println("aaaaaaaaaaaaaaaaaaaaaaaa");
            for (int i = 0; i < 1024; i++) {
                Thread.sleep(1);
                socket.receive(p);
                System.out.print("\n" + i + " = " + p.getLength() + " ----------------------\n");
                for (int j = 0; j < 50; j++) {
                    System.out.print((byte)p.getData()[i]);
                }
            }
            
            System.out.println("\n**********Finish****************");
        } catch (SocketException ex) {
            Logger.getLogger(Sender.class.getName()).log(Level.SEVERE, null, ex);
            
        } catch (IOException ex) {
            Logger.getLogger(Sender.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            socket.close();
        }
        
    }
}
