/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Thread;

import BusinessLogic.UploadingFile;
import Converter.DataPartition;
import Converter.TypeConverter;
import GraphicInterface.MainInterface;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Arrays;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author admin
 */
public class ChunkSender implements Runnable{

    public DatagramSocket getSocket() {
        return socket;
    }

    public Thread getThread() {
        return thread;
    }
    
    private DatagramSocket socket;
    private int DestPort;
    private MainInterface Interface;
    private Thread thread;

    public ChunkSender(MainInterface Interface, DatagramSocket sock) {
        thread = new Thread(this);
        this.Interface = Interface;
        socket = sock;
        IsWorking = false;
    }
    
    @Override
    public void run() {
        while (true) {
            try {
                // (0) get message from a machine
                DatagramPacket RequestPacket = new DatagramPacket(new byte[1024], 1024);
                socket.receive(RequestPacket);
              
                int message = (int) TypeConverter.deserialize(RequestPacket.getData());
                
                // a machine wants to search a file in your machine (message = 1)
                if (message == 1) {
                    // (1) receive file's name to search
                    DatagramPacket FileNamePacket = new DatagramPacket(new byte[1024], 1024);
                    socket.receive(FileNamePacket);
                    
                    // searching the file in local
                    File torrent = this.SearchTorrentFile((String)TypeConverter.deserialize(FileNamePacket.getData()));
                    UploadingFile FoundFile = this.SearchFile((String)TypeConverter.deserialize(FileNamePacket.getData()));
                    
                    // (2) receive IP src
                    DatagramPacket IpSrcPacket = new DatagramPacket(new byte[1024], 1024);
                    socket.receive(IpSrcPacket);
                    
                    // (3) receiving src port
                    DatagramPacket PortSrcPacket = new DatagramPacket(new byte[1024], 1024);
                    socket.receive(PortSrcPacket);
                    DestPort = (int)TypeConverter.deserialize(PortSrcPacket.getData());
               
                    // (4) reply this message to confirm that I received the message
                    byte[] reply = TypeConverter.serialize(message);
                    socket.send(new DatagramPacket(reply, reply.length, (InetAddress) TypeConverter.deserialize(IpSrcPacket.getData()),DestPort));
                    
                    if (FoundFile != null && torrent != null) {
                        // (5) sending length of found file
                        socket.send(new DatagramPacket(TypeConverter.serialize(FoundFile.getLocalFile().length()), TypeConverter.serialize(FoundFile.getLocalFile().length()).length, (InetAddress) TypeConverter.deserialize(IpSrcPacket.getData()), DestPort));

                        // (6) send name of found file to client, who want to search
                        socket.send(new DatagramPacket(TypeConverter.serialize(FoundFile.getLocalFile().getName()), TypeConverter.serialize(FoundFile.getLocalFile().getName()).length, (InetAddress) TypeConverter.deserialize(IpSrcPacket.getData()), DestPort));
                    }
                    else {
                        // (5) sending packet length of found files
                        long length = -1;
                        socket.send(new DatagramPacket(TypeConverter.serialize(length), TypeConverter.serialize(length).length, (InetAddress) TypeConverter.deserialize(IpSrcPacket.getData()), DestPort));
                    }
                }
                
                // receiving file torrent
                else if (message == 2) {
                    // (1') receive file torrent's name to search
                    DatagramPacket FileNamePacket = new DatagramPacket(new byte[1024], 1024);
                    socket.receive(FileNamePacket);
                    String FileName = (String)TypeConverter.deserialize(FileNamePacket.getData());
                    
                    // (1) receive file's torrent to test if the file exists
                    DatagramPacket TorrentPacket = new DatagramPacket(new byte[2048], 2048);
                    socket.receive(TorrentPacket);
                    
                    // (2) receive IP src
                    DatagramPacket IpSrcPacket = new DatagramPacket(new byte[1024], 1024);
                    socket.receive(IpSrcPacket);
                    
                    // (3) receiving src port
                    DatagramPacket PortSrcPacket = new DatagramPacket(new byte[1024], 1024);
                    socket.receive(PortSrcPacket);
                    DestPort = (int)TypeConverter.deserialize(PortSrcPacket.getData());
                    
                    // (4) reply this message to confirm that I received the message
                    byte[] reply = TypeConverter.serialize(message);
                    socket.send(new DatagramPacket(reply, reply.length, (InetAddress) TypeConverter.deserialize(IpSrcPacket.getData()),DestPort));
                    
                    File torrent = this.SearchTorrentFile(FileName);
                    
                    if (torrent != null) {
                        byte Torrent[] = this.ReadFileContent(torrent);
                        String str[] = FileName.split(".torrent");
                        UploadingFile file = this.SearchFile(str[0]);
                        
                        if (ChunkSender.CompareByteArray(Torrent, TorrentPacket.getData()) && file != null) {
                            // (7) sending the size of file you want to download          
                            socket.send(new DatagramPacket(TypeConverter.serialize(file.getLocalFile().length()), TypeConverter.serialize(file.getLocalFile().length()).length, (InetAddress) TypeConverter.deserialize(IpSrcPacket.getData()), DestPort));

                            // (8) sending local IP to the machine calling te request
                            socket.send(new DatagramPacket(TypeConverter.serialize(this.Interface.GetMachine().getIPAddr()), TypeConverter.serialize(this.Interface.GetMachine().getIPAddr()).length, (InetAddress) TypeConverter.deserialize(IpSrcPacket.getData()), DestPort));
                            
                            // (9) sending file name back
                            socket.send(new DatagramPacket(TypeConverter.serialize(file.getLocalFile().getName()), TypeConverter.serialize(file.getLocalFile().getName()).length, (InetAddress) TypeConverter.deserialize(IpSrcPacket.getData()), DestPort));
                        } else {
                            // (7) sending file not found message
                            long FNFMessage = -1;
                            System.out.println("1 ) error");
                            this.socket.send(new DatagramPacket(TypeConverter.serialize(FNFMessage), TypeConverter.serialize(FNFMessage).length, (InetAddress) TypeConverter.deserialize(IpSrcPacket.getData()), this.DestPort));
                        }
                    }
                    else {
                        // (7) sending file not found message
                        long FNFMessage = -1;
                        System.out.println("2 ) error");
                        this.socket.send(new DatagramPacket(TypeConverter.serialize(FNFMessage), TypeConverter.serialize(FNFMessage).length, (InetAddress) TypeConverter.deserialize(IpSrcPacket.getData()), this.DestPort));
                    }
                }
                else if (message == 3){
                    // (1) receive file's name to search
                    DatagramPacket FileNamePacket = new DatagramPacket(new byte[1024], 1024);
                    socket.receive(FileNamePacket);
                    
                    // searching the file in local
                    UploadingFile FoundFile = this.SearchFile((String)TypeConverter.deserialize(FileNamePacket.getData()));
                    
                    // (2) receive IP src
                    DatagramPacket IpSrcPacket = new DatagramPacket(new byte[1024], 1024);
                    socket.receive(IpSrcPacket);
                    
                    // (3) receiving src port
                    DatagramPacket PortSrcPacket = new DatagramPacket(new byte[1024], 1024);
                    socket.receive(PortSrcPacket);
                    DestPort = (int)TypeConverter.deserialize(PortSrcPacket.getData());
                    
                    // (10) receiving object ID you want to download
                    DatagramPacket ObectIDPacket = new DatagramPacket(new byte[1024], 1024);
                    socket.receive(ObectIDPacket);
                    int ObjID = (int)TypeConverter.deserialize(ObectIDPacket.getData());
                    
                    if (FoundFile != null){
                        // (11) sending size of packet chunk
                        int length = FoundFile.getChunks().get(ObjID).getData().length;
                        System.out.println("l = " + length);
                        DatagramPacket SizePacket = new DatagramPacket(TypeConverter.serialize(length), TypeConverter.serialize(length).length, (InetAddress) TypeConverter.deserialize(IpSrcPacket.getData()), this.DestPort);
                        socket.send(SizePacket);
                    
                        // devide datapacket into frames and send it
                        Vector<byte[]> Frames = DataPartition.SeparateObjectByteArray(FoundFile.getChunks().get(ObjID).getData(), 65507);
                        for (byte[] Frame : Frames) {
                            int ack = -1;
                            
                            // if ack == -1 : error, the data sent is not received intactly from client -> resend 
                            while (ack == -1) {
                                StringBuffer checksum = TypeConverter.toHexFormat(Frame);
                                DatagramPacket checksumPacket = new DatagramPacket(TypeConverter.serialize(checksum), TypeConverter.serialize(checksum).length, (InetAddress) TypeConverter.deserialize(IpSrcPacket.getData()), this.DestPort);
                                // (12) sending checksum
                                socket.send(checksumPacket);
                                
                                Thread.sleep(1000);
                                
                                // (13) sending frame
                                DatagramPacket FramePacket = new DatagramPacket(Frame, Frame.length, (InetAddress) TypeConverter.deserialize(IpSrcPacket.getData()), this.DestPort);
                                socket.send(FramePacket);
                                
                                try {
                                    //socket.setSoTimeout(5000);
                                    
                                    // (14) receiving ACK
                                    DatagramPacket ACKPacket = new DatagramPacket(new byte[1024], 1024);
                                    socket.receive(ACKPacket);

                                    ack = (int) TypeConverter.deserialize(ACKPacket.getData());
                                } catch (SocketException e) {
                                    System.out.println((InetAddress) TypeConverter.deserialize(IpSrcPacket.getData()) + "********");
                                    continue;
                                }
                            }
                        }
                    }else {
                        // (11) sending file not found mss
                        int FNFMessage = -1;
                        System.out.println("sending error");
                        socket.send(new DatagramPacket(TypeConverter.serialize(FNFMessage), TypeConverter.serialize(FNFMessage).length, (InetAddress) TypeConverter.deserialize(IpSrcPacket.getData()), this.DestPort));
                    }
                }
            } catch (IOException | ClassNotFoundException | InterruptedException ex) {
                Logger.getLogger(ChunkSender.class.getName()).log(Level.SEVERE, null, ex);
                socket.close();
                return;
            }
            
            IsWorking = false;
        }
    }
    
    private UploadingFile SearchFile(String FileName) {
        File folder = new File("BitTorrent");
        UploadingFile FoundFile = null;
        
        // finding FileName
        for (final UploadingFile fileEntry : this.Interface.GetMachine().GetFiles()) {
            if (fileEntry.getLocalFile().getName().equals(FileName)) {
                FoundFile = fileEntry;
                break;
            }
        }   
        return FoundFile;
    }
    
    private File SearchTorrentFile(String FileName) {
        File folder = new File("BitTorrent");
        File TorrentFile = null;
        // finding torrent file of FileName
        for (final File fileEntry : folder.listFiles()) {
            if (!fileEntry.isDirectory() && fileEntry.getName().equals(FileName)) {
                TorrentFile = fileEntry;
                break;
            }
        }
        
        return TorrentFile;
    }
    
    public void start(){
        this.thread.start();
    }
    
    private static boolean CompareByteArray(byte[] arr1, byte[] arr2){
        if (arr1.length > arr2.length){
            byte[] temp = Arrays.copyOf(arr1, arr2.length);
            return CompareByteArray(temp, arr2);
        } else if (arr1.length < arr2.length) {
            byte[] temp = Arrays.copyOf(arr2, arr1.length);
            return CompareByteArray(temp, arr1);
        }
        else {
            return Arrays.equals(arr1, arr2);
        }
    }
    
    private byte[] ReadFileContent(File f){
        byte[] bytes = null;
        try {
            bytes = new byte[(int)f.length()];
            DataInputStream dataIs = new DataInputStream(new FileInputStream(f));
            dataIs.readFully(bytes);
            
            return bytes;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ChunkSender.class.getName()).log(Level.SEVERE, null, ex);
            this.thread.interrupt();
            this.socket.close();
        } catch (IOException ex) {
            Logger.getLogger(ChunkSender.class.getName()).log(Level.SEVERE, null, ex);
            this.thread.interrupt();
            this.socket.close();
        }
        return bytes;
    }
}
