package net.kkolyan.web.weedyweb.mini.core.view;

/**
 * @author nplekhanov
 */
public interface View {
    boolean renderView(Object model, String viewName, RenderingContext renderingContext) throws Exception;
}
