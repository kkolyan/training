package net.kkolyan.web.http.server;


import net.kkolyan.web.http.api.HttpStatusException;

import java.io.ByteArrayOutputStream;

/**
* @author nplekhanov
*/
public abstract class LineBasedParsingStep implements ParsingStep {
    private ByteArrayOutputStream buf = new ByteArrayOutputStream();

    @Override
    public int addData(byte[] bytes, int offset, int length, ParsingContext context) throws HttpStatusException {
        for (int i = offset; i < offset + length; i ++) {
            int lineBreak = getLineBreak(bytes, i);
            if (lineBreak > 0) {
                buf.write(bytes, offset, i - offset);
                addLine(buf.toString(), context);
                buf.reset();
                return i - offset + lineBreak;
            }
        }
        buf.write(bytes, offset, length);
        return length;
    }

    private int getLineBreak(byte[] bytes, int pos) {
        if (bytes[pos] == '\r') {
            if (bytes.length > pos + 1 && bytes[pos + 1] == '\n') {
                return 2;
            }
            return 1;
        }
        if (bytes[pos] == '\n') {
            return 1;
        }
        return 0;
    }

    protected abstract void addLine(String line, ParsingContext context) throws HttpStatusException;
}
