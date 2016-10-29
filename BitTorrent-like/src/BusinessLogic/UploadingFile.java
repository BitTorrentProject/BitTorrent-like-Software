/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BusinessLogic;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author admin
 */
public class UploadingFile {
    private String FileName;
    private List<Chunk> chunks = new ArrayList();;
    
    private long size;
    
    public UploadingFile(File f, int request) {
        if (f.exists() && f.isFile()) {
            size = f.length();
            FileName = f.getName();
            
            // copying file to folder BitTorrent
            Path p = Paths.get(f.getAbsolutePath());
            try {
                if(request == 1) {
                    File BittorrentFile = new File("BitTorrent//" + f.getName());
                    this.copyFileUsingChannel(f, BittorrentFile);
                }    
            } catch (IOException ex) {
                Logger.getLogger(UploadingFile.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            // devide file into chunks
            this.DevideFileIntoChunks(p);
        }
    }
    
    //--------------------------Get methods--------------------
    public String GetName(){
        return FileName;
    }
    public long GetSize() {
        return this.size;
    }
    public List<Chunk> GetChunks() {
        return this.chunks;
    }
    
    //---------------------------File functions methods--------------------------
    private void copyFileUsingChannel(File source, File dest) throws IOException {
      FileChannel sourceChannel = null;
      FileChannel destChannel = null;
      try {
          sourceChannel = new FileInputStream(source).getChannel();
          sourceChannel = new FileInputStream(source).getChannel();
          
          destChannel = new FileOutputStream(dest).getChannel();
          destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
         }finally{
             sourceChannel.close();
             destChannel.close();
         }
    }   
   
    //------------------------File dividing method------------------------------
    private void DevideFileIntoChunks(Path p) {
        byte[] fileArray = null;
        try {
            fileArray = Files.readAllBytes(p);
        } catch (IOException ex) {
            Logger.getLogger(UploadingFile.class.getName()).log(Level.SEVERE, null, ex);
        }

        // deviding file into chunks
        if (size % (1024 * 1024) == 0) {
            for (int i = 1; i <= size / (1024 * 1024); i++) {
                byte[] ChunkBytes = new byte[1024 * 1024];
                int k = 0;
                for (int j = (int) ((i - 1) * (Math.pow(1024, 2) + 1)); j <= i * (Math.pow(1024, 2) + 1) - 1; j++) {
                    if (k < ChunkBytes.length) {
                        ChunkBytes[k] = fileArray[j];
                    }
                    k++;
                }
                Chunk NewChunk = new Chunk(i, 1, ChunkBytes);
                chunks.add(NewChunk);
            }
        } else {
            int i = 1;
            int j = 0;
            for (i = 1; i <= size / (1024 * 1024); i++) {
                byte[] ChunkBytes = new byte[1024 * 1024];
                int k = 0;
                for (j = (int) ((i - 1) * (Math.pow(1024, 2) + 1)); j <= i * (Math.pow(1024, 2) + 1) - 1; j++) {
                    if (k < ChunkBytes.length) {
                        ChunkBytes[k] = fileArray[j];
                    }
                    k++;
                }
                Chunk NewChunk = new Chunk(i, 1, ChunkBytes);
                chunks.add(NewChunk);
            }

            // adding 1 byte left
            double MByteLeft = ((double)(size)/ (1024 * 1024)) - (int)(size / (1024 * 1024));
            byte[] ChunkBytes = new byte[1024 * 1024];
            int k = 0;
            while (j < size) {
                if (k < ChunkBytes.length) {
                    ChunkBytes[k] = fileArray[j];
                }
                k++;
                j++;
            }
            Chunk NewChunk = new Chunk(i, MByteLeft, ChunkBytes);
            chunks.add(NewChunk);
        }
    }
    
    public void WriteFileInfoToTorrent() throws IOException{
        File file = new File("BitTorrent//" + this.FileName + ".torrent");
        FileOutputStream fos = new FileOutputStream(file);
 
	BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
        bw.write(FileName + "-----------Size: " + this.size/(1024*1024) + "MB \n");
        bw.newLine();
        for (int i = 0; i < this.chunks.size(); i++){
            Chunk chunk = this.chunks.get(i);
            bw.write("++ chunk : " + chunk.getID() + "\n");
            bw.newLine();
            String n = String.format("%.10f", chunk.GetSize());
            bw.write("++ size: " + n + "\n");   
            bw.newLine();
        }
        
        bw.flush();
        bw.close();
    }
    
    public static void main(String args[]){
        //File f = new File("D:\\Nhac\\Giao trinh Bolero Full.pdf");
        //UploadingFile file = new UploadingFile(f,1);
        //System.out.println("Finish running");

        long size = 11688251;
        double a = ((double)(size)/ (1024 * 1024));
        String a1 = String.format("%.10f", a);
        System.out.println(a1);
        int b = (int)(size / (1024 * 1024));
        System.out.println(b);
        
        double MByteLeft = a - b;
        String n = String.format("%.10f", MByteLeft);
        System.out.print(n);
    }
}