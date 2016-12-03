/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Converter;

import java.io.IOException;
import java.util.Vector;

/**
 *
 * @author admin
 */
public class DataPartition {
    private static Vector<byte[]> object = new Vector<>();
    private static byte[] ObjectByteArray;
    
    public static Vector<byte[]> SeparateObjectByteArray(byte[] ObjectArray) {
        object.removeAllElements();
        
        long size = ObjectArray.length;
        if (ObjectArray.length % 1024 == 0) {
            for (int i = 1; i <= size / 1024; i++) {
                byte[] packet = new byte[1024];
                int k = 0;
                for (int j = (int) ((i - 1) * (1024 + 1)); j <= i * (1024 + 1) - 1; j++) {
                    if (k < packet.length) {
                        packet[k] = ObjectArray[j];
                    }
                    k++;
                }
                object.addElement(packet);
            }
        }
        else {
            int i = 1;
            int j = 0;
            
            for (i = 1; i <= size / (1024); i++) {
                byte[] packet = new byte[1024];
                int k = 0;
                
                for (j = (int) ((i - 1) * (1024 + 1)); j <= i * (1024 + 1) - 1; j++) {
                    if (k < packet.length) {
                        packet[k] = ObjectArray[j];
                    }
                    k++;
                }
                object.addElement(packet);
            }
            
            // adding 1 byte left
            double BytesLeft = ((double)(size)/ (1024)) - (int)(size / (1024));
            byte[] packet = new byte[1024];
            int k = 0;
            while (j < size) {
                if (k < packet.length) {
                    packet[k] = ObjectArray[j];
                }
                k++;
                j++;
            }
            object.addElement(packet);
        }
        
        return DataPartition.object;
    }
    
    public static byte[] Assemble(Vector<byte[]> VectorObjectByteArray) {
        long ObjectSize = 0;
        for (byte[] packet : VectorObjectByteArray) {
            ObjectSize += packet.length;
        }
        ObjectByteArray = new byte[(int)ObjectSize];
        
        int j = 0;
        for (byte[] packet : VectorObjectByteArray) {
            for (int i = 0; i < packet.length; i++) {
                ObjectByteArray[j] = packet[i];
                j++;
            }
            j++;
        }
        
        return ObjectByteArray;
    }
    
    public static void main(String args[]) throws IOException, ClassNotFoundException {
        String str = "toan khung";
        
        byte[] strClone = TypeConverter.serialize((Object)str);
        DataPartition.SeparateObjectByteArray(strClone);
        
        //String strCopied = (String)TypeConverter.deserialize(DataPartition.Assemble());
        
        //System.out.println("str = " + strCopied);
    }
}
