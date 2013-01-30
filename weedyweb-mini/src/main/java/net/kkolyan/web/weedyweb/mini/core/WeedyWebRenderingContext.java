package net.kkolyan.web.weedyweb.mini.core;

import net.kkolyan.web.http.api.HttpRequest;
import net.kkolyan.web.http.api.HttpResponse;
import net.kkolyan.web.http.api.HttpStatus;
import net.kkolyan.web.weedyweb.mini.core.view.RenderingContext;

import java.io.OutputStream;

/**
* @author nplekhanov
*/
public class WeedyWebRenderingContext implements RenderingContext {

    private HttpRequest request;
    private HttpResponse response;

    public WeedyWebRenderingContext(HttpRequest request, HttpResponse response) {
        this.request = request;
        this.response = response;
    }

    @Override
    public String getRequestHeader(String name) {
        return request.getHeaders().get(name);
    }

    @Override
    public void setResponseHeader(String name, String value) {
        response.getHeaders().put(name, value);
    }

    @Override
    public OutputStream getContent() {
        return response.getContent();
    }

    @Override
    public void setStatusCode(int code) {
        for (HttpStatus status : HttpStatus.values()) {
            if (status.getCode() == code) {
                response.setStatus(status);
            }
        }
    }
}
