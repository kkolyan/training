package net.kkolyan.web.http.api;

import java.util.Map;
import java.util.TreeMap;

/**
 * @author nplekhanov
 */
public final class HttpStatusException extends Exception {
    private HttpStatus status;
    private Map<String,String> headers = new TreeMap<String, String>();

    public HttpStatusException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatusException(Throwable cause, HttpStatus status) {
        super(cause);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
    
    public HttpStatusException addHeader(String name, String value) {
        headers.put(name, value);
        return this;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    @Override
    public String toString() {
        return "{" + status + ", " + headers + "} " + super.toString();
    }
}
