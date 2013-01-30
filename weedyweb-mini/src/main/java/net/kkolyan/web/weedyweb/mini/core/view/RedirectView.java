package net.kkolyan.web.weedyweb.mini.core.view;

/**
 * @author nplekhanov
 */
public class RedirectView implements View {

    @Override
    public boolean renderView(Object model, String viewName, RenderingContext renderingContext) throws Exception {
        if (viewName.startsWith("redirect:")) {
            String location = viewName.substring("redirect:".length());

            return redirect(location, renderingContext);
        }
        if (viewName.equals("")) {
            String referer = renderingContext.getRequestHeader("Referer");
            if (referer == null) {
                referer = "";
            }
            return redirect(referer, renderingContext);
        }
        return false;
    }
    
    private boolean redirect(String location, RenderingContext context) {
        String contextPath = getContextPath();
        if (location.startsWith("/") && contextPath != null) {
            location = contextPath + location;
        }
        context.setResponseHeader("Location", location);
        context.setStatusCode(302);
        return true;
    }

    public String getContextPath() {
        return "";
    }

    @Override
    public String toString() {
        return "RedirectView{" +
                '}';
    }
}
