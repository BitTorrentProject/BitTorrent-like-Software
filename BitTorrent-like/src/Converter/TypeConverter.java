/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Converter;

import BusinessLogic.Chunk;
import BusinessLogic.Machine;
import GraphicInterface.MainInterface;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Vector;

/**
 *
 * @author admin
 */
public class TypeConverter {

    public static byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(obj);
        os.close();
        return out.toByteArray();
    }

    public static Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ObjectInputStream is = new ObjectInputStream(in);
        Object obj = is.readObject();
        is.close();
        return obj;
    }
    
    public static void main (String arg[]) throws IOException, ClassNotFoundException {
        MainInterface Interface = new MainInterface();
        
        
        byte[] b = TypeConverter.serialize((Object)Interface.GetMachine());
        
        Machine m = (Machine)TypeConverter.deserialize(b);
        
        for (int i = 0; i < m.GetFiles().size();i++)
            System.out.println(m.GetFiles().elementAt(i).getName());
        
        System.out.println(m.getIPAddr().toString());
    }
}
