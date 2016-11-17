/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BusinessLogic;

import java.io.Serializable;
<<<<<<< HEAD
import java.util.Vector;
=======
<<<<<<< HEAD
import java.util.Vector;
=======
>>>>>>> origin/master
>>>>>>> origin/master

/**
 *
 * @author admin
 */
public class Chunk implements Serializable{
    private int ID;
<<<<<<< HEAD
=======
<<<<<<< HEAD
>>>>>>> origin/master
    private Vector<Machine> ContainingMachine;
    private double Size;
    private byte[] Data;
    String Hash;
<<<<<<< HEAD
=======
=======
    //private Machine ContainingMachine;
    private double size;
    private byte[] data;
    
>>>>>>> origin/master
>>>>>>> origin/master
    public Chunk(int PacketID, double Size, byte[] bytes){
        ID = PacketID;
        this.Size = Size;
        this.Data = bytes.clone();
    }
    public int getID() {
        return ID;
    }
    public double getSize(){
        return Size;
    }
    public byte [] getData(){
        return Data;
    }
}