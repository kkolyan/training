package net.kkolyan.web.http.server;

import net.kkolyan.web.http.api.HttpRequest;
import net.kkolyan.web.http.api.HttpGateway;
import net.kkolyan.web.http.api.HttpStatus;
import net.kkolyan.web.http.api.HttpStatusException;
import net.kkolyan.web.http.server.networking.Connection;
import net.kkolyan.web.http.server.networking.ConnectionHandler;

import java.io.*;
import java.util.Map;

/**
 * @author nplekhanov
 */
public class HttpConnectionHandler implements ConnectionHandler {
    private HttpGateway gateway;

    @Override
    public void initConnection(Connection connection) {
        HttpContext context = new HttpContext();
        connection.setContext(context);
    }

    @Override
    public void handleData(byte[] bytes, int offset, int length, Connection connection) throws IOException {
        HttpContext context = (HttpContext) connection.getContext();
        try {
            context.parseData(bytes, offset, length);

            if (context.isRequestParsed()) {
                HttpRequest request = context.getRequest();
                ReadableHttpResponse response = new ReadableHttpResponse();

                boolean served;
                try {
                    served = gateway.doServe(request, response);
                } catch (RuntimeException e) {
                    throw new HttpStatusException(e, HttpStatus.INTERNAL_SERVER_ERROR);
                }
                if (!served) {
                    response.setStatus(HttpStatus.NOT_FOUND);
                }

                sendResponse(response, connection);
            }
        } catch (HttpStatusException e) {
            e.printStackTrace();

            ReadableHttpResponse response = new ReadableHttpResponse();
            e.printStackTrace(new PrintWriter(response.getContent(), true));
            response.setStatus(e.getStatus());
            response.getHeaders().putAll(e.getHeaders());

            sendResponse(response, connection);
        }
    }

    private void sendResponse(ReadableHttpResponse response, final Connection connection) throws IOException {

        ByteArrayOutputStream responseMetadata = new ByteArrayOutputStream();
        Writer writer = new OutputStreamWriter(responseMetadata, "utf8");

        writer.write("HTTP/1.1 " + response.getStatus().getCode() + " " + response.getStatus().getMessage() + "\n");

        response.getHeaders().put("Content-Length", response.getContent().size() + "");
        for (Map.Entry<String, String> header: response.getHeaders().entrySet()) {
            if (header.getValue() == null) {
                continue;
            }
            writer.write(header.getKey() + ": " + header.getValue() + "\n");
        }
        writer.write("\n");
        writer.flush();

        OutputStream connectionStream = new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                throw new UnsupportedOperationException();
            }

            @Override
            public void write(byte[] b, int off, int len) throws IOException {
                connection.sendData(b, off, len);
            }
        };
        responseMetadata.writeTo(connectionStream);
        response.getContent().writeTo(connectionStream);

        writer.flush();

        connection.close();
    }

    @Override
    public void destroyConnection(Connection connection) {
    }

    public void setGateway(HttpGateway gateway) {
        this.gateway = gateway;
    }
}
