package porter.verticle.socketporter;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.NetServer;
import io.vertx.core.net.NetSocket;

import java.util.HashMap;
import java.util.Map;

/**
 * Run as socket server, deployed vertx instance, used to transfer data from sensor clients and
 * monitor client.
 */
public class JxSocketPorterVerticle extends AbstractVerticle {

    private JsonObject _config;
    private NetServer _server;
    /**
     * Store client's socket instance connections and.
     */
    private Map<NetSocket, JxClientConnection> _sockets;
    /**
     * Called when verticle deployed.
     */
    @Override
    public void start() {
        // Configuration.
        _config = config();

        // create new net server, serve as socket server.
        _server = vertx.createNetServer();

        _sockets = new HashMap<NetSocket, JxClientConnection>();

        // Setup Server.
        setConnectHandler();

        // Start listenning.
        _server.listen(Integer.parseInt(_config.getString("port")), _config.getString("host"));
    }

    private void setConnectHandler() {
        // Run when client connecting;
        _server.connectHandler(socket -> {
            // New socket Connecting.
            System.out.println("receive something." + Thread.currentThread().getId());

            // Receive data handler.
            socket.handler(buffer -> {
                System.out.println(":" + buffer.toString());
                JxClientConnection client;
                if((client = _sockets.get(socket)) == null){
                    client = new JxClientConnection();
                    _sockets.put(socket, client);
                    client.setSocket(socket);
                }
                byte[] res = client.dataProcess(buffer);
                if(res.length > 0 ) {
                    if (client.getClientType() == JxClientConnection.SensorClient) {
                        for (JxClientConnection dst_client : _sockets.values()) {
                            if (dst_client.getClientType() == JxClientConnection.MonitorClient &&
                                    dst_client.matchedKeyword(client.getKeyword())) {
                                dst_client.sendData(res);
                            }
                        }
                    }
                }
            });
        });
    }
}
