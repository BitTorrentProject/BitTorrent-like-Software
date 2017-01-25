/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ThuNghiemTinhNang;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author admin
 */
public class Disposing {
    static Thread t = null;
    static Runnable Thread2 = new Runnable() {
        int th = 1;

        public void run() {
            /*synchronized (this) {
             try {
             this.wait();
             } catch (InterruptedException ex) {
             Logger.getLogger(Disposing.class.getName()).log(Level.SEVERE, null, ex);
             }
             }*/

            /*if (th == 1) {
                t.interrupt();
                th++;
                t.start();
            }*/
            
            int S = 0;
            for (int i = 0; i < 10; i++) {
                S += i;
            }
            if (th == 1)
                System.out.println("end thread 2 " + S);
            else
                System.out.println(th + " end thread 2 " + S);
        }
    };
    public static void main(String args[]) throws InterruptedException {
        
        t = new Thread(Thread2);
        t.start();
        t.interrupt();
        //t.start();
        
        /*Runnable Thread1 = new Runnable() {
            int t = 0;
            public Thread T1 = new Thread(this);
            public void run() {
                while (true) {
                    int S = 0;
                    for (int j = 0; j < 10; j++) {
                        S += j;
                    }
                    System.out.println("end thread 1 " + S);
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Disposing.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    synchronized (Thread2) {
                        if (isAlive()) {
                            Thread2.notifyAll();
                            for (int i = 0; i < 5; i++) {
                                System.out.println("T1 " + i);
                            }
                            return;
                        } else {
                        }
                    }
                }
            }
        };*/
        
        t.join();
       
    }
}
