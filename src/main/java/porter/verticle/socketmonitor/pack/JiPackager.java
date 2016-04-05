package porter.verticle.socketmonitor.pack;

import java.util.LinkedList;

/**
 * Created by parker on 2015/10/21.
 */
public interface JiPackager {
    byte[] pack(byte[] data) throws JxPackException;
    byte[] unpack(LinkedList<Byte> data) throws JxUnpackException;

    void discardFrame(LinkedList<Byte> data);
}
