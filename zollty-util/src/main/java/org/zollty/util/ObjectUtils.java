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
package org.zollty.util;

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

}
