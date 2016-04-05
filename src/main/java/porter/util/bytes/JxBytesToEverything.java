package porter.util.bytes;

/**
 * Created by parker on 2015/12/7.
 */
public class JxBytesToEverything {

    /**
     * Read 2 bytes from data, and convert to int16.
     * @param data
     * @param pos
     * @return
     */
    public static short toShort(byte[] data, int pos){
        short rt = (short)(0xff & data[pos + 1]); rt <<= 8;
        rt |= (short)(0xff & data[pos + 0]);
        return rt;
    }

    /**
     * Read 4 bytes from data, and convert to int32.
     * @param data
     * @param pos
     * @return
     */
    public static int toInteger(byte[] data, int pos){
        int rt = (0xff & data[pos + 3]); rt <<= 8;
        rt |= (0xff & data[pos + 2]); rt <<= 8;
        rt |= (0xff & data[pos + 1]); rt <<= 8;
        rt |= (0xff & data[pos + 0]);
        return rt;
    }

    /**
     * Read 8 bytes from data, and convert to double.
     * @param data
     * @param start_pos
     * @return
     */
    public static double toDouble(byte[] data, int start_pos){
        long t = (0xff & data[start_pos + 7]); t <<= 8;
        t += (0xff & data[start_pos + 6]); t <<= 8;
        t += (0xff & data[start_pos + 5]); t <<= 8;
        t += (0xff & data[start_pos + 4]); t <<= 8;
        t += (0xff & data[start_pos + 3]); t <<= 8;
        t += (0xff & data[start_pos + 2]); t <<= 8;
        t += (0xff & data[start_pos + 1]); t <<= 8;
        t += (0xff & data[start_pos + 0]);
        return Double.longBitsToDouble(t);
    }
}
