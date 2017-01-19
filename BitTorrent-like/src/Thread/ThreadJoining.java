/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Thread;

import BusinessLogic.UploadingFile;
import GraphicInterface.MainInterface;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author admin
 */
public class ThreadJoining implements Runnable{

    public Thread getThread() {
        return thread;
    }
    private Thread thread;
    private MainInterface Interface;
    public ThreadJoining(MainInterface Interface) {
        thread = new Thread(this);
        this.Interface = Interface;
    }
    
    @Override
    public void run() {
        try {
            for (ProcessingThread t : this.Interface.receivers) {
                t.getThread().join();
            }
            
            // create file downloaded at local
            UploadingFile DownloadedFile  = new UploadingFile(this.Interface.Chunks);
            this.Interface.GetMachine().AddFile(DownloadedFile);
            
            // Writing file torrent
            File BittorrentFile = new File("BitTorrent//" + this.Interface.StaticFileTorrent.getName());
            UploadingFile.copyFileUsingChannel(this.Interface.StaticFileTorrent, BittorrentFile);
            
            // insert to table of local files
            DefaultTableModel model = (DefaultTableModel) this.Interface.getTableFileList().getModel();
            model.addRow(new Object[]{DownloadedFile.getLocalFile().getName(), MainInterface.RoundFileSize(DownloadedFile.getLocalFile().length()), "available"});
            this.Interface.getTableFileList().setModel(model);
        } catch (InterruptedException | IOException ex) {
            Logger.getLogger(ThreadJoining.class.getName()).log(Level.SEVERE, null, ex);

        }
        finally {
            this.thread.interrupt();
        }
    }
    
    public void start(){
        thread.start();
    }
}