package net.kkolyan.web.http.server;


import net.kkolyan.web.http.api.HttpStatus;
import net.kkolyan.web.http.api.HttpStatusException;

/**
* @author nplekhanov
*/
public class HeadersParsingStep extends LineBasedParsingStep {

    @Override
    protected void addLine(String line, ParsingContext context) throws HttpStatusException {
        line = line.trim();

        EditableHttpRequest request = context.getRequest();

        if (line.isEmpty()) {
            if (request.getMethod().equals("POST")) {
                String contentType = request.getHeaders().get("Content-Type");
                if (contentType == null) {
                    throw new HttpStatusException("content type is required for POST", HttpStatus.BAD_REQUEST);
                }
                context.setParsingStep(new StoreInMemoryContentParsingStep(context));
            } else {
                context.setParsingStep(new UnexpectedDataParsingStep());
                context.setRequestParsed(true);
            }
            return;
        }

        String[] kv = line.split(":", 2);
        if (kv.length != 2) {
            throw new HttpStatusException("invalid header: \""+line+"\"", HttpStatus.BAD_REQUEST);
        }
        request.getHeaders().put(kv[0].trim(), kv[1].trim());
    }
}
