package net.kkolyan.web.http.server.networking;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author nplekhanov
 */
public class NioConnection implements Connection {
    private Object context;
    private SelectionKey selectionKey;
    private Queue<ByteBuffer> writeQueue = new ConcurrentLinkedQueue<ByteBuffer>();
    private ByteBuffer messageToWrite;
    private boolean pendingForClose;

    public NioConnection(SelectionKey selectionKey) {
        this.selectionKey = selectionKey;
    }

    @Override
    public void close() {
        pendingForClose = true;
    }

    @Override
    public void sendData(byte[] bytes, int offset, int length) throws IOException {
        ByteBuffer m = ByteBuffer.wrap(bytes, offset, length);
        writeQueue.offer(m);
        selectionKey.interestOps(selectionKey.interestOps() | SelectionKey.OP_WRITE);
    }

    @Override
    public Object getContext() {
        return context;
    }

    @Override
    public void setContext(Object context) {
        this.context = context;
    }

    public ByteBuffer getMessageToWrite() {
        while (true) {
            if (messageToWrite == null) {
                messageToWrite = writeQueue.poll();
            }
            if (messageToWrite == null || messageToWrite.remaining() > 0) {
                return messageToWrite;
            }
            messageToWrite = null;
        }
    }

    public boolean isPendingForClose() {
        return pendingForClose;
    }
}
