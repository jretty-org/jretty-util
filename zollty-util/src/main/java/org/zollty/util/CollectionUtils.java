/* 
 * Copyright (C) 2014-2015 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License").
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * Create by ZollTy on 2014-6-22 (http://blog.zollty.com/, zollty@163.com)
 */
package org.zollty.util;

import java.lang.reflect.Array;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Queue;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * 集合工具类
 * 
 * @author zollty
 * @since 2014-06-22
 */
public class CollectionUtils {
    
    /**
     * Checks if the collection is null or empty (size==0).
     * @return true if is null or empty
     */
    public static boolean isNullOrEmpty(Collection<?> coll) {
        return (coll == null || coll.isEmpty()) ? true : false;
    }

    /**
     * Checks if the collection is null or empty (size==0).
     * @return true if is not null and not empty
     */
    public static boolean isNotEmpty(Collection<?> coll) {
        return (coll != null && !coll.isEmpty()) ? true : false;
    }
    
    
    /**
     * Checks if the collection is null or empty (size==0).
     * @return true if is null or empty
     */
    public static boolean isNullOrEmpty(Enumeration<?> enu) {
        return (enu == null || !enu.hasMoreElements()) ? true : false;
    }

    /**
     * Checks if the collection is null or empty (size==0).
     * @return true if is not null and not empty
     */
    public static boolean isNotEmpty(Enumeration<?> enu) {
        return (enu != null && enu.hasMoreElements()) ? true : false;
    }
    
    /**
     * Convenience method to return a Collection as a delimited (e.g. CSV)
     * String. E.g. useful for <code>toString()</code> implementations.
     * @param coll the Collection to display
     * @param delim the delimiter to use (probably a ",")
     * @return the delimited String
     */
    public static String toString(Collection<?> coll, String delim) {
        if ( coll == null || coll.isEmpty() ) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        Iterator<?> it = coll.iterator();
        while (it.hasNext()) {
            sb.append(it.next());
            if (it.hasNext()) {
                sb.append(delim);
            }
        }
        return sb.toString();
    }

    /**
     * Convenience method to return a Collection as a CSV String.
     * E.g. useful for <code>toString()</code> implementations.
     * @param coll the Collection to display
     * @return the delimited String
     */
    public static String toString(Collection<?> coll) {
        return toString(coll, ",");
    }
    
    /**
     * Check Array Element Duplication. <p>
     * If it's main type element array, use like this <p>
     * <code> checkDuplication( changeType(array) ) </code>
     * @return return -1 if no duplication
     */
    public static int checkDuplication(Object[] array) {
        return checkDuplication(Arrays.asList(array));
    }
    
    /**
     * Check List Element Duplication
     * @return return -1 if no duplication
     */
    public static int checkDuplication(List<?> array) {
        int size = array.size();
        if(array!=null && size>0)
        {
            for(int i=0;i<size;i++){
                for(int j=i+1;j<size;j++){
                    if( safeEqual(array.get(j), array.get(i)) ) {
                        return i;
                    }
                }
            }
        }
        return -1;
    }
    
    
    /**
     * 根据类型自动返回一个集合
     */
    @SuppressWarnings("rawtypes")
    public static Collection getCollectionObj(Class<?> clazz) {
        if (clazz.isInterface()) {
            if (clazz.isAssignableFrom(List.class))
                return new ArrayList();
            else if (clazz.isAssignableFrom(Set.class))
                return new HashSet();
            else if (clazz.isAssignableFrom(Queue.class))
                return new ArrayDeque();
            else if (clazz.isAssignableFrom(SortedSet.class))
                return new TreeSet();
            else if (clazz.isAssignableFrom(BlockingQueue.class))
                return new LinkedBlockingDeque();
            else
                return null;
        }
        else {
            Collection collection = null;
            try {
                collection = (Collection) clazz.newInstance();
            }
            catch (Exception e) {
                throw new NestedRuntimeException(e);
            }
            return collection;
        }
    }

    /**
     * 根据类型自动返回一个Map
     */
    @SuppressWarnings("rawtypes")
    public static Map getMapObj(Class<?> clazz) {
        if (clazz.isInterface()) {
            if (clazz.isAssignableFrom(Map.class))
                return new HashMap();
            else if (clazz.isAssignableFrom(ConcurrentMap.class))
                return new ConcurrentHashMap();
            else if (clazz.isAssignableFrom(SortedMap.class))
                return new TreeMap();
            else if (clazz.isAssignableFrom(NavigableMap.class))
                return new TreeMap();
            else if (clazz.isAssignableFrom(ConcurrentNavigableMap.class))
                return new ConcurrentSkipListMap();
            else
                return null;
        }
        else {
            Map map = null;
            try {
                map = (Map) clazz.newInstance();
            }
            catch (Exception e) {
                throw new NestedRuntimeException(e);
            }
            return map;
        }
    }

    /**
     * Copy the given Collection into array. The Collection must contain &lt;T&gt; elements only.
     * 
     * @param collection
     *            the Collection to copy
     * @return the array (<code>null</code> if the passed-in Collection was <code>null</code>)
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] toArray(Collection<T> collection, Class<T> arrayType) {
        if (collection == null) {
            return null;
        }
        if (arrayType == null) {
            throw new IllegalArgumentException("arrayType can't be null");
        }
        int size = collection.size();
        // Allocate a new Array
        T[] newArray = (T[]) Array.newInstance(arrayType, size);
        Iterator<T> iterator = collection.iterator();
        // Convert and set each element in the new Array
        for (int i = 0; i < size; i++) {
            T element = iterator.next();
            newArray[i] = element;
        }
        return newArray;
    }
    
    /**
     * Copy the given Enumeration into array. The Enumeration must contain &lt;T&gt; elements only.
     * 
     * @param enums
     *            the Enumeration to copy
     * @return the array (<code>null</code> if the passed-in Enumeration was <code>null</code>)
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] toArray(Enumeration<T> enums, Class<T> arrayType) {
        if (enums == null) {
            return null;
        }
        if( arrayType==null ) {
            throw new IllegalArgumentException("arrayType can't be null");
        }
        if (!enums.hasMoreElements()) {
            return (T[]) Array.newInstance(arrayType, 0);
        }
        List<T> list = Collections.list(enums);
        int size = list.size();
        T[] result = (T[]) Array.newInstance(arrayType, size);
        Object[] array = list.toArray(new Object[size]);
        System.arraycopy(array, 0, result, 0, size);
        return result;
    }

    
    // helper method ~~~
    
    private static boolean safeEqual(Object obj1, Object obj2) {
        if (obj1 != null) {
            return obj1.equals(obj2);
        }
        if (obj2 == null) {
            return true;
        }
        return true;
    }

}
