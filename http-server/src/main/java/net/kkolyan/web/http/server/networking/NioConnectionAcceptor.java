package net.kkolyan.web.http.server.networking;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author nplekhanov
 */
public class NioConnectionAcceptor extends Thread {
    private ConnectionHandler connectionHandler;
    private Selector selector;
    private ByteBuffer readBuffer = ByteBuffer.allocate(64*1024);

    private List<Integer> ports = new ArrayList<Integer>();

    public void setConnectionHandler(ConnectionHandler connectionHandler) {
        this.connectionHandler = connectionHandler;
    }

    @PostConstruct
    public void bind() throws IOException {
        selector = Selector.open();

        for (int port: ports) {
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.socket().bind(new InetSocketAddress(port));
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        }

        start();
    }

    @Override
    public void run() {

        while (selector.isOpen()) {
            try {
                selector.select();

                Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
                while (keys.hasNext()) {
                    SelectionKey key = keys.next();
                    keys.remove();

                    if (key.isValid() && key.isAcceptable()) {
                        ServerSocketChannel server = (ServerSocketChannel) key.channel();
                        SocketChannel client = server.accept();
                        client.configureBlocking(false);
                        SelectionKey clientKey = client.register(selector, SelectionKey.OP_READ);

                        Connection connection = new NioConnection(clientKey);
                        clientKey.attach(connection);

                        connectionHandler.initConnection(connection);
                    }

                    try {
                        if (key.isValid() && key.isReadable()) {
                            ReadableByteChannel in = (ReadableByteChannel) key.channel();
                            readBuffer.clear();
                            int read = in.read(readBuffer);
                            if (read < 0) {
                                closeConnection(key);
                            } else {
                                NioConnection connection = (NioConnection) key.attachment();

                                if (connection == null) {
                                    throw new IllegalStateException();
                                }

                                byte[] array = readBuffer.array();
                                connectionHandler.handleData(array, 0, read, connection);
                            }
                        }

                        if (key.isValid() && key.isWritable()) {
                            NioConnection connection = (NioConnection) key.attachment();
                            WritableByteChannel out = (WritableByteChannel) key.channel();

                            while (true) {
                                ByteBuffer task = connection.getMessageToWrite();
                                if (task == null) {
                                    if (connection.isPendingForClose()) {
                                        closeConnection(key);
                                    } else {
                                        key.interestOps(key.interestOps() & ~SelectionKey.OP_WRITE);
                                    }
                                    break;
                                }
                                int toWrite = task.remaining();
                                int written = out.write(task);
                                if (written < toWrite) {
                                    //socket buffer is full
                                    break;
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        closeConnection(key);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void closeConnection(SelectionKey selectionKey) throws IOException {
        selectionKey.cancel();
        selectionKey.channel().close();
        Connection connection = (Connection) selectionKey.attach(null);
        if (connection != null) {
            connectionHandler.destroyConnection(connection);
        }
    }

    @PreDestroy
    public void unbind() throws IOException {
        selector.close();
    }

    public void setPorts(List<Integer> ports) {
        this.ports = ports;
    }

    @Override
    public String toString() {
        return "NioConnectionAcceptor{" +
                "ports=" + ports +
                '}';
    }
}
