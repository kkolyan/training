package net.kkolyan.web.weedyweb.mini.core;

import net.kkolyan.web.http.api.HttpRequest;
import net.kkolyan.web.http.api.HttpStatusException;
import net.kkolyan.web.http.api.HttpResponse;

/**
* @author nplekhanov
*/
public interface RequestHandler {
    boolean handle(HttpRequest request, HttpResponse response) throws HttpStatusException;
}
