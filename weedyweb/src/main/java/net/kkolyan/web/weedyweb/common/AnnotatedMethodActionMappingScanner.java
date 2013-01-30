package net.kkolyan.web.weedyweb.common;

import net.kkolyan.web.weedyweb.api.Action;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @author nplekhanov
 */
public class AnnotatedMethodActionMappingScanner implements ActionMappingScanner {

    @Override
    public void scanForConfiguration(Class componentClass, ActionMappingCallback callback) {
        for (Method method: componentClass.getMethods()) {
            Action config = method.getAnnotation(Action.class);
            if (config == null) {
                continue;
            }
            ControllerAction action = new MethodControllerAction(method, config.params(), config.view());
            RequestMatcher matcher = new PathAndMethodRequestMatcher(config.path(), Arrays.asList(config.method()));
            callback.addActionMapping(matcher, action);
        }
    }

}
