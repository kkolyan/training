package net.kkolyan.web.weedyweb.content;

import net.kkolyan.web.weedyweb.common.Polymorphic;
import net.kkolyan.web.weedyweb.common.PolymorphicText;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

/**
 * @author nplekhanov
 */
public class XWwwFormDataContentHandler implements ContentHandler {

    @Override
    public boolean handleContent(InputStream data, String contentType, Map<String, Polymorphic> store) throws IOException {
        if (!"application/x-www-form-urlencoded".equals(contentType)) {
            return false;
        }
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        byte[] bb = new byte[1024];
        while (true) {
            int n = data.read(bb);
            if (n < 0) {
                break;
            }
            buf.write(bb, 0, n);
        }
        String p = buf.toString("utf8");
        p = URLDecoder.decode(p, "utf8");
        parseParams(p, store);
        return true;
    }

    private void parseParams(String q, Map<String,Polymorphic> store) throws UnsupportedEncodingException {
        if (q.startsWith("?")) {
            q = q.substring(1);
        }
        String[] entries = q.split("&");
        for (String entry: entries) {
            String[] kv = entry.split("=");
            if (kv.length == 2) {
                String k = kv[0].trim();
                String v = kv[1].trim();
                v = URLDecoder.decode(v, "utf8");
                store.put(k, new PolymorphicText(v));
            } else if (kv.length == 1) {
                store.put(kv[0].trim(), new PolymorphicText(""));
            } else {
                throw new IllegalStateException("wrong request");
            }
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
