package net.kkolyan.web.http.server.networking;

import java.io.IOException;

/**
 * @author nplekhanov
 */
public interface Connection {
    void close();
    void sendData(byte[] bytes, int offset, int length) throws IOException;
    Object getContext();
    void setContext(Object context);
}
