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

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.jretty.util.support.OrderUtils;

/**
 * 
 * @author zollty
 * @since 2018年9月29日
 */
public class AnnotationAwareOrderComparator extends OrderComparator {

    /**
     * Shared default instance of {@code AnnotationAwareOrderComparator}.
     */
    public static final AnnotationAwareOrderComparator INSTANCE = new AnnotationAwareOrderComparator();


    /**
     * This implementation checks for {@link Order @Order} or
     * {@link javax.annotation.Priority @Priority} on various kinds of
     * elements, in addition to the {@link org.springframework.core.Ordered}
     * check in the superclass.
     */
    protected Integer findOrder(Object obj) {
        // Check for regular Ordered interface
        Integer order = super.findOrder(obj);
        if (order != null) {
            return order;
        }

        // Check for @Order and @Priority on various kinds of elements
        if (obj instanceof Class) {
            return OrderUtils.getOrder((Class<?>) obj);
        }
        else if (obj instanceof Method) {
            Order ann = AnnotationUtils.findAnnotation((Method) obj, Order.class);
            if (ann != null) {
                return ann.value();
            }
        }
        else if (obj instanceof AnnotatedElement) {
            Order ann = AnnotationUtils.getAnnotation((AnnotatedElement) obj, Order.class);
            if (ann != null) {
                return ann.value();
            }
        }
        else if (obj != null) {
            order = OrderUtils.getOrder(obj.getClass());
        }

        return order;
    }

    /**
     * Sort the given List with a default AnnotationAwareOrderComparator.
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
     * Sort the given array with a default AnnotationAwareOrderComparator.
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
