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
    private int ChunkID;
    private static Semaphore[] ReceivingChunkMutex;
    
    public ProcessingThread(String IPNeighbor, MainInterface Interface, DatagramSocket sock, int LocalPort)
    {
        super(IPNeighbor, Interface, sock, LocalPort);
    }
    
    public static void setReceivingChunkMutex(int n) {
        //ReceivingChunkMutex = new Semaphore[this.Interface.NoMachineAccessing.length];
        ReceivingChunkMutex = new Semaphore[n];
        for (int i = 0; i < ProcessingThread.ReceivingChunkMutex.length; i++) {
            ReceivingChunkMutex[i] = new Semaphore(1, true);
        }
    }
    
    public void run(){
        // set request
        this.SetRequest(3);
        
        boolean WorkFine = false;
        
        while (WorkFine == false) {
            // find peer to connect to get chunk
            int PeerIndex = this.FindPeerNumberToConnect();

            // acquire permit of semaphore
            try {
                // this means : there are no peers active to download from --> return
                if (this.Interface.NoMachineAccessing[PeerIndex] == (int) Double.POSITIVE_INFINITY) {
                    System.out.println("Error tranferring of chunk " + this.ChunkID);
                    return; // ending thread
                }

                // create socket and binding port
                if (this.socket == null || this.socket.isClosed()) {
                    this.SrcPort = PortFinder.findFreePort();
                    this.socket = new DatagramSocket(this.SrcPort);
                }
                
                // acquire permit to download
                ProcessingThread.ReceivingChunkMutex[PeerIndex].acquire();
            } catch (InterruptedException | SocketException ex) {
                Logger.getLogger(ProcessingThread.class.getName()).log(Level.SEVERE, null, ex);
                this.socket.close();
                this.thread.interrupt(); // ending thread
            }

            WorkFine = ProcessMessage(PeerIndex);
        }
    }
    
    private static int randInt(int min, int max) {

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
    
    private boolean ProcessMessage(int MachineIndex){
        try {
            byte[] receivedData = new byte[1024];
            int PacketSize = 0;
            
            // sending request
            this.SendRequest();
            
            // (10) sending object ID you want to download
            DatagramPacket ObectIDPacket = new DatagramPacket(TypeConverter.serialize(this.ChunkID), TypeConverter.serialize(this.ChunkID).length, IPDest, DestPort);
            socket.send(ObectIDPacket);
            
            // (11) receiving Data Packet size (size of byte array in 1 chunk)
            try {
                socket.setSoTimeout(3000);
                socket.receive(new DatagramPacket(receivedData, receivedData.length));
                PacketSize = (int) TypeConverter.deserialize(receivedData);
            } catch (SocketException ex) {
                // stop current thread to start a new downloader
                this.ReDownload(MachineIndex);
                return false;
            }
            
            // if the file is found at server machine (PacketSize != -1)
            if (PacketSize >= 0) {
                System.out.println(this.IPDest + ", Chunk " + this.ChunkID + " length = " + PacketSize);
                // calculating no of frames will be received
                int nFrames = PacketSize / (65507);
                if (PacketSize % (65507) != 0)
                    nFrames += 1;
                
                Vector<byte[]> FrameVector = new Vector<byte[]>();
                
                // 
                if (PacketSize % (65507) != 0) {
                    for (int i = 0; i < nFrames - 1; i++) {
                        while (true) {
                            StringBuffer checksum = null, cksFrame = null;
                            
                            // (12) receiving checksum
                            try {
                                socket.setSoTimeout(5000);
                                DatagramPacket checksumPacket = new DatagramPacket(new byte[1024], 1024);
                                socket.receive(checksumPacket);
                                checksum = (StringBuffer) TypeConverter.deserialize(checksumPacket.getData());
                            } catch (SocketException ex) {
                                // stop current thread to start a new downloader
                                this.ReDownload(MachineIndex);
                                return false;
                            }
                            
                            // (13) receiving frame
                            try {
                                receivedData = new byte[65507];
                                socket.setSoTimeout(5000);
                                socket.receive(new DatagramPacket(receivedData, receivedData.length));
                                cksFrame = TypeConverter.toHexFormat(receivedData);
                            } catch (SocketException ex) {
                                // stop current thread to start a new downloader
                                this.ReDownload(MachineIndex);
                                return false;
                            }
                            
                            // detecting errors:
                            if (checksum.toString().equals(cksFrame.toString())) {
                                // (14) sending ACK
                                int ack = 1;
                                DatagramPacket ackPacket = new DatagramPacket(TypeConverter.serialize(ack), TypeConverter.serialize(ack).length, IPDest, DestPort);
                                socket.send(ackPacket);
                                
                                // add element to FrameVector
                                FrameVector.addElement(receivedData);
                                break;
                            }
                            else {
                                // sending ACK : error
                                int ack = -1;
                                DatagramPacket ackPacket = new DatagramPacket(TypeConverter.serialize(ack), TypeConverter.serialize(ack).length, IPDest, DestPort);
                                socket.send(ackPacket);
                            }
                        }
                    }
                    while (true) {
                        StringBuffer checksum = null, cksFrame = null;

                        // (12) receiving checksum
                        try {
                            socket.setSoTimeout(5000);
                            DatagramPacket checksumPacket = new DatagramPacket(new byte[1024], 1024);
                            socket.receive(checksumPacket);
                            checksum = (StringBuffer) TypeConverter.deserialize(checksumPacket.getData());
                        } catch (SocketException ex) {
                            // stop current thread to start a new downloader
                            this.ReDownload(MachineIndex);
                            return false;
                        }
                        
                        // (13) receiving frame
                        try {
                            socket.setSoTimeout(5000);
                            receivedData = new byte[PacketSize % (65507)];
                            socket.receive(new DatagramPacket(receivedData, receivedData.length));
                            cksFrame = TypeConverter.toHexFormat(receivedData);
                        } catch (SocketException ex) {
                            // stop current thread to start a new downloader
                            this.ReDownload(MachineIndex);
                            return false;
                        }
                        
                        // detecting errors:
                        if (checksum.toString().equals(cksFrame.toString())) {
                            int ack = 1;
                            // (14) sending ACK
                            DatagramPacket ackPacket = new DatagramPacket(TypeConverter.serialize(ack), TypeConverter.serialize(ack).length, IPDest, DestPort);
                            socket.send(ackPacket);

                            // add element to FrameVector
                            FrameVector.addElement(receivedData);
                            break;
                        } else {
                            int ack = -1;
                            
                            // (14) sending ACK : error
                            DatagramPacket ackPacket = new DatagramPacket(TypeConverter.serialize(ack), TypeConverter.serialize(ack).length, IPDest, DestPort);
                            socket.send(ackPacket);
                        }
                    }
                }
                else {
                    for (int i = 0; i < nFrames; i++) {
                        while (true) {
                            StringBuffer checksum = null, cksFrame = null;
                            
                            // (12) receiving checksum
                            try {
                                socket.setSoTimeout(5000);
                                DatagramPacket checksumPacket = new DatagramPacket(new byte[1024], 1024);
                                socket.receive(checksumPacket);
                                checksum = (StringBuffer) TypeConverter.deserialize(checksumPacket.getData());
                            } catch (SocketException ex) {
                                // stop current thread to start a new downloader
                                this.ReDownload(MachineIndex);
                                return false;
                            }
                            
                            // (13) receiving frame
                            try {
                                receivedData = new byte[65507];
                                socket.setSoTimeout(5000);
                                socket.receive(new DatagramPacket(receivedData, receivedData.length));
                                cksFrame = TypeConverter.toHexFormat(receivedData);
                            } catch (SocketException ex) {
                                // stop current thread to start a new downloader
                                this.ReDownload(MachineIndex);
                                return false;
                            }
                            
                            // detecting errors:
                            if (checksum.toString().equals(cksFrame.toString())) {
                                // (14) sending ACK
                                int ack = 1;
                                DatagramPacket ackPacket = new DatagramPacket(TypeConverter.serialize(ack), TypeConverter.serialize(ack).length, IPDest, DestPort);
                                socket.send(ackPacket);
                                
                                // add element to FrameVector
                                FrameVector.addElement(receivedData);
                                break;
                            }
                            else {
                                // (14) sending ACK : error
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
                System.out.println(this.IPDest.toString() + " ******* " + this.ChunkID + " ******* " + ChunkArray.length);
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
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ProcessingThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally {
            this.socket.close();
            this.Interface.NoMachineAccessing[MachineIndex] -= 1;

            // releasing mutex
            ProcessingThread.ReceivingChunkMutex[MachineIndex].release();
            return true;
        }
    }
    
    private void setIPDest() {
        int j = ProcessingThread.randInt(0, this.Interface.AddrContainingFile.size() - 1);
        this.IPDest = this.Interface.AddrContainingFile.elementAt(j);
    }
    
    public void setChunkID(int ChunkID) {
        this.ChunkID = ChunkID;
    }
    
    private int FindPeerNumberToConnect() {
        int machine = -1;
        synchronized(this.Interface.NoMachineAccessing) {
            machine = Min(this.Interface.NoMachineAccessing);
            this.Interface.NoMachineAccessing[machine] += 1;
            this.IPDest = this.Interface.AddrContainingFile.elementAt(machine);
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
    
    /**
     *
     * @param MachineIndex the value of MachineIndex
     */
    private void ReDownload(int MachineIndex){
        //  IF AN ERROR EXISTS WHILE THANSFERRING
        System.out.println("Error tranferring of chunk " + this.ChunkID);
        
        // set this peer to Infinity : It's not possible to download from peer MachineIndex
        this.Interface.NoMachineAccessing[MachineIndex] = (int) Double.POSITIVE_INFINITY;

        // releasing mutex
        ProcessingThread.ReceivingChunkMutex[MachineIndex].release();
    }
}