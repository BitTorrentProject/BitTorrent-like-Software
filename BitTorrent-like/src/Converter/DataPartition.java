/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Converter;

import BusinessLogic.UploadingFile;
import java.io.File;
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
        
        int size =ObjectArray.length ;
        int N=ObjectArray.length/1024;
        // N là số block/packet
        // xử lý phần block nguyên
        for(int i=0;i<N;i++)
        {
            int destPos=i*1024;
            byte[] packet = new byte[1024];
            System.arraycopy(ObjectArray, destPos, packet, 0, 1024);
            object.addElement(packet);
        }
        // xử lý phần block bị thiếu
        if(size%1024!=0)
        {
            byte[] packet = new byte[size%1024]; 
            int k=0;
            for(int i = N*1024;i<size;i++)
            {
               packet[k++]=ObjectArray[i];
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
            System.arraycopy(packet, 0, ObjectByteArray, j * 1024, packet.length);
            
            j++;
        }
        
        return ObjectByteArray;
    }
    
    public static void main(String args[]) throws IOException, ClassNotFoundException {
        /*String str = "toan khung";
        
        byte[] strClone = TypeConverter.serialize((Object)str);
        DataPartition.SeparateObjectByteArray(strClone);
        
        //String strCopied = (String)TypeConverter.deserialize(DataPartition.Assemble());
        
        //System.out.println("str = " + strCopied);*/
        
        /*Vector<UploadingFile> UF = new Vector<UploadingFile>(0);
        
        new File("BitTorrent").mkdir();
        File folder = new File("BitTorrent");
        for (final File fileEntry : folder.listFiles()) {
            if (!fileEntry.isDirectory() && !fileEntry.getName().endsWith(".torrent")) {
                UploadingFile UploadedFile = new UploadingFile(fileEntry,0);
                UF.addElement(UploadedFile);
            }
        }
        
        byte[] strClone = TypeConverter.serialize((Object)UF);
        System.out.println("length = "+ strClone.length);
        DataPartition.SeparateObjectByteArray(strClone);
        
        Vector<byte[]> copy = (Vector<byte[]>)object.clone();
        Vector<UploadingFile> strCopied = (Vector<UploadingFile>)TypeConverter.deserialize(DataPartition.Assemble(copy));
        
        //System.out.println(strClone.length + " str = " + strCopied.elementAt(0).getName());*/
        
        String str = "06-BGP.txt.torrent";
        String st[] = str.split(".torrent");
        
        for (String s : st){
            System.out.println(s);
        }
        System.out.println(st.length);
    }
}
