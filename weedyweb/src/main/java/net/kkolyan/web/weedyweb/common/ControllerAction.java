package net.kkolyan.web.weedyweb.common;

import net.kkolyan.web.weedyweb.api.ModelAndView;

import java.util.Map;

/**
 * @author nplekhanov
 */
public interface ControllerAction {
    ModelAndView execute(Object instance, Map<String, Polymorphic> params) throws Exception;
}
