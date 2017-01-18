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
import static java.lang.Math.round;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author admin
 */
public class ProcessingThread extends ChunkReceiver{
    private DataPacket ReceivedPacket;
    private int ChunkID;
    private static Semaphore[] ReceivingChunkMutex;
    public ProcessingThread(String IPNeighbor, MainInterface Interface, DatagramSocket sock, int LocalPort)
    {
        super(IPNeighbor, Interface, sock, LocalPort);
        ReceivingChunkMutex = new Semaphore[this.Interface.NoMachineAccessing.length];
        for (int i = 0; i < ProcessingThread.ReceivingChunkMutex.length; i++) {
            ReceivingChunkMutex[i] = new Semaphore(1, true);
        }
    }
    
    public void run(){
        // set request
        this.SetRequest(3);
                
        int MachineIndex = this.BindSocketAndDestAddr();
        
        // acquire permit of semaphore
        try {
            // create socket and binding port
            this.SrcPort = PortFinder.findFreePort();
            this.socket = new DatagramSocket(this.SrcPort);
            ProcessingThread.ReceivingChunkMutex[MachineIndex].acquire();
        } catch (InterruptedException | SocketException ex) {
            Logger.getLogger(ProcessingThread.class.getName()).log(Level.SEVERE, null, ex);
            this.socket.close();
            this.thread.interrupt();
        }
        
        ProcessMessage(MachineIndex);
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
    
    private void ProcessMessage(int MachineIndex){
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
                        while (true) {
                            // receiving checksum
                            DatagramPacket checksumPacket = new DatagramPacket(new byte[1024], 1024);
                            socket.receive(checksumPacket);
                            StringBuffer checksum = (StringBuffer) TypeConverter.deserialize(checksumPacket.getData());

                            // receiving frame
                            receivedData = new byte[65507];
                            socket.receive(new DatagramPacket(receivedData, receivedData.length));
                            StringBuffer cksFrame = TypeConverter.toHexFormat(receivedData);

                            // detecting errors:
                            if (checksum.toString().equals(cksFrame.toString())) {
                                // sending ACK
                                int ack = 1;
                                DatagramPacket ackPacket = new DatagramPacket(TypeConverter.serialize(ack), TypeConverter.serialize(ack).length, IPDest, DestPort);
                                socket.send(ackPacket);
                                
                                // add element to FrameVector
                                FrameVector.addElement(receivedData);
                                break;
                            }
                            else {
                                // sending ACK
                                int ack = -1;
                                DatagramPacket ackPacket = new DatagramPacket(TypeConverter.serialize(ack), TypeConverter.serialize(ack).length, IPDest, DestPort);
                                socket.send(ackPacket);
                            }
                        }
                    }
                    while (true) {
                        // receiving checksum
                        DatagramPacket checksumPacket = new DatagramPacket(new byte[1024], 1024);
                        socket.receive(checksumPacket);
                        StringBuffer checksum = (StringBuffer) TypeConverter.deserialize(checksumPacket.getData());

                        // receiving frame
                        receivedData = new byte[PacketSize % (65507)];
                        socket.receive(new DatagramPacket(receivedData, receivedData.length));
                        StringBuffer cksFrame = TypeConverter.toHexFormat(receivedData);

                        // detecting errors:
                        if (checksum.toString().equals(cksFrame.toString())) {
                            int ack = 1;
                            // sending ACK
                            DatagramPacket ackPacket = new DatagramPacket(TypeConverter.serialize(ack), TypeConverter.serialize(ack).length, IPDest, DestPort);
                            socket.send(ackPacket);

                            // add element to FrameVector
                            FrameVector.addElement(receivedData);
                            break;
                        } else {
                            int ack = -1;
                            // sending ACK
                            DatagramPacket ackPacket = new DatagramPacket(TypeConverter.serialize(ack), TypeConverter.serialize(ack).length, IPDest, DestPort);
                            socket.send(ackPacket);
                        }
                    }
                }
                else {
                    // (12) receiving chunk (one chunk have more than 1 frame)
                    for (int i = 0; i < nFrames; i++) {
                        while (true) {
                            // receiving checksum
                            DatagramPacket checksumPacket = new DatagramPacket(new byte[1024], 1024);
                            socket.receive(checksumPacket);
                            StringBuffer checksum = (StringBuffer) TypeConverter.deserialize(checksumPacket.getData());

                            // receiving frame
                            receivedData = new byte[65507];
                            socket.receive(new DatagramPacket(receivedData, receivedData.length));
                            StringBuffer cksFrame = TypeConverter.toHexFormat(receivedData);

                            // detecting errors:
                            if (checksum.toString().equals(cksFrame.toString())) {
                                // sending ACK
                                int ack = 1;
                                DatagramPacket ackPacket = new DatagramPacket(TypeConverter.serialize(ack), TypeConverter.serialize(ack).length, IPDest, DestPort);
                                socket.send(ackPacket);
                                
                                // add element to FrameVector
                                FrameVector.addElement(receivedData);
                                break;
                            }
                            else {
                                // sending ACK
                                int ack = -1;
                                DatagramPacket ackPacket = new DatagramPacket(TypeConverter.serialize(ack), TypeConverter.serialize(ack).length, IPDest, DestPort);
                                socket.send(ackPacket);
                            }
                        }
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
                
                // updating Process bar
                double percentage = ((double)(PacketSize * 100)/(long)this.Interface.GetTableDownloadProcess().getModel().getValueAt(0, 1));
                long value = round(this.Interface.getPgbDownLoad().getValue() + percentage);
                this.Interface.getPgbDownLoad().setValue((int)value);
            }
            else {
                System.out.println("Error");
            }
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(ProcessingThread.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        finally {
            this.socket.close();
            this.Interface.NoMachineAccessing[MachineIndex] -= 1;
            ProcessingThread.ReceivingChunkMutex[MachineIndex].release();
        }
    }
    
    private void setIPDest() {
        int j = ProcessingThread.randInt(0, this.Interface.AddrContainingFile.size() - 1);
        this.IPDest = this.Interface.AddrContainingFile.elementAt(j);
    }
    
    public void setChunkID(int ChunkID) {
        this.ChunkID = ChunkID;
    }
    
    private int BindSocketAndDestAddr() {
        synchronized(this.Interface.NoMachineAccessing) {
            int machine = -1;
            machine = Min(this.Interface.NoMachineAccessing);
            this.Interface.NoMachineAccessing[machine] += 1;
            this.IPDest = this.Interface.AddrContainingFile.elementAt(machine);
            this.Interface.receivers.addElement(this);
            return machine;
        }
    }
    private static int Min(int[] arr) {
        int m = arr[0];
        int index = 0;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] < m) {
                m = arr[i];
                index = i;
            }
        }
        return index;
    }
}