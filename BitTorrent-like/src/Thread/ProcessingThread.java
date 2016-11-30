/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Thread;

import BusinessLogic.UploadingFile;
import GraphicInterface.MainInterface;
import java.io.IOException;
import java.net.DatagramSocket;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author admin
 */
public class ProcessingThread extends ChunkReceiver implements Runnable{
    private Integer receivedMessage;
    
    public ProcessingThread(String IP, MainInterface Interface, DatagramSocket sock) throws IOException {
        super(IP, Interface, sock);
    }
    
    public void run(){
        /*try {
            // a new thread is created to catch another message
            ProcessingThread thread = new ProcessingThread(this.Interface);
            thread.start();
            
            // catch and process replied message
            this.ProcessMessage();
        } catch (IOException ex) {
            Logger.getLogger(ProcessingThread.class.getName()).log(Level.SEVERE, null, ex);
            this.socket.close();
        }*/
    }
    
    public  void ProcessMessage() {
        /*try {
            // (2) other machine reply and we catch the message
            receivedMessage = is.readInt();
            
            synchronized (receivedMessage) {
                // the replied message is 0 :
                if (receivedMessage == 0) {
                    // (3)
                    Vector<UploadingFile> FoundFiles = (Vector<UploadingFile>) is.readObject();

                    for (UploadingFile File : FoundFiles) {
                        DefaultTableModel model = (DefaultTableModel) this.Interface.GetTableDownloadProcess().getModel();
                        model.addRow(new Object[]{File.getName(), File.getSize(), ""});
                    }
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ProcessingThread.class.getName()).log(Level.SEVERE, null, ex);
            this.socket.close();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ProcessingThread.class.getName()).log(Level.SEVERE, null, ex);
            this.socket.close();
        }*/
    }
}
