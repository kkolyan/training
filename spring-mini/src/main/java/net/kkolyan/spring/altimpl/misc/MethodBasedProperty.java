package net.kkolyan.spring.altimpl.misc;

import net.kkolyan.spring.altimpl.Property;
import net.kkolyan.spring.altimpl.Transformer;
import net.kkolyan.spring.altimpl.LazyValue;

import java.lang.reflect.Method;

/**
 * @author nplekhanov
 */
public class MethodBasedProperty implements Property {

    private Method setter;
    private LazyValue value;

    public MethodBasedProperty(Class beanClass, String propertyName, LazyValue value) throws Exception {
        this.value = value;
        setter = findSetter(beanClass, propertyName);
    }

    public MethodBasedProperty(Method setter, LazyValue value) {
        this.setter = setter;
        this.value = value;
    }

    protected Method findSetter(Class aClass, String property) {
        property = property.substring(0, 1).toUpperCase() + property.substring(1);
        String setterName = "set" + property;
        for (Method method: aClass.getMethods()) {
            if (method.getName().equals(setterName) && method.getParameterTypes().length == 1) {
                return method;
            }
        }
        throw new IllegalStateException();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void injectInto(Object instance, Transformer transformer) throws Exception {
        Object value = this.value.evaluate();
        value = transformer.transform(setter.getGenericParameterTypes()[0], value);
        value = setter.getParameterTypes()[0].cast(value);
        setter.invoke(instance, value);
    }
}
