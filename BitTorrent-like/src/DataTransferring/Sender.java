/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataTransferring;

import Converter.DataPartition;
import Converter.TypeConverter;
import Port.PortFinder;
import Thread.DataPacket;
import java.io.IOException;
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
public class Sender extends Thread{
    DatagramSocket socket;
    int port;
    InetAddress Dest;
    public Sender(int port, String IP) throws SocketException, UnknownHostException {
        socket = new DatagramSocket(port);
        this.port = port;
        this.Dest = InetAddress.getByName(IP);
    }
    
    public void run() {
        try {
            Vector<byte[]> ChunkPacket = new Vector<byte[]>();
            
            DatagramPacket RequestPacket = new DatagramPacket(TypeConverter.serialize(port), TypeConverter.serialize(port).length, Dest, 9090);
            socket.send(RequestPacket);
            System.out.println("dsdsadsadas");
            
            // (0) receiving nFrame
            DatagramPacket SizePacket = new DatagramPacket(new byte[1024], 1024);
            socket.receive(SizePacket);
            
            int size = (int)TypeConverter.deserialize(SizePacket.getData());
            if (size < 0)
                return;
            System.out.println("size  = " + size);
            int nFrames = size/65507;
            
            // (1) receiving frame
            for (int i = 0; i <= nFrames - 1; i++) {
                DatagramPacket p1 = new DatagramPacket(new byte[65507], 65507);
                //Thread.sleep(2000);
                socket.setSoTimeout(5000);
                socket.receive(p1);
                ChunkPacket.addElement(p1.getData());
                System.out.println("Port " + port + " , Frame " + (i) + " : received, HashCode: " + TypeConverter.toHexFormat(p1.getData()));
            }
            
            DatagramPacket p1 = new DatagramPacket(new byte[size % 65507], size % 65507);
            //Thread.sleep(2000);
            socket.setSoTimeout(5000);
            socket.receive(p1);
            ChunkPacket.addElement(p1.getData());
            System.out.println("Port " + port + "Frame " + (nFrames) + " : received, HashCode: " + TypeConverter.toHexFormat(p1.getData()));
            
            // display data.
            /*DataPacket packet = (DataPacket)TypeConverter.deserialize(DataPartition.Assemble(ChunkPacket, 65507));
            byte[] data = (byte[])packet.getData();
            for (int i = 0; i < data.length; i++) {
                System.out.println(data[i]);
            }*/
            
            System.out.println("-------------------------END PROGRAM-------------------");
        } catch (SocketException ex) {
            Logger.getLogger(Sender.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Sender.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Sender.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally {
            socket.close();
        }
    }
    public static void main(String args[]) throws SocketException, UnknownHostException, InterruptedException {
        
        
        for (int i = 0; i < 2; i++) {
            Sender s1 = new Sender(PortFinder.findFreePort(), "192.168.1.1");
            Sender s2 = new Sender(PortFinder.findFreePort(), "192.168.1.3");
            
            s1.start();
            s2.start();
            
            s1.join();
            s2.join();
        }
    }
}
