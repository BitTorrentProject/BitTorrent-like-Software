/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BusinessLogic;

import java.io.Serializable;
import java.util.Vector;

/**
 *
 * @author admin
 */
public class Chunk implements Serializable{
    private int ID;
    private Vector<Machine> ContainingMachine;
    private double Size;
    private byte[] Data;
    private Object HashValue;
    
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
    public void setHashValue(Object HashValue) {
        this.HashValue = HashValue;
    }

    public Object getHashValue() {
        return HashValue;
    }
}