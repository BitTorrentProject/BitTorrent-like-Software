/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BusinessLogic;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author admin
 */
public class Machine {
    private String IPAddr;
    private Vector<UploadingFile> Files;
    private Vector<Machine> ConnectedMachines;
<<<<<<< HEAD
    private Socket Socket;
    //private List<Chunk> chunksInside;
=======
<<<<<<< HEAD
    private Socket Socket;
    //private List<Chunk> chunksInside;
=======
    private Socket socket;
    private List<Chunk> chunksInside;
>>>>>>> origin/master
>>>>>>> origin/master
    
    public Machine() {
        InetAddress addressIP = null;
        try {
            addressIP = InetAddress.getLocalHost();
        } catch (UnknownHostException ex) {
            Logger.getLogger(Machine.class.getName()).log(Level.SEVERE, null, ex);
        }
        IPAddr = addressIP.getHostAddress().toString();
        
        Files = new Vector<>();
        ConnectedMachines = new Vector<>();
        /*File folder = new File("BitTorrent");
        
        for (final File fileEntry : folder.listFiles()) {
            if (!fileEntry.isDirectory()) {
                UploadingFile UploadedFile = new UploadingFile(fileEntry);
                Files.addElement(UploadedFile);
                System.out.println(UploadedFile.GetName());
            } else {
                //System.out.println(fileEntry.getName());
            }
        }*/
    }
    
    public void AddFile(UploadingFile file) {
        this.Files.addElement(file);
    }
    
    public void RemoveFileAt(int index){
        this.Files.remove(index);
    }
    
    public Vector<UploadingFile> GetFiles() {
        return this.Files;
    }
    
<<<<<<< HEAD
//    public List<Chunk> getChunksInside(){
//        return this.chunksInside;
//    }
=======
<<<<<<< HEAD
//    public List<Chunk> getChunksInside(){
//        return this.chunksInside;
//    }
=======
    public List<Chunk> getChunksInside(){
        return this.chunksInside;
    }
>>>>>>> origin/master
>>>>>>> origin/master
    
    public static void main(String args[]){
        Machine m = new Machine();
        System.out.println("Finish running");
    }
}