package net.kkolyan.spring.altimpl.misc;

import net.kkolyan.spring.altimpl.Property;
import net.kkolyan.spring.altimpl.Transformer;
import net.kkolyan.spring.altimpl.LazyValue;

import java.lang.reflect.Field;

/**
 * @author nplekhanov
 */
public class FieldBasedProperty implements Property {
    private Field field;
    private LazyValue value;

    public FieldBasedProperty(Field field, LazyValue value) {
        this.field = field;
        this.value = value;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void injectInto(Object instance, Transformer transformer) throws Exception {
        Object o = value.evaluate();
        o = transformer.transform(field.getGenericType(), o);
        o = field.getType().cast(o);
        field.setAccessible(true);
        field.set(instance, o);
    }
}
