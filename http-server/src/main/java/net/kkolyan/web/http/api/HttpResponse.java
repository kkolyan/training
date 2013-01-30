package net.kkolyan.web.http.api;

import java.io.OutputStream;
import java.util.Map;

/**
 * @author nplekhanov
 */
public interface HttpResponse {
    void setStatus(HttpStatus status);
    Map<String, String> getHeaders();
    OutputStream getContent();
}
