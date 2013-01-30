package net.kkolyan.web.http.server.networking;

import java.io.IOException;
import java.net.Socket;

/**
 * @author nplekhanov
 */
public class BlockingConnection implements Connection {
    private Socket socket;
    private Object context;

    public BlockingConnection(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void close() {
        try {
            socket.close();
        } catch (IOException ignored) {
        }
    }

    @Override
    public void sendData(byte[] bytes, int offset, int length) throws IOException {
        socket.getOutputStream().write(bytes, offset, length);
        socket.getOutputStream().flush();
    }

    @Override
    public Object getContext() {
        return context;
    }

    @Override
    public void setContext(Object context) {
        this.context = context;
    }
}
