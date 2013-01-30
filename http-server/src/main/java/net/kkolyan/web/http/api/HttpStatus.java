package net.kkolyan.web.http.api;

/**
 * @author nplekhanov
 */
public enum HttpStatus {
    OK(200, "OK", Type.SUCCESS),
    MOVED_PERMANENTLY(301, "Moved Permanently", Type.REDIRECT),
    FOUND(302, "Found", Type.REDIRECT),
    BAD_REQUEST(400, "Bad Request", Type.CLIENT_ERROR),
    NOT_FOUND(404, "Not Found", Type.CLIENT_ERROR),
    METHOD_NOT_ALLOWED(405, "Method Not Allowed", Type.CLIENT_ERROR),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error", Type.SERVER_ERROR),
    HTTP_VERSION_NOT_SUPPORTED(505, "HTTP Version Not Supported", Type.SERVER_ERROR);
    
    public enum Type {
        SUCCESS, CLIENT_ERROR, SERVER_ERROR, REDIRECT
    }
    
    private int code;
    private String message;
    private Type type;

    private HttpStatus(int code, String message, Type type) {
        this.code = code;
        this.message = message;
        this.type = type;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public boolean isRedirect() {
        return type == Type.REDIRECT;
    }

    public boolean isError() {
        return type == Type.CLIENT_ERROR || type == Type.SERVER_ERROR;
    }

    public boolean isSuccess() {
        return type == Type.SUCCESS;
    }

    @Override
    public String toString() {
        return "HttpStatus{" + code + " " + message + "}";
    }
}
