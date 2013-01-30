package net.kkolyan.web.http.server.networking;

import java.io.IOException;

/**
 * @author nplekhanov
 */
public interface ConnectionHandler {
    void initConnection(Connection connection);
    void handleData(byte[] bytes, int offset, int length, Connection connection) throws IOException;
    void destroyConnection(Connection connection);
}
