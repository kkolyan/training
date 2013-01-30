package net.kkolyan.web.http.server;

import net.kkolyan.web.http.api.HttpResponse;
import net.kkolyan.web.http.api.HttpStatus;

import java.io.ByteArrayOutputStream;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author nplekhanov
 */
public class ReadableHttpResponse implements HttpResponse {

    private HttpStatus status = HttpStatus.OK;
    private Map<String,String> headers = new CaseInsensitiveMap<String>(new TreeMap<String, String>());
    private ByteArrayOutputStream content = new ByteArrayOutputStream();

    @Override
    public void setStatus(HttpStatus responseStatus) {
        this.status = responseStatus;
    }

    @Override
    public Map<String, String> getHeaders() {
        return headers;
    }

    @Override
    public ByteArrayOutputStream getContent() {
        return content;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
