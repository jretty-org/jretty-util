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
 * Create by ZollTy on 2014-6-11 (http://blog.zollty.com/, zollty@163.com)
 */
package org.jretty.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author zollty
 * @since 2014-6-11
 */
public class ObjectUtils {

    /**
     * 判断是否相等，允许为null，两个都为null会返回true
     * Consequently, if both arguments are null, true is returned and if exactly one argument is null, false is returned.
     * @deprecated this method has been replaced by {@code java.util.Objects.equals(Object, Object)} in Java 7 and will
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
     * 将参数Object数组，转换成字符串显示，仅供“显示”用途，比如记录sql参数。
     */
    public static String arrayToString(Object[] params) {
        if (params == null) {
            return "null";
        }
        StringBuilder buff = new StringBuilder();
        buff.append("{");
        boolean first = true;
        for (Object obj : params) {
            if (first) {
                first = false;
            }
            else {
                buff.append(",");
            }

            if (obj == null) {
                buff.append("null");
            }
            else {
                if (obj instanceof Date) {
                    buff.append("[" + DateFormatUtils.format_yyyy_MM_dd((Date) obj) + "]");
                }
                else {
                    buff.append("[" + obj.toString() + "]");
                }
            }
        }
        buff.append("}");
        return buff.toString();
    }
    
    
    /**
     * 给对象赋初始值<br>
     * （该方法等价于{@code initValue(Object, true, false)}，也就是说会覆盖旧值，而且bean要是标准的、属性能完全被setter方法赋值）
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
    public static void initValue(Object standardBean, boolean overwrittenOldValue, boolean allowIncompleteInit) {
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
                        value = mGet.invoke(standardBean); // 调用getter方法获取属性值
                    }
                }

                if (value != null && !overwrittenOldValue) { // 不重载旧值
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
    
}
