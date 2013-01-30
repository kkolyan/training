package net.kkolyan.web.weedyweb.mini.core;

import net.kkolyan.web.http.api.HttpRequest;
import net.kkolyan.web.http.api.HttpResponse;
import net.kkolyan.web.http.api.HttpStatus;
import net.kkolyan.web.http.api.HttpStatusException;
import net.kkolyan.web.weedyweb.api.ActionInterceptor;
import net.kkolyan.web.weedyweb.api.InterceptionPoint;
import net.kkolyan.web.weedyweb.api.ModelAndView;
import net.kkolyan.web.weedyweb.common.ActionInterception;
import net.kkolyan.web.weedyweb.common.ControllerAction;
import net.kkolyan.web.weedyweb.common.Polymorphic;
import net.kkolyan.web.weedyweb.common.RequestMatcher;
import net.kkolyan.web.weedyweb.content.ContentHandler;
import net.kkolyan.web.weedyweb.mini.core.view.View;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.kkolyan.web.weedyweb.common.ActionInterception.*;

/**
 * @author nplekhanov
 */
public class ActionRequestHandler implements RequestHandler {
    private final RequestMatcher matcher;
    private final ControllerAction action;
    private final List<View> views;
    private final List<ContentHandler> contentHandlers;
    private final Object instance;
    private List<ActionInterceptor> interceptors;

    public ActionRequestHandler(RequestMatcher matcher, ControllerAction action, List<View> views,
                                List<ContentHandler> contentHandlers, List<ActionInterceptor> interceptors,
                                Object instance) {
        this.matcher = matcher;
        this.action = action;
        this.views = views;
        this.contentHandlers = contentHandlers;
        this.interceptors = interceptors;
        this.instance = instance;
    }

    @Override
    public boolean handle(HttpRequest request, HttpResponse response) throws HttpStatusException {
        final Map<String,Polymorphic> params = new HashMap<String, Polymorphic>();
        if (matcher.match(request.getPath(), request.getMethod(), params)) {
            try {

                InputStream queryAsStream = new ByteArrayInputStream(request.getQuery().getBytes("utf8"));
                fetchContent(queryAsStream, "application/x-www-form-urlencoded", params);

                if ("POST".equalsIgnoreCase(request.getMethod())) {
                    String contentType = request.getHeaders().get("Content-Type");
                    fetchContent(request.getContent(), contentType, params);
                }

                ModelAndView modelAndView = executeViaInterceptors(interceptors, new InterceptionPoint() {
                    @Override
                    public ModelAndView executeAction() throws Exception {
                        return action.execute(instance, params);
                    }
                });
                for (View view: views) {
                    if (view.renderView(
                            modelAndView.getModel(),
                            modelAndView.getViewName(),
                            new WeedyWebRenderingContext(request, response))) {
                        return true;
                    }
                }
                throw new IllegalStateException("can't find renderer");
            } catch (Exception e) {
                throw new HttpStatusException(e, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return false;
    }

    private void fetchContent(InputStream stream, String contentType, Map<String, Polymorphic> store) throws IOException {
        for (ContentHandler contentHandler : contentHandlers) {
            if (contentHandler.handleContent(stream, contentType, store)) {
                return;
            }
        }
        throw new IllegalStateException("unsupported content type: "+contentType);
    }
}
