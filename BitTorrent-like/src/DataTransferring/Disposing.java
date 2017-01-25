/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataTransferring;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author admin
 */
public class Disposing {
    public static void main(String args[]) {
        Runnable Thread2 = new Runnable() {
            public void run() {
                synchronized (this) {
                    try {
                        this.wait();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Disposing.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                
                int S = 0;
                for (int i = 0; i < 10; i++) {
                    S += i;
                }
                System.out.println("end thread 2 " + S);
            }
        };
        
        Runnable Thread1 = new Runnable() {
            public void run() {
                synchronized (Thread2) {
                    int S = 0;
                    for (int i = 0; i < 10; i++) {
                        S += i;
                    }
                    System.out.println("end thread 1 " + S);
                    Thread2.notify();
                }
            }
        };
        
        new Thread(Thread2).start();
        new Thread(Thread1).start();
    }
}
