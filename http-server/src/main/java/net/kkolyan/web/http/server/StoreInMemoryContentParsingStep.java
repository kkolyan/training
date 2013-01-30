package net.kkolyan.web.http.server;


import net.kkolyan.web.http.api.HttpStatus;
import net.kkolyan.web.http.api.HttpStatusException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
* @author nplekhanov
*/
public class StoreInMemoryContentParsingStep implements ParsingStep {
    private int remaining;
    private ByteArrayOutputStream buf = new ByteArrayOutputStream();

    public StoreInMemoryContentParsingStep(ParsingContext context) throws HttpStatusException {
        String contentLength = context.getRequest().getHeaders().get("Content-Length");
        if (contentLength == null) {
            throw new HttpStatusException("content length expected", HttpStatus.BAD_REQUEST);
        }
        remaining = Integer.parseInt(contentLength);
    }

    @Override
    public int addData(byte[] bytes, int offset, int length, ParsingContext context) {
        int toRead = Math.min(remaining, length);
        buf.write(bytes, offset, toRead);
        remaining -= toRead;

        if (remaining < 0) {
            throw new IllegalStateException();
        }
        if (remaining == 0) {
            context.getRequest().setContent(new ByteArrayInputStream(buf.toByteArray()));
            context.setParsingStep(new UnexpectedDataParsingStep());
            context.setRequestParsed(true);
        }
        return toRead;
    }
}
