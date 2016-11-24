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
 * Create by ZollTy on 2014-5-17 (http://blog.zollty.com/, zollty@163.com)
 */
package org.jretty.util.json;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import org.jretty.util.json.support.ObjectWriter;
import org.jretty.util.json.support.StringWriter;

/**
 * Simple JSON工具类
 * @author zollty
 * @since 2015-5-17
 */
public class JSONUtils {
    
    public static JSONParser getJSONParser(String jsonStr) {
        return new JSONParserDirector(jsonStr);
    }

    public static JSONWriter getJSONWriter() {
        return new JSONWriterDirector();
    }

    public static JSONWriter getJSONWriter(boolean htmlSafe) {
        return new JSONWriterDirector(String.class, new StringWriter(htmlSafe));
    }

    @SuppressWarnings("rawtypes")
    public static JSONWriter getJSONWriter(ObjectWriter... objWriters) {
        if (objWriters == null || objWriters.length == 0) {
            return new JSONWriterDirector();
        }
        else if (objWriters.length == 1) {
            return new JSONWriterDirector(getObjectWriterActualType(objWriters[0]), objWriters[0]);
        }
        else {
            Map<Class, ObjectWriter> map = new HashMap<Class, ObjectWriter>();
            for (ObjectWriter ow : objWriters) {
                map.put(getObjectWriterActualType(ow), ow);
            }
            return new JSONWriterDirector(map);
        }
    }

    @SuppressWarnings("rawtypes")
    public static JSONWriter getJSONWriter(Map<Class, ObjectWriter> map) {
        return new JSONWriterDirector(map);
    }

    public static String toJSONString(Object o) {
        return new JSONWriterDirector().writeObject(o).toString();
    }

    public static String toJSONString(Object o, boolean htmlSafe) {
        return new JSONWriterDirector(String.class, new StringWriter(htmlSafe)).writeObject(o).toString();
    }

    @SuppressWarnings("rawtypes")
    public static String toJSONString(Object o, ObjectWriter... objWriters) {
        return getJSONWriter(objWriters).writeObject(o).toString();
    }
    
    @SuppressWarnings("rawtypes")
    public static String toJSONString(Object o, Map<Class, ObjectWriter> map) {
        return new JSONWriterDirector(map).writeObject(o).toString();
    }

    public static Object parse(String jsonStr) {
        JSONParser parser = new JSONParserDirector(jsonStr);
        return parser.parse();
    }
    

    // ~ helper methods --------

    @SuppressWarnings("rawtypes")
    private static Class getObjectWriterActualType(ObjectWriter ow) {
        Class ls = ow.getClass();
        Type tp = getRawType(ls);
        if (tp instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) tp;
            return (Class) pt.getActualTypeArguments()[0];
        }
        else {
            return Object.class;
        }
    }

    @SuppressWarnings("rawtypes")
    private static Type getRawType(Class ls) {
        Type[] ty = ls.getGenericInterfaces(); // 获取其接口，如果有则判断是否为ObjectWriter
        for (Type tp : ty) {
            if (tp instanceof ParameterizedType) {
                ParameterizedType pt = (ParameterizedType) tp;
                if (pt.getRawType() == ObjectWriter.class) {
                    return pt;
                }
            }
        }

        // 如果本类没有 ObjectWriter 接口，则递归调用其父类
        Type type = ls.getGenericSuperclass();
        if (type instanceof Class) {
            Class cl = (Class) type;
            return getRawType(cl);
        }

        return null;
    }
}
