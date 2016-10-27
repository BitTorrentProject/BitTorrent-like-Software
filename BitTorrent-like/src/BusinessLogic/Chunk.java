/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BusinessLogic;

/**
 *
 * @author admin
 */
public class Chunk {
    private int ID;
    private Machine ContainingMachine;
    private double size;
    private byte[] data;
    
    public Chunk(int PacketID, double Size, byte[] bytes){
        ID = PacketID;
        size = Size;
        data = bytes.clone();
    }
}
