package net.kkolyan.web.http.api;

/**
 * @author nplekhanov
 */
public interface HttpSessionContext {
    Object getAttribute(Object key);
    void setAttribute(Object key, Object value);
}
