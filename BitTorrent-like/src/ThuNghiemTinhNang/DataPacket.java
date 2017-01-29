/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ThuNghiemTinhNang;

import java.net.InetAddress;

/**
 *
 * @author admin
 */
public class DataPacket {
    private Object Data;
    private InetAddress IPSrc;
    private InetAddress IPDest;
    private boolean checksum;
    private int type;
    private int DataLength;
    
    public DataPacket(Object Data, InetAddress IPSrc, InetAddress IPDest, boolean checksum, int type, int DataLength) {
        this.Data = Data;
        this.IPSrc = IPSrc;
        this.IPDest = IPDest;
        this.checksum = checksum;
        this.type = type;
        this.DataLength = DataLength;
    }
    
    public Object getData() {
        return Data;
    }

    public InetAddress getIPSrc() {
        return IPSrc;
    }

    public InetAddress getIPDest() {
        return IPDest;
    }

    public boolean isChecksum() {
        return checksum;
    }

    public int getType() {
        return type;
    }

    public int getDataLength() {
        return DataLength;
    }

    public void setData(Object Data) {
        this.Data = Data;
    }

    public void setIPSrc(InetAddress IPSrc) {
        this.IPSrc = IPSrc;
    }

    public void setIPDest(InetAddress IPDest) {
        this.IPDest = IPDest;
    }

    public void setChecksum(boolean checksum) {
        this.checksum = checksum;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setDataLength(int DataLength) {
        this.DataLength = DataLength;
        int i = 3;
    }
}
