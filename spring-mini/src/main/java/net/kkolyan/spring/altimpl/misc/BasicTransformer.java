/**
 * This code contains copyright information which is the proprietary property
 * of SITA Information Network Computing Limited (SITA). No part of this
 * code may be reproduced, stored or transmitted in any form without the prior
 * written permission of SITA.
 *
 * Copyright (C) SITA Information Network Computing Limited 2009-2012.
 * All rights reserved.
 */
package net.kkolyan.spring.altimpl.misc;

import net.kkolyan.spring.altimpl.Transformer;

import java.io.File;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;

/**
 * @author nplekhanov
 */
public class BasicTransformer implements Transformer {

    @Override
    public Object transform(Type genericType, Object o) throws Exception {
        Class type = GenericTypes.getRaw(genericType);
        if (o == null) {
            if (type.isPrimitive()) {
                if (type == boolean.class) {
                    return false;
                }
                return transform(null, "0");
            }
            return null;
        }

        if (Collection.class.isAssignableFrom(type)) {
            @SuppressWarnings("unchecked")
            Collection<Object> source = (Collection) o;
            Type elementType = GenericTypes.getGenericParameter(genericType, 0);
            return new TransformationList(Arrays.asList(source.toArray()), this, elementType);
        }

        if (type.isInstance(o)) {
            return o;
        }

        String text = o.toString();

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

        if (type == Class.class) {
            return Class.forName(text);
        }

        if (File.class.isAssignableFrom(type)) {
            return new File(text).getAbsoluteFile();
        }

        throw new IllegalStateException("unsupported parameter type: "+type);
    }
}
