package net.kkolyan.web.weedyweb.common;

import net.kkolyan.web.weedyweb.api.ModelAndView;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author nplekhanov
 */
public class MethodControllerAction implements ControllerAction {
    private Method method;
    private String[] paramNames;
    private String defaultViewName;

    public MethodControllerAction(Method method, String[] paramNames, String defaultViewName) {
        this.method = method;
        this.paramNames = paramNames;
        this.defaultViewName = defaultViewName;
    }

    @Override
    public ModelAndView execute(Object instance, Map<String, Polymorphic> params) throws Exception {
        List<Polymorphic> args = new ArrayList<Polymorphic>();
        for (String paramName: paramNames) {
            Polymorphic polymorphic = params.get(paramName);
            args.add(polymorphic);
        }
        Object result = call(instance, args);
        if (result instanceof ModelAndView) {
            return (ModelAndView) result;
        }
        if (result == null) {
            return new ModelAndView(defaultViewName, instance);
        }
        return new ModelAndView(defaultViewName, result);
    }

    private Object call(Object instance, List<Polymorphic> polymorphicArgs) throws Exception {
        Object[] args = new Object[polymorphicArgs.size()];
        for (int i = 0; i < args.length; i ++) {
            Polymorphic polymorphic = polymorphicArgs.get(i);
            Class type = method.getParameterTypes()[i];
            if (polymorphic == null) {
                args[i] = defaultValueOf(type);
            } else {
                args[i] = polymorphic.morphTo(type);
            }
        }
        return method.invoke(instance, args);
    }

    private Object defaultValueOf(Class aClass) {
        if (aClass.isPrimitive()) {
            if (aClass == boolean.class) {
                return false;
            }
            return new PolymorphicText("0").morphTo(aClass);
        }
        return null;
    }
}
