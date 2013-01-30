package net.kkolyan.web.http.server;


import net.kkolyan.web.http.api.HttpStatusException;

/**
 * @author nplekhanov
 */
public class HttpContext implements ParsingContext {
    private ParsingStep parsingStep = new HeadlineParsingStep();
    private EditableHttpRequest request = new EditableHttpRequest();
    private boolean requestParsed;

    @Override
    public EditableHttpRequest getRequest() {
        return request;
    }

    @Override
    public void setParsingStep(ParsingStep step) {
        this.parsingStep = step;
    }

    @Override
    public void setRequestParsed(boolean requestParsed) {
        this.requestParsed = requestParsed;
    }

    public boolean isRequestParsed() {
        return requestParsed;
    }

    public void parseData(byte[] bytes, int offset, int length) throws HttpStatusException {
        int read = 0;
        while (read < length) {
            read += parsingStep.addData(bytes, offset + read, length - read, this);
        }
    }
}
