/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Thread;

import BusinessLogic.UploadingFile;
import GraphicInterface.MainInterface;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author admin
 */
public class ThreadJoining implements Runnable{
    private Thread t;
    private MainInterface Interface;
    public ThreadJoining(MainInterface Interface) {
        t = new Thread(this);
        this.Interface = Interface;
    }
    
    @Override
    public void run() {
        /*for (ProcessingThread thread : this.Interface.receivers) {
            try {
                thread.getThread().join();
            } catch (InterruptedException ex) {
                Logger.getLogger(ThreadJoining.class.getName()).log(Level.SEVERE, null, ex);
            }
        }*/
        
        while (this.check(this.Interface.Chunks.size() - 1) == false){
            
        }
        
        try {
            // create file downloaded at local
            UploadingFile file  = new UploadingFile(this.Interface.Chunks);
            this.Interface.GetMachine().AddFile(file);
        } catch (IOException ex) {
            Logger.getLogger(ThreadJoining.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void start(){
        t.start();
    }
    
    private boolean check(int index){
        if (index == 0) {
            if (this.Interface.Chunks.get(index) == null)
                return false;
            else
                return true;
        } else {
            if (this.Interface.Chunks.get(index) == null)
                return false;
            else
                return this.check(index - 1);
        }
    }
}