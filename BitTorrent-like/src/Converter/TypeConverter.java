/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Converter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author admin
 */
public class TypeConverter {
    
    // converting object to byte array
    public static byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(obj);
        os.close();
        return out.toByteArray();
    }
    
    //converting byte array to object
    public static Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ObjectInputStream is = new ObjectInputStream(in);
        Object obj = is.readObject();
        is.close();
        return obj;
    }
    
    public static StringBuffer toHexFormat(byte[] array) {
        StringBuffer sb = new StringBuffer();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(array);
            byte[] byteData = md.digest();
            
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(TypeConverter.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return sb;
    }
    
    public static void main (String arg[]) throws IOException, ClassNotFoundException {
        byte[] a = {1,2,3, 4};
        byte[] b = {1,2,3, 4};
        
        System.out.println(TypeConverter.toHexFormat(a).toString());
        System.out.println(TypeConverter.toHexFormat(b).toString());
    }
    
}
