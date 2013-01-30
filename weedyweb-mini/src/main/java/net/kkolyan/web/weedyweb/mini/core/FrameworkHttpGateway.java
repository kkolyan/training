package net.kkolyan.web.weedyweb.mini.core;

import net.kkolyan.web.http.api.HttpGateway;
import net.kkolyan.web.http.api.HttpRequest;
import net.kkolyan.web.http.api.HttpResponse;
import net.kkolyan.web.http.api.HttpStatusException;
import net.kkolyan.web.weedyweb.api.ActionInterceptor;
import net.kkolyan.web.weedyweb.common.*;
import net.kkolyan.web.weedyweb.content.ContentHandler;
import net.kkolyan.web.weedyweb.mini.core.view.View;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author nplekhanov
 */
public class FrameworkHttpGateway implements HttpGateway {

    @Resource
    private List<View> views;

    @Resource
    private List<ContentHandler> contentHandlers;

    private List<RequestHandler> handlers = new ArrayList<RequestHandler>();

    @Resource
    private List<Object> components;

    @Resource
    private List<ActionInterceptor> interceptors;

    @Resource
    private ActionMappingScanner actionMappingScanner;

    @PostConstruct
    public void startup() throws Exception {
        for (final Object component: components) {
            if (component instanceof RequestHandler) {
                handlers.add((RequestHandler) component);
            }
            actionMappingScanner.scanForConfiguration(component.getClass(), new ActionMappingCallback() {
                @Override
                public void addActionMapping(RequestMatcher matcher, ControllerAction action) {
                    handlers.add(new ActionRequestHandler(matcher, action, views, contentHandlers, interceptors, component));
                }
            });
        }
    }

    @Override
    public boolean doServe(HttpRequest request, HttpResponse response) throws HttpStatusException {
        for (RequestHandler handler: handlers) {
            if (handler.handle(request, response)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "FrameworkHttpGateway{" +
                "views=" + views +
                ", contentHandlers=" + contentHandlers +
                ", handlers=" + handlers +
                '}';
    }

    public List<View> getViews() {
        return views;
    }

    public void setViews(List<View> views) {
        this.views = views;
    }

    public List<ContentHandler> getContentHandlers() {
        return contentHandlers;
    }

    public void setContentHandlers(List<ContentHandler> contentHandlers) {
        this.contentHandlers = contentHandlers;
    }

    public List<RequestHandler> getHandlers() {
        return handlers;
    }

    public void setHandlers(List<RequestHandler> handlers) {
        this.handlers = handlers;
    }

    public void setComponents(List<Object> components) {
        this.components = components;
    }
}
