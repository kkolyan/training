package net.kkolyan.web.weedyweb.api;

/**
 * @author nplekhanov
 */
public final class ModelAndView {
    private Object model;
    private String viewName;

    public ModelAndView(String viewName) {
        this.viewName = viewName;
    }

    public ModelAndView(String viewName, Object model) {
        this.model = model;
        this.viewName = viewName;
    }

    public Object getModel() {
        return model;
    }

    public String getViewName() {
        return viewName;
    }
}
