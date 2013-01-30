package net.kkolyan.web.weedyweb.common;

import net.kkolyan.web.weedyweb.api.ActionInterceptor;
import net.kkolyan.web.weedyweb.api.InterceptionPoint;
import net.kkolyan.web.weedyweb.api.ModelAndView;

import java.util.List;

/**
 * @author <a href="mailto:nplekhanov86@gmail.com">nplekhanov</a>
 */
public class ActionInterception {
    public static ModelAndView executeViaInterceptors(final List<ActionInterceptor> interceptors, final InterceptionPoint target) throws Exception {
        InterceptionPoint point = new InterceptionPoint() {
            int depth;
            @Override
            public ModelAndView executeAction() throws Exception {
                if (depth < interceptors.size()) {
                    return interceptors.get(depth++).intercept(this);
                }
                return target.executeAction();
            }
        };
        return point.executeAction();
    }
}
