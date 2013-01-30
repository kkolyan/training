package net.kkolyan.web.http.server;

import net.kkolyan.web.http.api.HttpRequest;

import java.io.InputStream;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author nplekhanov
 */
public class EditableHttpRequest implements HttpRequest {
    private String method;
    private String path;
    private String query;
    private Map<String,String> headers = new CaseInsensitiveMap<String>(new TreeMap<String, String>());
    private InputStream content;

    @Override
    public String getMethod() {
        return method;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public String getQuery() {
        return query;
    }

    @Override
    public Map<String, String> getHeaders() {
        return headers;
    }

    @Override
    public InputStream getContent() {
        return content;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public void setContent(InputStream content) {
        this.content = content;
    }
}
