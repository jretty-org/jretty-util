/* 
 * Copyright (C) 2014-2017 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License").
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * Create by ZollTy on 2014-6-11 (http://blog.zollty.com/, zollty@163.com)
 */
package org.jretty.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

/**
 * @author zollty
 * @since 2014-6-11
 */
public class ObjectUtils {
    
    /**
     * 比较两个值的大小，支持null
     */
    public static <T> int compareTo(Comparable<T> obj, T other) {
        if (obj != null) {
            if (other != null) {
                return obj.compareTo(other);
            }
            return 1;
        }
        else {
            if (other != null) {
                return -1;
            }
            return 0;
        }
    }
    
    /**
     * Determine whether the given object is an array:
     * either an Object array or a primitive array.
     * @param obj the object to check
     */
    public static boolean isArray(Object obj) {
        return (obj != null && obj.getClass().isArray());
    }
    
    /**
     * 给对象赋初始值<br>
     * （该方法等价于{@code initValue(Object, true, false)}
     * ，也就是说会覆盖旧值，而且bean要是标准的、属性能完全被setter方法赋值）
     * 
     * @param standardBean 标准bean，（带setter&&getter方法）
     */
    public static void initValue(Object standardBean) {
        initValue(standardBean, true, false);
    }
    
    /**
     * 给对象赋初始值<br>
     * （该方法等价于{@code initValue(Object, boolean, false)}）
     * 
     * @param standardBean 标准bean，（带setter&&getter方法）
     * @param overwrittenOldValue 是否覆盖旧值
     */
    public static void initValue(Object standardBean, boolean overwrittenOldValue) {
        initValue(standardBean, overwrittenOldValue, false);
    }
    
    /**
     * 给对象赋初始值
     * 
     * @param standardBean 标准bean，（带setter&&getter方法）
     * @param overwrittenOldValue 是否覆盖旧值
     * @param allowIncompleteInit 允许不完取赋值
     */
    public static void initValue(Object standardBean, 
            boolean overwrittenOldValue, boolean allowIncompleteInit) {
        Assert.notNull(standardBean, "param can not be null.");
        Class<?> clazz = standardBean.getClass();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
            PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor pd : pds) {
                if ("class".equals(pd.getName())) {
                    continue;
                }
                Method mSet = pd.getWriteMethod();
                if (mSet == null) {
                    if (allowIncompleteInit) { // 跳过
                        continue;
                    }
                    throw new IllegalArgumentException(
                            "can not find set method for " + pd + ". Can not init this bean completely.");
                }

                Object value = null;
                if (!overwrittenOldValue) {
                    Method mGet = pd.getReadMethod();
                    if (mGet != null) {
                        // 调用getter方法获取属性值
                        value = mGet.invoke(standardBean);
                    }
                }

                // 不重载旧值
                if (value != null && !overwrittenOldValue) {
                    continue;
                }

                String type = pd.getPropertyType().toString();
                if (type.equals("class java.lang.String")) {
                    mSet.invoke(standardBean, "");
                }
                else if (type.equals("class java.lang.Integer")) {
                    mSet.invoke(standardBean, new Integer(0));
                }
                else if (type.equals("class java.lang.Byte")) {
                    mSet.invoke(standardBean, (byte) 0);
                }
                else if (type.equals("class java.lang.Boolean")) {
                    mSet.invoke(standardBean, false);
                }
                else if (type.equals("class java.util.Date")) {
                    mSet.invoke(standardBean, new Date(0));
                }
                else if (type.equals("class java.math.BigDecimal")) {
                    mSet.invoke(standardBean, new BigDecimal(0));
                }
                else if (type.equals("class java.lang.Float")) {
                    mSet.invoke(standardBean, new Float(0.0));
                }
                else if (type.equals("class java.lang.Double")) {
                    mSet.invoke(standardBean, new Double(0.0));
                }
                else if (type.equals("class java.lang.Short")) {
                    mSet.invoke(standardBean, (short) 0);
                }
            }
        } catch (Exception e) {
            throw new NestedRuntimeException(e);
        }
    }
    
    
    /**
     * 判断是否相等，允许为null，两个都为null会返回true
     * Consequently, if both arguments are null, 
     * true is returned and if exactly one argument is null, false is returned.
     * @deprecated this method has been replaced by 
     * {@code java.util.Objects.equals(Object, Object)} in Java 7 and will
     * be removed from future releases.
     */
    public static boolean safeEqual(Object obj1, Object obj2) {
        if (obj1 != null) {
            return obj1.equals(obj2);
        }
        if (obj2 == null) {
            return true;
        }
        return true;
    }
    
    /**
     * Determine whether the given object is empty.
     * <p>This method supports the following object types.
     * <ul>
     * <li>{@code Array}: considered empty if its length is zero</li>
     * <li>{@link CharSequence}: considered empty if its length is zero</li>
     * <li>{@link Collection}: delegates to {@link Collection#isEmpty()}</li>
     * <li>{@link Map}: delegates to {@link Map#isEmpty()}</li>
     * </ul>
     * <p>If the given object is non-null and not one of the aforementioned
     * supported types, this method returns {@code false}.
     * @param obj the object to check
     * @return {@code true} if the object is {@code null} or <em>empty</em>
     * @since 4.2
     * @see ObjectUtils#isEmpty(Object[])
     * @see StringUtils#hasLength(CharSequence)
     * @see StringUtils#isEmpty(Object)
     * @see CollectionUtils#isEmpty(java.util.Collection)
     * @see CollectionUtils#isEmpty(java.util.Map)
     */
    @SuppressWarnings("rawtypes")
    public static boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        }

        if (obj instanceof CharSequence) {
            return ((CharSequence) obj).length() == 0;
        }
        if (obj.getClass().isArray()) {
            return Array.getLength(obj) == 0;
        }
        if (obj instanceof Collection) {
            return ((Collection) obj).isEmpty();
        }
        if (obj instanceof Map) {
            return ((Map) obj).isEmpty();
        }

        // else
        return false;
    }
    
    public static String nullSafeToString(Object obj) {
        if (obj == null) {
            return Const.STRING_NULL;
        }
        if (obj instanceof String) {
            return (String) obj;
        }
        if (obj instanceof Object[]) {
            return Arrays.toString((Object[]) obj);
        }
        if (obj instanceof boolean[]) {
            return Arrays.toString((boolean[]) obj);
        }
        if (obj instanceof byte[]) {
            return Arrays.toString((byte[]) obj);
        }
        if (obj instanceof char[]) {
            return Arrays.toString((char[]) obj);
        }
        if (obj instanceof double[]) {
            return nullSafeToString((double[]) obj);
        }
        if (obj instanceof float[]) {
            return Arrays.toString((float[]) obj);
        }
        if (obj instanceof int[]) {
            return Arrays.toString((int[]) obj);
        }
        if (obj instanceof long[]) {
            return Arrays.toString((long[]) obj);
        }
        if (obj instanceof short[]) {
            return Arrays.toString((short[]) obj);
        }
        String str = obj.toString();
        return (str != null ? str : Const.STRING_LEN0);
    }
    
}
