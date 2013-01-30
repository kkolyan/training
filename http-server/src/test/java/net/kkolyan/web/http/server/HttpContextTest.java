package net.kkolyan.web.http.server;

import net.kkolyan.web.http.server.HttpContext;
import net.kkolyan.web.http.api.HttpRequest;
import net.kkolyan.web.http.api.HttpStatusException;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;

/**
 * @author nplekhanov
 */
public class HttpContextTest {

    @Test
    public void testPost() throws IOException, HttpStatusException {
        test("\r\n");
        test("\n");
        test("\r");
    }

    private void test(String lineBreak) throws IOException, HttpStatusException {
        String s =
                "POST / HTTP/1.1\n" +
                "Host: localhost:8080\n" +
                "Connection: keep-alive\n" +
                "Content-Length: 9\n" +
                "Cache-Control: max-age=0\n" +
                "Origin: null\n" +
                "User-Agent: Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.11 (KHTML, like Gecko) Chrome/17.0.963.83 Safari/535.11\n" +
                "Content-Type: application/x-www-form-urlencoded\n" +
                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\n" +
                "Accept-Encoding: gzip,deflate,sdch\n" +
                "Accept-Language: ru-RU,ru;q=0.8,en-US;q=0.6,en;q=0.4\n" +
                "Accept-Charset: windows-1251,utf-8;q=0.7,*;q=0.3\n" +
                "\n" +
                "x=27&y=15";
        s = s.replace("\n", lineBreak);

        InputStream source = new ByteArrayInputStream(s.getBytes("utf8")) {

            @Override
            public int read(byte[] b, int off, int len) {
                int n = super.read(b, off, len);
                if (n < 0) {
                    return 0; //emulate socket behavior
                }
                return n;
            }
        };
        HttpContext context = new HttpContext();

        byte[] bytes = s.getBytes("utf8");
        context.parseData(bytes, 0, bytes.length);

        assertEquals(true, context.isRequestParsed());

        HttpRequest request = context.getRequest();

        assertEquals("POST", request.getMethod());
        assertEquals("/", request.getPath());
        assertEquals("", request.getQuery());
        assertEquals(11, request.getHeaders().size());
        assertEquals(9, request.getContent().available());

        assertEquals("x=27&y=15", readStream(request.getContent()));
    }

    @Test
    public void test2() throws IOException, HttpStatusException {
        String s = "GET / HTTP/1.1\n\n";
        byte[] bytes = s.getBytes("utf8");
        HttpContext context = new HttpContext();
        context.parseData(bytes, 0, bytes.length);
        assertEquals(true, context.isRequestParsed());
    }

    private String readStream(InputStream stream) throws IOException {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        byte[] bytes = new byte[64];
        int n;
        while ((n = stream.read(bytes)) >= 0) {
            buf.write(bytes, 0, n);
        }
        return buf.toString();
    }
}
