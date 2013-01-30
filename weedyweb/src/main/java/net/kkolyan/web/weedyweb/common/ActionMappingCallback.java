package net.kkolyan.web.weedyweb.common;

/**
* @author nplekhanov
*/
public interface ActionMappingCallback {
    void addActionMapping(RequestMatcher matcher, ControllerAction action);
}
