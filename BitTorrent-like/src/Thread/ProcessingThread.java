/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Thread;

import BusinessLogic.Chunk;
import BusinessLogic.UploadingFile;
import Converter.TypeConverter;
import GraphicInterface.MainInterface;
import Port.PortFinder;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author admin
 */
public class ProcessingThread extends ChunkReceiver{
    private DataPacket ReceivedPacket;
    private int ChunkID;
    
    public ProcessingThread(String IPNeighbor, MainInterface Interface, DatagramSocket sock, int LocalPort)
            throws IOException {
        super(IPNeighbor, Interface, sock, LocalPort);
    }
    
    public void run(){
        try {
            // choosing IP destination to connect
            this.setIPDest();
            
            this.Interface.receivers.addElement(this);
            
            // choosing destination port to create socket
            int port = PortFinder.findFreePort();
            this.socket = new DatagramSocket(port);
            this.SrcPort = port;
            
            this.SetRequest(2);
            
            System.out.println(this.IPDest);
            // starting send-receive data
            this.ProcessMessage();
        } catch (SocketException ex) {
            Logger.getLogger(ProcessingThread.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            this.socket.close();
            this.thread.interrupt();
        }
    }
     
    public static int randInt(int min, int max) {

        // NOTE: This will (intentionally) not run as written so that folks
        // copy-pasting have to think about how to initialize their
        // Random instance.  Initialization of the Random instance is outside
        // the main scope of the question, but some decent options are to have
        // a field that is initialized once and then re-used as needed or to
        // use ThreadLocalRandom (if using at least Java 1.7).
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }
    
    private synchronized  void ProcessMessage(){
        try {
            // sending request
            this.SendRequest();
            
            // (8) sending chunk ID packet
            DatagramPacket ChunkIDPacket  = new DatagramPacket(TypeConverter.serialize(this.ChunkID), TypeConverter.serialize(this.ChunkID).length, this.IPDest, this.DestPort);
            socket.send(ChunkIDPacket);
            
            // get selected row
            int selectedRow = this.Interface.GetTableDownloadProcess().getSelectedRow();
            
            // reading data in file torrent
            byte[] bytes= this.Interface.getTorrents().elementAt(selectedRow);
            
            // (9) sending file torrent content
            DatagramPacket TorrentArray = new DatagramPacket(bytes, bytes.length, this.IPDest, this.DestPort);
            socket.send(TorrentArray);
            
            // (10) receiving Chunk Packet size (size of byte array in 1 chunk)
            byte[] receivedData = new byte [1024];
            socket.receive(new DatagramPacket(receivedData, receivedData.length, this.IPDest, this.DestPort));
            int PacketSize = (int)TypeConverter.deserialize(receivedData);
            
            // (11) receiving Packet Chunk
            // if the file is found at server machine (PacketSize != -1)
            if (PacketSize >= 0) {
                receivedData = new byte[PacketSize];
                socket.receive(new DatagramPacket(receivedData, receivedData.length, this.IPDest, this.DestPort));
                this.ReceivedPacket = (DataPacket) TypeConverter.deserialize(receivedData);
                
                // create the torrent file for the file you want to download
                FileOutputStream fos = new FileOutputStream("Bittorrent//" + (String)this.Interface.GetTableDownloadProcess().getModel().getValueAt(selectedRow, 0) + ".torrent");
                fos.write(bytes);
                fos.close();
                
                //set chunk
                this.Interface.Chunks.set(ChunkID, (Chunk)this.ReceivedPacket.getData());
            }
            else {
                ProcessingThread t = new ProcessingThread(null, this.Interface, null, 0);
                t.ChunkID = this.ChunkID;
                this.Interface.receivers.addElement(t);
                System.out.println("new ");
                t.start();
            }
        } catch (IOException ex) {
            Logger.getLogger(ProcessingThread.class.getName()).log(Level.SEVERE, null, ex);
            
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ProcessingThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void setIPDest() {
        try {
            int j = ProcessingThread.randInt(0, this.Interface.getPeers().length - 1);
            String[] IPDestList = this.Interface.getPeers();
            this.IPDest = InetAddress.getByName(IPDestList[j]);
        } catch (UnknownHostException ex) {
            Logger.getLogger(ProcessingThread.class.getName()).log(Level.SEVERE, null, ex);
            this.thread.interrupt();
        }
    }
    
    public void setChunkID(int ChunkID) {
        this.ChunkID = ChunkID;
    }
    
    public Thread getThread() {
        return thread;
    }
}
