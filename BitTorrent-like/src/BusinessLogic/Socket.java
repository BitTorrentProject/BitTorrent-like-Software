/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BusinessLogic;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Vector;

//FUCK THAT SHIT, I'M FUCKED UP
//I SHOULD HAVE USED UPLOADING FILE LIST, BUT I USED CHUNK LIST
//SO NOW I'M FUCKED UP


/**
 *
 * @author m4rk.51jh
 */
public class Socket {
    private DatagramSocket Socket = null;
    //private static List<Chunk> Chunks;
    private static Vector<UploadingFile> Files;

    public Socket() {

    }

    //----------------------Broadcasting Progress----------------------
    
    
    
    
    
    
    
    //----------------------Download Progress----------------------
    public void download(Vector<UploadingFile> FileList) throws IOException {
        try {
            Socket = new DatagramSocket(6060);
            byte[] incomingData = new byte[1024*1024];
            
                
            //datagram packet prepared to download data
            DatagramPacket incomingPacket = new DatagramPacket(incomingData, incomingData.length);
            Socket.receive(incomingPacket);
            //data downloaded

            //transfer data stream into chunk-type
            byte[] data = incomingPacket.getData();
            ByteArrayInputStream bis = new ByteArrayInputStream(data);
            ObjectInputStream ois = new ObjectInputStream(bis);

            //add chunk downloaded to chunk list
            try {
                FileList.get(0).getChunks().add((Chunk) ois.readObject());
                //chunkList.add((Chunk) ois.readObject());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
//                InetAddress IPAddress = incomingPacket.getAddress();
//                int port = incomingPacket.getPort();
//                String reply = "Chunk downloaded";
//                byte[] replyBytea = reply.getBytes();
//                DatagramPacket replyPacket =
//                new DatagramPacket(replyBytea, replyBytea.length, IPAddress, port);
//                socket.send(replyPacket);
            Thread.sleep(2000);
            System.exit(0);

        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException i) {
            i.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    
    //------------Upload Progress------------
    public void upload(Vector<UploadingFile> FileList) {
        try {
            
            Socket = new DatagramSocket();
            
            //transfer chunk into data stream
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(FileList.get(1).getChunks().get((1)));
            byte[] data = bos.toByteArray();
            
            //send chunk to destination address
            InetAddress IPAddress = InetAddress.getByName("localhost");
            DatagramPacket sendPacket = new DatagramPacket(data, data.length, IPAddress, 9876);
            Socket.send(sendPacket);
            System.out.println("Chunk uploaded");
//            byte[] incomingData = new byte[1024*1024];
//            DatagramPacket incomingPacket = new DatagramPacket(incomingData, incomingData.length);
//            socket.receive(incomingPacket);
//            String response = new String(incomingPacket.getData());
//            System.out.println("Response from server:" + response);
            Thread.sleep(2000);

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) throws IOException {
        Socket peer = new Socket();
        //peer.download(chunks);
       // peer.upload(chunks);
    }
}