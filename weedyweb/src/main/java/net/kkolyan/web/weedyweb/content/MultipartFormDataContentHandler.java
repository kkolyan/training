package net.kkolyan.web.weedyweb.content;

import net.kkolyan.web.weedyweb.common.Polymorphic;

import java.io.InputStream;
import java.util.Map;

/**
 * @author nplekhanov
 */
public class MultipartFormDataContentHandler implements ContentHandler {

    @Override
    public boolean handleContent(InputStream data, String contentType, Map<String, Polymorphic> store) {
        if (!"multipart/form-data".equals(contentType)) {
            return false;
        }
        throw new UnsupportedOperationException("not implemented");
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
