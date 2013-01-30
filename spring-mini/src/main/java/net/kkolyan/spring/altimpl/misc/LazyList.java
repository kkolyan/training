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

import net.kkolyan.spring.altimpl.LazyValue;

import java.util.AbstractList;
import java.util.List;

/**
* @author nplekhanov
*/
public class LazyList<T> extends AbstractList<T> implements LazyValue {
    private List<? extends LazyValue> list;

    public LazyList(List<? extends LazyValue> list) {
        this.list = list;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T get(int index) {
        try {
            return (T) list.get(index).evaluate();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public Object evaluate() throws Exception {
        return this;
    }
}
