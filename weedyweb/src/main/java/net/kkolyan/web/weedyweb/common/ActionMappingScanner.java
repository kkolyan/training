package net.kkolyan.web.weedyweb.common;

/**
 * @author nplekhanov
 */
public interface ActionMappingScanner {
    void scanForConfiguration(Class componentClass, ActionMappingCallback callback);
}
