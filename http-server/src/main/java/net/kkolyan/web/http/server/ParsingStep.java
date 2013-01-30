package net.kkolyan.web.http.server;


import net.kkolyan.web.http.api.HttpStatusException;

/**
* @author nplekhanov
*/
public interface ParsingStep {

    int addData(byte[] bytes, int offset, int length, ParsingContext context) throws HttpStatusException;
}
