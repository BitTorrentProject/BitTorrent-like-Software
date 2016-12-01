/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BusinessLogic;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
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
public class Machine  implements Serializable{
    private InetAddress IPAddr;
    private Vector<UploadingFile> UploadedFiles; // your local files
    private Vector<UploadingFile> FoundFiles; // files found in network
    //private Vector<Machine> ConnectedMachines;
    
    //private List<Chunk> chunksInside;
   
    public Machine() {
        //InetAddress addressIP = null;
        try {
            IPAddr = InetAddress.getLocalHost();
        } catch (UnknownHostException ex) {
            Logger.getLogger(Machine.class.getName()).log(Level.SEVERE, null, ex);
        }
        //IPAddr = addressIP.getHostAddress().toString();
        Object o = new Object();
        
        UploadedFiles = new Vector<>();
        FoundFiles = new Vector<>();
        //ConnectedMachines = new Vector<>();
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
    
    public Vector<UploadingFile> getUploadedFiles() {
        return UploadedFiles;
    }

    public Vector<UploadingFile> getFoundFiles() {
        return FoundFiles;
    }
    
    public void AddFile(UploadingFile file) {
        this.UploadedFiles.addElement(file);
    }
    
    public void RemoveFileAt(int index){
        this.UploadedFiles.remove(index);
    }
    
    public Vector<UploadingFile> GetFiles() {
        return this.UploadedFiles;
    }
    
//    public List<Chunk> getChunksInside(){
//        return this.chunksInside;
//    }
    
    public InetAddress getIPAddr() {
        return IPAddr;
    }
    
    public static void main(String args[]){
        Machine m = new Machine();
        System.out.println("Finish running");
    }
}