package net.kkolyan.web.weedyweb.api;

/**
 * @author <a href="mailto:nplekhanov86@gmail.com">nplekhanov</a>
 */
public interface InterceptionPoint {
    ModelAndView executeAction() throws Exception;
}
