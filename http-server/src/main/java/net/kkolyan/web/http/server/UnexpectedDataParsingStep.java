package net.kkolyan.web.http.server;


import net.kkolyan.web.http.api.HttpStatusException;

/**
* @author nplekhanov
*/
public class UnexpectedDataParsingStep implements ParsingStep {
    @Override
    public int addData(byte[] bytes, int offset, int length, ParsingContext context) throws HttpStatusException {
        if (length == 0) {
            return 0;
        }
        throw new IllegalStateException("unexpected data");
    }
}
