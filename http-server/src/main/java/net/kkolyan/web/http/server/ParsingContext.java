package net.kkolyan.web.http.server;

/**
 * @author nplekhanov
 */
public interface ParsingContext {
    EditableHttpRequest getRequest();
    void setParsingStep(ParsingStep step);
    void setRequestParsed(boolean finished);
}
