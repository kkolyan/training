package net.kkolyan.web.http.server.networking;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author nplekhanov
 */
public class BlockingConnectionAcceptor extends Thread  {
    private ServerSocket serverSocket;
    private int port;

    private ConnectionHandler connectionHandler;

    private ExecutorService executor = Executors.newCachedThreadPool();

    @PostConstruct
    public void bind() throws IOException {
        serverSocket = new ServerSocket(port);
        start();
    }

    @Override
    public void run() {
        while (!serverSocket.isClosed()) {
            try {
                final Socket socket = serverSocket.accept();

                executor.execute(new Runnable() {

                    @Override
                    public void run() {
                        Connection connection = new BlockingConnection(socket);
                        try {
                            connectionHandler.initConnection(connection);

                            InputStream in = socket.getInputStream();
                            byte[] bytes = new byte[Math.max(in.available(), 1024)];
                            int n;
                            while (!socket.isClosed() && (n = in.read(bytes)) >= 0) {
                                connectionHandler.handleData(bytes, 0, n, connection);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            connection.close();
                        } finally {
                            connection.close();
                        }
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @PreDestroy
    public void unbind() throws IOException {
        serverSocket.close();
        executor.shutdown();
    }

    public void setConnectionHandler(ConnectionHandler connectionHandler) {
        this.connectionHandler = connectionHandler;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return "BlockingConnectionAcceptor{" +
                "port=" + port +
                '}';
    }
}
