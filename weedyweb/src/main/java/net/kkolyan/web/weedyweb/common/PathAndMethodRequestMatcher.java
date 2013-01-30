package net.kkolyan.web.weedyweb.common;

import java.util.Collection;
import java.util.Map;

/**
 * @author nplekhanov
 */
public class PathAndMethodRequestMatcher implements RequestMatcher {
    private String path;
    private Collection<String> methods;

    public PathAndMethodRequestMatcher(String path, Collection<String> methods) {
        this.path = path;
        this.methods = methods;
    }

    @Override
    public boolean match(String requestPath, String requestMethod, Map<String, Polymorphic> params) {
        return path.equalsIgnoreCase(requestPath) && methods.contains(requestMethod.toUpperCase());
    }
}
