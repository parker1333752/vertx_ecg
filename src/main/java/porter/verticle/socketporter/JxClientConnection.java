package porter.verticle.socketporter;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetSocket;

/**
 * Note: If keyword[0] == 0, it's a monitor client, or it's a sensor client.
 *
 * Created by parker on 2015/10/14.
 */
public class JxClientConnection {
    public static final String WaitClient = "WaitClient";
    public static final String SensorClient = "SensorClient";
    public static final String MonitorClient = "MonitorClient";
    private static final int MaxKeywordLength = 8;

    private byte[] keyword;
    private String clientType;
    private int currentKeywordLength;

    public void setSocket(NetSocket socket) {
        this.socket = socket;
    }

    private NetSocket socket;

    public JxClientConnection() {
        keyword = new byte[MaxKeywordLength];
        currentKeywordLength = 0;
        clientType = WaitClient;
    }

    public byte[] dataProcess(Buffer buffer) {
        byte[] data = buffer.getBytes();
        int i = 0;
        if (currentKeywordLength < MaxKeywordLength) {
            while (i < data.length && currentKeywordLength < MaxKeywordLength) {
                keyword[currentKeywordLength++] = data[i++];
            }
            if (currentKeywordLength >= MaxKeywordLength) {
                if (keyword[0] != 0) {
                    clientType = SensorClient;
                    System.out.println("here is a " + SensorClient);
                } else {
                    clientType = MonitorClient;
                    System.out.println("here is a " + MonitorClient);
                }
            }
        }
        if (i < data.length) {
            byte[] rt = new byte[data.length - i];
            int j = 0;
            while (i < data.length) rt[j++] = data[i++];
            System.out.println("receive data: " + new String(rt));
            dataRecord(rt);
            return rt;
        }
        return new byte[]{};
    }

    private void dataRecord(byte[] rt) {
        // Data record, for example store data into mongo.
    }

    public String getClientType() {
        return clientType;
    }

    public byte[] getKeyword() {
        return keyword;
    }

    public boolean matchedKeyword(byte[] keyword) {
        int i = 0;
        while ((++i) < MaxKeywordLength) {
            if (keyword[i] != this.keyword[i]) return false;
        }
        return true;
    }

    public void sendData(byte[] res) {
        this.socket.write(Buffer.buffer(res));
    }
}
