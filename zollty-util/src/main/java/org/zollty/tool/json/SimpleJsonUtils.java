/* 
 * Copyright (C) 2013-2015 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License").
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * Create by ZollTy on 2013-12-10 (http://blog.zollty.com/, zollty@163.com)
 */
package org.zollty.tool.json;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author zollty
 * @since 2013-12-10
 * @deprecated use JSONUtils replace
 */
@Deprecated
public class SimpleJsonUtils {

    private SimpleJsonUtils() {
    }

    /**
     * json 只能是类似：{"resultType": "000", "errorCode": "0x0001"}的简单格式。
     * <p>
     * 即，只能有最外层一个{}，值的类型全部都是string，不能有内部对象和数组。
     * 
     * @param json
     * @return Map&ltString, String&gt
     */
    public static Map<String, String> simpleJsonToMap(String json) {
        Map<String, String> map = new HashMap<String, String>();
        char[] chars = json.toCharArray();
        int a = -1;
        int b;
        String key = null;
        String value = null;
        for (int i = 1; i < chars.length - 1; i++) {
            if (chars[i] == '\"' && chars[i - 1] != '\\') {
                if (a == -1) {
                    a = i;
                }
                else {
                    b = i;
                    if (key == null) {
                        key = json.substring(a + 1, b);
                    }
                    else {
                        value = json.substring(a + 1, b).replace("\\\"", "\"");
                        map.put(key, value);
                        key = null;
                    }
                    a = -1;
                }
                continue;
            }
        }
        return map;
    }

    /**
     * 组装成的简单json格式为：{"resultType": "000", "errorCode": "0x0001"}， 
     * （全部都用双引号包裹） 且默认Map<String, String>的string全都不包含特殊字符
     * 
     * @see #simpleMapToJson(String)
     * @author zollty
     */
    public static String simpleMapToJson(Map<String, String> map) {
        StringBuilder strb = new StringBuilder();
        strb.append("{");
        Iterator<Map.Entry<String, String>> entryIter = map.entrySet().iterator();
        Map.Entry<String, String> entry;
        boolean first = true;
        while (entryIter.hasNext()) {
            if (!first) {
                strb.append(", ");
            }
            else {
                first = false;
            }
            entry = entryIter.next();
            strb.append("\"").append(entry.getKey()).append("\": ").append("\"").append(entry.getValue()).append("\"");
        }
        strb.append("}");

        return strb.toString();
    }

    /**
     * @see #simpleJsonToMap(String)
     * @author zollty
     */
    public static String mapToJson(Map<?, ?> map) {
        StringBuilder strb = new StringBuilder();
        strb.append("{");
        Iterator<?> entryIter = map.entrySet().iterator();
        Map.Entry<?, ?> entry;
        Object value;
        // boolean first = true;
        while (entryIter.hasNext()) {
            // if (!first) {
            // strb.append(", ");
            // }
            // else {
            // first = false;
            // }

            entry = (Entry<?, ?>) entryIter.next();
            strb.append("\"").append(entry.getKey().toString()).append("\": ");
            value = entry.getValue();
            if (value == null) {
                strb.append("null");
            }
            else if (Integer.class == value.getClass()) {
                strb.append(value.toString());
            }
            else if (Long.class == value.getClass()) {
                strb.append(value.toString());
            }
            else if (Double.class == value.getClass()) {
                strb.append(value.toString());
            }
            else if (Boolean.class == value.getClass()) {
                strb.append(value.toString());
            }
            else {
                strb.append("\"").append(value.toString()).append("\"");
            }

            if (entryIter.hasNext()) {
                strb.append(", ");
            }
        }
        strb.append("}");
        return strb.toString();
    }

    /**
     * 将List转化成json字符串 [{APPID:XXX,ORGID:XXX},{},{}]
     * 
     * @return
     */
    public static String SimpleListToJson(List<?> list) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < list.size(); i++) {
            sb.append("{");
            Object obj = list.get(i);
            Field[] fields = obj.getClass().getDeclaredFields();
            for (Field field : fields) {
                // 如果不为空，设置可见性，然后返回
                field.setAccessible(true);

                try {
                    // 设置字段可见，即可用get方法获取属性值。
                    sb.append("\"" + field.getName() + "\":\"" + field.get(obj) + "\",");
                }
                catch (Exception e) {
                    // System.out.println("error--------"+methodName+".Reason is:"+e.getMessage());
                }
            }
            sb.deleteCharAt(sb.length() - 1);
            sb.append("}");
            if (i != list.size() - 1) {
                sb.append(",");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    /**
     * 特殊字符转义 1、json标准中，value和name不能包含引号和斜杠
     * <p>
     * 例如，value = a"b\c\d，替换后 为 a\"b\\c\\d 2、将java的特殊字符转换成字符串，比如字符(\n)转换成字符串("\n")
     */
    public static String escapeValueOrName(String valueOrName) {
        if (valueOrName == null) {
            return valueOrName;
        }
        String temp = valueOrName;
        temp = temp.replace("\\", "\\\\");
        temp = temp.replace("\"", "\\\"");
        temp = temp.replace("\n", "\\n");
        temp = temp.replace("\r", "\\r");
        temp = temp.replace("\t", "\\t");
        temp = temp.replace("\b", "\\b");
        temp = temp.replace("\f", "\\f");
        return temp;
    }

}