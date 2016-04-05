package porter.verticle.socketmonitor;

import porter.model.JxEcgData;
import porter.util.bytes.JxBytesToEverything;
import porter.verticle.socketmonitor.pack.JiPackager;
import porter.verticle.socketmonitor.pack.JxEscapePackage2;
import porter.verticle.socketmonitor.pack.JxUnpackException;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;

/**
 * Created by parker on 2015/10/20.
 */
public class JxSensorDataProcessor {

    private LinkedList<Byte> _buffer;
    private JiPackager _packager;
    private FileOutputStream _fileAdcOut;
    private String fid;
    private int counts1;
    private int counts2;
    private int counts3;

    public JxSensorDataProcessor() {
        _buffer = new LinkedList<>();
        _packager = new JxEscapePackage2();
        fid = String.valueOf(System.currentTimeMillis());
        counts1 = 0;
        counts2 = 0;
        counts3 = 0;
        try {
            _fileAdcOut = new FileOutputStream("tmp2/" + fid + ".adc.dat", false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int runProcess(byte[] data) {
        int i;
        for (i = 0; i < data.length; ++i) {
            _buffer.offer(data[i]);
        }
        while (doFrameProcess() != 0) ;
        return 0;
    }

    // Return 0 means there is no more new frames.
    // Return 0xff means one frame process complete.
    // Return other value means error occured.
    private int doFrameProcess() {
        byte[] data;
        try {
            data = _packager.unpack(_buffer);
        } catch (JxUnpackException e) {
            e.printStackTrace();
            _packager.discardFrame(_buffer);
            return 1;
        }
        if (data == null)// no more new frame;
        {
            return 0;
        }

//        System.out.println(String.format("%8d,%8d,%8d", counts1, counts2, counts3));
        counts1++;

        short length;
        length = (short) (0xff & data[0]);
        if ((data.length - 2) < length) return 3; // length not match.
        counts2++;

        byte sum_check = 0;
        for (int i = 1; i < data.length - 1; i++) {
            sum_check += data[i];
        }
        if (sum_check != data[data.length - 1]) return 4; // sum check failed;
        counts3++;

        printOut(data, 1);
        try {
            if (data.length < 23) return 5; // data length not enough
            _fileAdcOut.write(data, 1, (4 * 2 + 6 * 2 + 2) * 10);
        } catch (IOException e) {
            e.printStackTrace();
            return 6; // IO error.
        }
        return 0xff; // Normal frame complete.
    }

    private void printOut(byte[] data, int pos) {
        JxEcgData record = doMpuDataParse(data, pos);

        System.out.println(String.format("[%8d,%8d,%8d,%8d,%8d,%8d,%12f,%12f,%8d]", record.getAccx(),
                record.getAccy(), record.getAccz(), record.getOmegax(), record.getOmegay(),
                record.getOmegaz(), record.getEcg() / (double) 0x10000000, record.getHs() / (double) 0x10000000, record.getDate()));
//        counts+=1;
//        if(!record_valid(record))counts1+=1;
//        double g1 = record.getAccx() / 32768.0 * 8;
//        double g2 = record.getAccy() / 32768.0 * 8;
//        double g3 = record.getAccz() / 32768.0 * 8;
//        double g = Math.sqrt(g1*g1+g2*g2+g3*g3);
//        double g = record.getOmegax() + record.getOmegay() + record.getOmegaz();
//        System.out.println(String.format("[%10f,%8d] [%8d/%8d]", g, record.getDate(), counts1, counts));
    }

    private boolean record_valid(JxEcgData record) {
        double g1 = record.getAccx() / 32768.0 * 8;
        double g2 = record.getAccy() / 32768.0 * 8;
        double g3 = record.getAccz() / 32768.0 * 8;
        double g = Math.sqrt(g1 * g1 + g2 * g2 + g3 * g3);
        return (g < 1.2 && g > 0.9);
//        return (record.getAccx() == 104);
    }

    private JxEcgData doMpuDataParse(byte[] data, int pos) {
        JxEcgData record = new JxEcgData();
        record.setEcg(JxBytesToEverything.toInteger(data, pos));
        pos += 4;
        record.setHs(JxBytesToEverything.toInteger(data, pos));
        pos += 4;
        record.setAccx(JxBytesToEverything.toShort(data, pos));
        pos += 2;
        record.setAccy(JxBytesToEverything.toShort(data, pos));
        pos += 2;
        record.setAccz(JxBytesToEverything.toShort(data, pos));
        pos += 2;
        record.setOmegax(JxBytesToEverything.toShort(data, pos));
        pos += 2;
        record.setOmegay(JxBytesToEverything.toShort(data, pos));
        pos += 2;
        record.setOmegaz(JxBytesToEverything.toShort(data, pos));
        pos += 2;
        record.setDate(JxBytesToEverything.toShort(data, pos)); //pos+=2;
        return record;
    }

    public void dispose() {
        try {
            _fileAdcOut.flush();
            _fileAdcOut.close();
            System.out.println("File " + fid + " Closed.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
