package net.kkolyan.web.weedyweb.mini.core;

import net.kkolyan.spring.altimpl.misc.MapBasedScope;
import net.kkolyan.web.http.api.HttpSessionContext;

import java.util.HashMap;
import java.util.Map;

/**
 * @author nplekhanov
 */
public class SessionScope extends MapBasedScope {
    private HttpSessionContext sessionContext;
    private Object sessionKey = "spring-mini-beans";

    public SessionScope(String name, HttpSessionContext sessionContext) {
        super(name);
        this.sessionContext = sessionContext;
    }

    @Override
    protected Map<String, Entry> getEntries() {
        ScopeData data = (ScopeData) sessionContext.getAttribute(sessionKey);
        if (data == null) {
            data = new ScopeData();
            sessionContext.setAttribute(sessionKey, data);
        }
        return data.entries;
    }

    private static class ScopeData {
        Map<String, Entry> entries = new HashMap<String, Entry>();
    }
}
