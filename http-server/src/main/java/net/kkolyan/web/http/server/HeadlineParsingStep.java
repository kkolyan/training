package net.kkolyan.web.http.server;


import net.kkolyan.web.http.api.HttpStatus;
import net.kkolyan.web.http.api.HttpStatusException;

import java.util.Arrays;

/**
* @author nplekhanov
*/
public class HeadlineParsingStep extends LineBasedParsingStep {

    @Override
    protected void addLine(String line, ParsingContext context) throws HttpStatusException {
        line = line.trim();
        String[] parts = line.split(" ", 3);
        if (parts.length != 3) {
            throw new HttpStatusException("invalid first line: "+ Arrays.toString(parts), HttpStatus.BAD_REQUEST);
        }
        EditableHttpRequest request = context.getRequest();

        request.setMethod(parts[0]);
        String uri = parts[1];

        String[] splitUri = uri.split("\\?", 2);
        if (splitUri.length > 1) {
            request.setPath(splitUri[0]);
            request.setQuery(splitUri[1]);
        } else {
            request.setQuery("");
            request.setPath(uri);
        }
        if (!parts[2].equals("HTTP/1.1")) {
            throw new HttpStatusException("invalid protocol: "+parts[2], HttpStatus.HTTP_VERSION_NOT_SUPPORTED);
        }
        context.setParsingStep(new HeadersParsingStep());
    }
}
