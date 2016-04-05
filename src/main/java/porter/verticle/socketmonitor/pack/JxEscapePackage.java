package porter.verticle.socketmonitor.pack;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by parker on 2015/11/11.
 */
@Deprecated
public class JxEscapePackage implements JiPackager {
    final static byte Esc = (byte) 0xfa;
    final static byte Start = (byte) 0xfb;
    final static byte End = (byte) 0xfc;
    final static byte _X1 = (byte) 0xff;
    final static byte _X2 = (byte) 0xaa;
    final static byte E_Esc = (byte) 0x01;
    final static byte E_Start = (byte) 0x02;
    final static byte E_End = (byte) 0x03;
    final static byte E_X1 = (byte) 0x04;
    final static byte E_X2 = (byte) 0x05;
    final static int MAX_FRAME_SIZE = 0x8000;

    private byte[] buffer;
    private boolean isEscape;
    private boolean isCompleted;
    private int buffer_index;

    public JxEscapePackage() {
        buffer = null;
    }

    @Override
    public byte[] pack(byte[] data) throws JxPackException {
        throw new JxPackException("This method didn't implement.");
    }

    public byte[] unpack(LinkedList<Byte> data) throws JxUnpackException {
        if(data == null)return null;
        if (isCompleted){ // Starting a new frame processing.
            isCompleted = false;
            resetState();
        }
        if (buffer == null) { // Find frame start byte.
            while (data.size() > 0 && !data.getFirst().equals(this.Start)) data.removeFirst();
            if(data.size() >= 2){
                data.removeFirst();
                if(allocBuffer(data.iterator()) != 0)return null; // Double frame start byte here.
            }else return null; // frame start byte not found.
        } else  { // alloc buffer memeory.
             reallocBuffer(data.iterator());
        }
        dataPackaging(new JxWrappedLinkedList(data));
        if (buffer_index >= buffer.length && data.size()>0 && data.getFirst().equals(this.End)) {
            isCompleted = true;
            if (isEscape) {
                throw new JxUnpackException("Unexpected frame end." + this.Esc);
            }
            data.removeFirst();
            return buffer;
        }
        return null;
    }

    // If error occur, return non-zero;
    private int dataPackaging(JxWrappedLinkedList<Byte> data) throws JxUnpackException {
        Byte tmp;
        if(isEscape){
            tmp = data.getAndRemoveFirst();
            tmp = unpackEscape(tmp);
            if(tmp == null){
                throw new JxUnpackException("Invalid Escape character value.");
            }
            buffer[buffer_index++] = tmp.byteValue();
            isEscape = false;
        }
        while (data.size() > 0 && buffer_index < buffer.length) {
            tmp = data.getAndRemoveFirst();
            if (tmp.equals(this.Esc)) {
                if (data.size() <= 0) {
                    this.isEscape = true;
                    break;
                }
                tmp = data.getAndRemoveFirst();
                tmp = unpackEscape(tmp);
                if(tmp == null)return 1; // non-exist escape byte;
            }
            buffer[buffer_index++] = tmp.byteValue();
        }
        return 0;
    }

    // if error occur, return non-zero;
    private int allocBuffer(Iterator<Byte> iter) {
        int length = getByteCounts(iter);
        if(length < 0){
            return 1;
        }
        this.isEscape = false;
        buffer = new byte[length];
        buffer_index = 0;
        return 0;
    }

    //if error occur, return non-zero.
    private int reallocBuffer(Iterator<Byte> iter) throws JxUnpackException {
        byte[] temp = buffer;
        int count = getByteCounts(iter);
        if(count == -1){
            throw new JxUnpackException("Wrong frame structure.");
        }
        count += temp.length;
        if(count>MAX_FRAME_SIZE) {
            new JxUnpackException("Data length exceed Max frame size.");
        }
        buffer = new byte[count];
        for (int i = 0; i < temp.length; i++) {
            buffer[i] = temp[i];
        }
        return 0;
    }

    // if error occur, return -1;
    private int getByteCounts(Iterator<Byte> iter){
        int count = 0;
        while (iter.hasNext()) {
            Byte temp = iter.next();
            if (temp.equals(this.Start)) {
                return -1;
            } else if (temp.equals(this.End)) {
                break;
            } else if (!temp.equals(this.Esc)) ++count;
        }
        return count;
    }

    // Reset all states and intermediate variables for receiving of new frame .
    private void resetState() {
        buffer = null;
        isEscape = false;
    }

    private Byte unpackEscape(Byte data) {
        if (data == this.E_Esc) {
            return this.Esc;
        } else if (data == this.E_Start) {
            return this.Start;
        } else if (data == this.E_End) {
            return this.End;
        } else if (data == this.E_X1) {
            return this._X1;
        } else if (data == this.E_X2) {
            return this._X2;
        } else {
            return null;
        }
    }

    @Override
    public void discardFrame(LinkedList<Byte> data) {
        resetState();
    }
}
