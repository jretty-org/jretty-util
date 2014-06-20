/* @(#)ConvertUtils.java 
 * Copyright (C) 2013-2014 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License").
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * Create by zollty on 2013-6-25 [http://blog.csdn.net/zollty (or GitHub)]
 */
package org.zollty.util;

import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;


/**
 * 类型转换工具类
 * @author zollty
 * @since 2014-6-25
 */
public class ConvertUtils {

	/**
	 * Copy the given Collection into a String array.
	 * The Collection must contain String elements only.
	 * @param collection the Collection to copy
	 * @return the String array (<code>null</code> if the passed-in
	 * Collection was <code>null</code>)
	 */
    public static String[] toStringArray(Collection<String> collection) {
        if (collection == null) {
            return null;
        }
        return collection.toArray(new String[collection.size()]);
    }
    
    public static Object[] collectionToArray(Collection<Object> collection) {
        if (collection == null) {
            return null;
        }
        return collection.toArray(new Object[collection.size()]);
    }
	
	/**
	 * Copy the given Enumeration into a String array.
	 * The Enumeration must contain String elements only.
	 * @param enumeration the Enumeration to copy
	 * @return the String array (<code>null</code> if the passed-in
	 * Enumeration was <code>null</code>)
	 */
    public static String[] toStringArray(Enumeration<String> enumeration) {
        if (enumeration == null) {
            return null;
        }
        List<String> list = Collections.list(enumeration);
        return list.toArray(new String[list.size()]);
    }
    
    @SuppressWarnings("unchecked")
    public static <T>T[] enumerationToArray(Enumeration<T> enumeration) {
        if (enumeration == null) {
            return null;
        }
        List<T> list = Collections.list(enumeration);
        return (T[])list.toArray(new Object[list.size()]);
    }
    
}