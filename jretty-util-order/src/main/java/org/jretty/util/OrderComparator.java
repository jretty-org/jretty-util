/* 
 * Copyright (C) 2013-2018 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License").
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * Create by ZollTy on 2018-9-28 (http://blog.zollty.com/, zollty@163.com)
 */
package org.jretty.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * {@link Comparator} implementation for {@link Ordered} objects, sorting
 * by order value ascending, respectively by priority descending.
 *
 * <h3>Same Order Objects</h3>
 * <p>Objects that have the same order value will be sorted with arbitrary
 * ordering with respect to other objects with the same order value.
 *
 * <h3>Non-ordered Objects</h3>
 * <p>Any object that does not provide its own order value is implicitly
 * assigned a value of {@link Ordered#LOWEST_PRECEDENCE}, thus ending up
 * at the end of a sorted collection in arbitrary order with respect to
 * other objects with the same order value.
 * 
 * @author zollty
 * @since 2018年9月29日
 */
public class OrderComparator implements Comparator<Object> {
    /**
     * Shared default instance of {@code OrderComparator}.
     */
    public static final OrderComparator INSTANCE = new OrderComparator();

    @Override
    public int compare(Object o1, Object o2) {
        int i1 = getOrder(o1);
        int i2 = getOrder(o2);
        return (i1 < i2) ? -1 : (i1 > i2) ? 1 : 0;
    }

    /**
     * Determine the order value for the given object.
     * <p>The default implementation checks against the {@link Ordered} interface
     * through delegating to {@link #findOrder}. Can be overridden in subclasses.
     * @param obj the object to check
     * @return the order value, or {@code Ordered.LOWEST_PRECEDENCE} as fallback
     */
    protected int getOrder(Object obj) {
        Integer order = findOrder(obj);
        return (order != null ? order : Ordered.LOWEST_PRECEDENCE);
    }

    /**
     * Find an order value indicated by the given object.
     * <p>The default implementation checks against the {@link Ordered} interface.
     * Can be overridden in subclasses.
     * @param obj the object to check
     * @return the order value, or {@code null} if none found
     */
    protected Integer findOrder(Object obj) {
        return (obj instanceof Ordered ? ((Ordered) obj).getOrder() : null);
    }

    /**
     * Sort the given List with a default OrderComparator.
     * <p>Optimized to skip sorting for lists with size 0 or 1,
     * in order to avoid unnecessary array extraction.
     * @param list the List to sort
     * @see java.util.Collections#sort(java.util.List, java.util.Comparator)
     */
    public static void sort(List<?> list) {
        if (list.size() > 1) {
            Collections.sort(list, INSTANCE);
        }
    }

    /**
     * Sort the given array with a default OrderComparator.
     * <p>Optimized to skip sorting for lists with size 0 or 1,
     * in order to avoid unnecessary array extraction.
     * @param array the array to sort
     * @see java.util.Arrays#sort(Object[], java.util.Comparator)
     */
    public static void sort(Object[] array) {
        if (array.length > 1) {
            Arrays.sort(array, INSTANCE);
        }
    }

}
