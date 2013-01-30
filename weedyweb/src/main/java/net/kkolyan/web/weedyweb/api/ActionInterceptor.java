package net.kkolyan.web.weedyweb.api;

/**
 * @author <a href="mailto:nplekhanov86@gmail.com">nplekhanov</a>
 */
public interface ActionInterceptor {
    ModelAndView intercept(InterceptionPoint interceptionPoint) throws Exception;
}
