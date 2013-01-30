package net.kkolyan.web.weedyweb.common;

import java.util.Arrays;

/**
 * @author nplekhanov
 */
public class PolymorphicText implements Polymorphic {
    private String text;

    public PolymorphicText(String text) {
        this.text = text;
    }

    @Override
    public Object morphTo(Class type) {
        if (text == null) {
            return null;
        }

        if (CharSequence.class.isAssignableFrom(type)) return text;

        text = text.trim();

        if (type == boolean.class) return Arrays.asList("", "true", "1").contains(text);
        if (type == Boolean.class) return Arrays.asList("", "true", "1").contains(text);

        if (type == int.class) return Integer.parseInt(text);
        if (type == long.class) return Long.parseLong(text);
        if (type == float.class) return Float.parseFloat(text);
        if (type == double.class) return Double.parseDouble(text);

        if (type == Integer.class) return Integer.parseInt(text);
        if (type == Long.class) return Long.parseLong(text);
        if (type == Float.class) return Float.parseFloat(text);
        if (type == Double.class) return Double.parseDouble(text);

        if (Enum.class.isAssignableFrom(type)) {
            @SuppressWarnings({"unchecked", "UnnecessaryLocalVariable"})
            Object enumValue = Enum.valueOf(type, text);
            return enumValue;
        }

        throw new IllegalStateException("unsupported parameter type: "+type);
    }

    @Override
    public String toString() {
        return text;
    }
}
