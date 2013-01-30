package net.kkolyan.web.http.api;

/**
 * @author nplekhanov
 */
public interface HttpGateway {
    boolean doServe(HttpRequest request, HttpResponse response) throws HttpStatusException;
}
