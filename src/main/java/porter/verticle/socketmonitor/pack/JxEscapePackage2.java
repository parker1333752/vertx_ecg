package porter.verticle.socketmonitor.pack;

import java.util.LinkedList;

/**
 * Created by parker on 2016/1/6.
 */
public class JxEscapePackage2 implements JiPackager {
    final static byte Esc = (byte) 0xfa;
    final static byte Start = (byte) 0xfb;
    final static byte End = (byte) 0xfc;

    final static byte E_Esc = (byte) 0x01;
    final static byte E_Start = (byte) 0x02;
    final static byte E_End = (byte) 0x03;
    final static int MAX_FRAME_SIZE = 0x8000;

    private LinkedList<Byte> buffer;
    private boolean isEscape;
    private LinkedList<Byte> last_data;

    @Override
    public byte[] pack(byte[] data) throws JxPackException {
        LinkedList<Byte> rt = new LinkedList<>();
        rt.add(JxEscapePackage2.Start);
        for (int i = 0; i < data.length; i++) {
            switch (data[i]){
                case JxEscapePackage2.Start:
                    rt.add(JxEscapePackage2.Esc);
                    rt.add(JxEscapePackage2.E_Start);
                    break;
                case JxEscapePackage2.End:
                    rt.add(JxEscapePackage2.Esc);
                    rt.add(JxEscapePackage2.E_End);
                    break;
                case JxEscapePackage2.Esc:
                    rt.add(JxEscapePackage2.Esc);
                    rt.add(JxEscapePackage2.E_Esc);
                    break;
                default:
                    rt.add(data[i]);
                    break;
            }
        }
        rt.add(JxEscapePackage2.End);
        return toByteArray(rt);
    }

    @Override
    public byte[] unpack(LinkedList<Byte> dataArray) throws JxUnpackException {
        if (last_data != null) {
            for (int i = 0; i < dataArray.size(); i++) {
                last_data.add(dataArray.removeFirst());
            }
            dataArray = last_data;
            last_data = null;
        }
        LinkedList<Byte> rt = null;
        if (dataArray == null) return null;
        while (dataArray.size() > 0) {
            byte data = dataArray.removeFirst();
            if (data == JxEscapePackage2.Start) {
                buffer = new LinkedList<>();
                isEscape = false;
            } else if (data == JxEscapePackage2.End) {
                if (buffer != null) {
                    rt = buffer;
                    buffer = null;
                    last_data = dataArray;
                    break;
                }
            } else if (buffer != null) {
                if (isEscape) {
                    switch (data) {
                        case JxEscapePackage2.E_End:
                            buffer.add(JxEscapePackage2.End);
                            break;
                        case JxEscapePackage2.E_Esc:
                            buffer.add(JxEscapePackage2.Esc);
                            break;
                        case JxEscapePackage2.E_Start:
                            buffer.add(JxEscapePackage2.Start);
                            break;
                        default:
                            break;
                    }
                    isEscape = false;
                } else {
                    if (data == JxEscapePackage2.Esc) isEscape = true;
                    else buffer.add(data);
                }
            } else {
            }
            if (buffer != null && buffer.size() >= MAX_FRAME_SIZE)
                throw new JxUnpackException("Buffer length is too long.");
        }
        return toByteArray(rt);
    }

    private byte[] toByteArray(LinkedList<Byte> arrays) {
        if (arrays == null) return null;
        byte[] rt = new byte[arrays.size()];
        int index = 0;
        while (!arrays.isEmpty()) {
            rt[index++] = arrays.removeFirst();
        }
        return rt;
    }

    @Override
    public void discardFrame(LinkedList<Byte> data) {
        buffer = null;
        isEscape = false;
    }
}
