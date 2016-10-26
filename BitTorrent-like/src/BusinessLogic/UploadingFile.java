/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BusinessLogic;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author admin
 */
public class UploadingFile {
    private List<Chunk> chunks = new ArrayList();;
    private String FileName;
    private long size;
    
    public UploadingFile(File f) {
        if (f.exists() && f.isFile()) {
            size = f.length();
            FileName = f.getName();
            
            Path p = Paths.get(f.getAbsolutePath());
            byte[] fileArray = null;
            try {
               fileArray = Files.readAllBytes(p);
            } catch (IOException ex) {
                Logger.getLogger(UploadingFile.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            for (int i = 1; i < size/(1024*1024); i++) {
                byte[] ChunkBytes = new byte[1024 * 1024];
                int k = 0;
                for (int j = (int) ((i-1)*(Math.pow(1024, 2)+1)); j <= i*(Math.pow(1024, 2)+1); j++) {
                    if (k < ChunkBytes.length) {
                        ChunkBytes[k] = fileArray[j];
                    }
                    k++;
                }
                Chunk NewChunk = new Chunk(i,(int)size/(1024*1024), ChunkBytes);
                chunks.add(NewChunk);
            }
        }
    }
    
    public String GetName(){
        return FileName;
    }
    
    public long GetSize() {
        return this.size;
    }
}
