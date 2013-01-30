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

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author nplekhanov
 */
public class GenericTypes {

    public static Class getRaw(Type genericType) {
        if (genericType instanceof ParameterizedType) {
            return getRaw(((ParameterizedType) genericType).getRawType());
        }
        if (genericType instanceof Class) {
            return (Class) genericType;
        }
        throw new IllegalArgumentException("unsupported generic type: "+genericType);
    }

    public static Class getParameter(Type genericType, int i) {
        Type type = getGenericParameter(genericType, i);
        return getRaw(type);
    }

    public static Type getGenericParameter(Type genericType, int i) {
        if (genericType instanceof ParameterizedType) {
            return ((ParameterizedType) genericType).getActualTypeArguments()[i];
        }
        return Object.class;
    }
}
