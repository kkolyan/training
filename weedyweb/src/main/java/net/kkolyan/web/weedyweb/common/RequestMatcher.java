package net.kkolyan.web.weedyweb.common;

import java.util.Map;

/**
 * @author nplekhanov
 */
public interface RequestMatcher {
    boolean match(String requestPath, String requestMethod, Map<String,Polymorphic> params);
}
