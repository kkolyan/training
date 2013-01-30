package net.kkolyan.web.weedyweb.mini.core.view;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author nplekhanov
 */
public interface RenderingContext {
    String getRequestHeader(String name);
    void setResponseHeader(String name, String value);
    OutputStream getContent() throws IOException;
    void setStatusCode(int code);
}
