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

import java.lang.reflect.Type;
import java.util.AbstractList;
import java.util.List;
import java.util.Set;

/**
 * @author nplekhanov
 */
public class TransformationList<T> extends AbstractList<T> implements Set<T> {
    private List<Object> list;
    private Transformer transformer;
    private Type elementType;

    public TransformationList(List<Object> list, Transformer transformer, Type elementType) {
        this.list = list;
        this.transformer = transformer;
        this.elementType = elementType;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T get(int index) {
        Object o = list.get(index);
        try {
            return (T) transformer.transform(elementType, o);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public int size() {
        return list.size();
    }
}
