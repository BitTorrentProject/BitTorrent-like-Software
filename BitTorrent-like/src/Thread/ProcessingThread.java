/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Thread;

import BusinessLogic.Chunk;
import Converter.DataPartition;
import Converter.TypeConverter;
import GraphicInterface.MainInterface;
import Port.PortFinder;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Random;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

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
            System.out.println(port);
            this.SetRequest(3);
            
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
            
            // (10) sending object ID you want to download
            DatagramPacket ObectIDPacket = new DatagramPacket(TypeConverter.serialize(this.ChunkID), TypeConverter.serialize(this.ChunkID).length, IPDest, DestPort);
            socket.send(ObectIDPacket);
            
            // (11) receiving Data Packet size (size of byte array in 1 chunk)
            byte[] receivedData = new byte [1024];
            socket.receive(new DatagramPacket(receivedData, receivedData.length));
            int PacketSize = (int)TypeConverter.deserialize(receivedData);
            
            // if the file is found at server machine (PacketSize != -1)
            if (PacketSize >= 0) {
                System.out.println(this.IPDest + " length = " + PacketSize);
                // calculating no of frames will be received
                int nFrames = PacketSize / (65507);
                if (PacketSize % (65507) != 0)
                    nFrames += 1;
                
                Vector<byte[]> FrameVector = new Vector<byte[]>();
                
                if (PacketSize % (65507) != 0) {
                    // (12) receiving chunk (one chunk have more than 1 frame)
                    for (int i = 0; i < nFrames - 1; i++) {
                        receivedData = new byte[65507];
                        socket.receive(new DatagramPacket(receivedData, receivedData.length));
                        FrameVector.addElement(receivedData);
                    }
                    
                    System.out.println(this.IPDest + " = " + receivedData.length);
                    receivedData = new byte[PacketSize % (65507)];
                    socket.receive(new DatagramPacket(receivedData, receivedData.length));
                    FrameVector.addElement(receivedData);
                    System.out.println(this.IPDest + " = " + receivedData.length);
                }
                else {
                    // (12) receiving chunk (one chunk have more than 1 frame)
                    for (int i = 0; i < nFrames; i++) {
                        receivedData = new byte[65507];
                        socket.receive(new DatagramPacket(receivedData, receivedData.length));
                        FrameVector.addElement(receivedData);
                        //System.out.println(this.IPDest + " = " + receivedData.length);
                    }
                }
                double ChunkSize = 0;
                
                if (PacketSize % (1024 * 1024) == 0) 
                    ChunkSize = 1;
                else
                    ChunkSize = ((double)(PacketSize)/ (1024 * 1024)) - (int)(PacketSize / (1024 * 1024));
                
                byte[] ChunkArray = DataPartition.Assemble(FrameVector,65507);
                System.out.println(" ******* " + ChunkArray.length);
                String FileName = (String)this.Interface.GetTableDownloadProcess().getModel().getValueAt(0, 0);
                
                //set chunk
                this.Interface.Chunks.set(ChunkID, new Chunk(this.ChunkID, ChunkSize, ChunkArray, FileName));
            }
            else {
                System.out.println("Error");
            }
        } catch (IOException ex) {
            Logger.getLogger(ProcessingThread.class.getName()).log(Level.SEVERE, null, ex);
            
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ProcessingThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void setIPDest() {
        int j = ProcessingThread.randInt(0, this.Interface.AddrContainingFile.size() - 1);
        this.IPDest = this.Interface.AddrContainingFile.elementAt(j);
    }
    
    public void setChunkID(int ChunkID) {
        this.ChunkID = ChunkID;
    }
}