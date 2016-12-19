/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BusinessLogic;

import java.io.Serializable;

/**
 *
 * @author admin
 */
public class Chunk implements Serializable{
    private int ID;
    private String ContainingFileName;
    private double Size;
    private byte[] Data;
    
    public Chunk(int PacketID, double Size, byte[] bytes, String FileName){
        ID = PacketID;
        this.Size = Size;
        this.Data = bytes.clone();
        ContainingFileName = FileName;
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
    public String getContainingFileName() {
        return ContainingFileName;
    }
}