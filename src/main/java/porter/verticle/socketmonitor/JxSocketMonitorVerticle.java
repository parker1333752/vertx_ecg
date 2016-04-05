package porter.verticle.socketmonitor;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetServer;
import io.vertx.core.net.NetSocket;
import porter.util.xmljson.JxXmlJsonReader;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by parker on 2015/10/20.
 */
public class JxSocketMonitorVerticle extends AbstractVerticle {
    private JxXmlJsonReader _config;
    private NetServer _server;
    static final String PASSWORD = "password";
    private Map<NetSocket, JxSensorDataProcessor> _dataProcessors;

    @Override
    public void start() {
        // Load configuration.
        _config = new JxXmlJsonReader(this.config());

        _server = vertx.createNetServer();

        _dataProcessors = new HashMap<>();
        setConnectHandler();
        _server.listen(_config.getInteger("port"), _config.getString("host"));
    }

    public void setConnectHandler() {
        _server.connectHandler(socket -> {
            // New socket connecting.
            System.out.println("Hello client");
            _dataProcessors.put(socket, new JxSensorDataProcessor());

            socket.write(Buffer.buffer(PASSWORD));

            // Receive data handle
            socket.handler(buffer -> {
                JxSensorDataProcessor processor = _dataProcessors.get(socket);
                processor.runProcess(buffer.getBytes());
            });

            // Socket closed.
            socket.closeHandler(v ->{
                _dataProcessors.get(socket).dispose();
                _dataProcessors.remove(socket);
                System.out.println("Goodbye client");
            });
        });
    }
}
