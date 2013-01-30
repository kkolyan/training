package net.kkolyan.web.weedyweb.content;

import net.kkolyan.web.weedyweb.common.Polymorphic;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * @author nplekhanov
 */
public interface ContentHandler {
    boolean handleContent(InputStream data, String contentType, Map<String,Polymorphic> store) throws IOException;
}
