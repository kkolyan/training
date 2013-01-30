package net.kkolyan.web.http.api;

import java.io.InputStream;
import java.util.Map;

/**
 * @author nplekhanov
 */
public interface HttpRequest {
    String getMethod();
    String getPath();
    String getQuery();
    Map<String, String> getHeaders();
    InputStream getContent();
}
