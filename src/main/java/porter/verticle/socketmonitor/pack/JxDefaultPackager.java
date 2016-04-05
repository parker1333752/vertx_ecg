package porter.verticle.socketmonitor.pack;

import javax.swing.text.html.HTML;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by parker on 2015/10/21.
 */
public class JxDefaultPackager implements JiPackager{

    private static final byte TAG = 0x01;
    @Override
    public byte[] pack(byte[] data) {
        LinkedList<Byte> l_data = new LinkedList<>();
        for (int i = 0; i < data.length; i++) {
            l_data.add(data[i]);
        }
        Iterator<Byte> iter = l_data.iterator();
        byte[] rt = new byte[l_data.size()];
        int i;
        for(i=0;i<l_data.size() && iter.hasNext();++i){
            rt[i] = iter.next();
        }
        return rt;
    }

    @Override
    public byte[] unpack(LinkedList<Byte> data) {
        Iterator<Byte> iter = data.iterator();
        byte[] rt = new byte[data.size()];
        int i;
        for(i=0;i<data.size() && iter.hasNext();++i){
            rt[i] = iter.next();
        }
        return rt;
    }

    @Override
    public void discardFrame(LinkedList<Byte> data) {
        if(data.get(0) != TAG){
            System.err.println("JxDefaultPackager [ERROR] : TAG is not match.");
            while(data.poll() != TAG); // Tag;
        }
        short length = data.poll(); // Length;
        length += ((short)data.poll())<<8;
        while((--length)>=0)data.poll(); // Value;
    }
}
