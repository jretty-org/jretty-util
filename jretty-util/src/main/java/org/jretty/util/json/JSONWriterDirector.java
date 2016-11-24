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

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.management.openmbean.CompositeData;
import javax.management.openmbean.TabularData;

import org.jretty.util.json.support.DateWriter;
import org.jretty.util.json.support.ExceptionWriter;
import org.jretty.util.json.support.ObjectWriter;
import org.jretty.util.json.support.StringWriter;

/**
 * 
 * @author zollty
 * @since 2014-5-17
 */
class JSONWriterDirector implements DefaultWriter {

    private StringBuilder out;

    @SuppressWarnings("rawtypes")
    private Map<Class, ObjectWriter> ObjWriterMap = null;

    @SuppressWarnings("rawtypes")
    public JSONWriterDirector() {
        this.out = new StringBuilder();
        ObjWriterMap = new HashMap<Class, ObjectWriter>();
        ObjWriterMap.put(Date.class, new DateWriter());
        ObjWriterMap.put(String.class, new StringWriter());
        ObjWriterMap.put(Throwable.class, new ExceptionWriter());
    }

    @SuppressWarnings("rawtypes")
    public JSONWriterDirector(Map<Class, ObjectWriter> map) {
        this();
        ObjWriterMap.putAll(map);
    }

    @SuppressWarnings("rawtypes")
    public JSONWriterDirector(Class clazz, ObjectWriter owriter) {
        this();
        ObjWriterMap.put(clazz, owriter);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Object writeObject(Object o) {
        if (o == null) {
            writeNull();
            return this;
        }

        if (o instanceof CharSequence) {
            writeString(o.toString());
            return this;
        }

        if (o instanceof Number || (o instanceof Boolean)) {
            write(o.toString());
            return this;
        }

        if (o instanceof Date) {
            writeDate((Date) o);
            return this;
        }

        if (o instanceof int[]) {
            int[] array = (int[]) o;
            write('[');
            for (int i = 0; i < array.length; ++i) {
                if (i != 0) {
                    write(',');
                }
                write(array[i]);
            }
            write(']');
            return this;
        }

        if (o instanceof long[]) {
            long[] array = (long[]) o;
            write('[');
            for (int i = 0; i < array.length; ++i) {
                if (i != 0) {
                    write(',');
                }
                write(array[i]);
            }
            write(']');
            return this;
        }

        if (o instanceof Collection) {
            writeCollection((Collection) o);
            return this;
        }

        if (o instanceof Map) {
            writeMap((Map) o);
            return this;
        }

        if (o.getClass().isArray()) {
            writeArray((Object[]) o);
            return this;
        }

        if (o.getClass() == SimpleJSON.class || o.getClass() == SimpleJSONArray.class) {
            write(o.toString());
            return this;
        }

        if (o instanceof Throwable) {
            writeError((Throwable) o);
            return this;
        }

        if (o instanceof TabularData) {
            writeTabularData((TabularData) o);
            return this;
        }

        if (o instanceof CompositeData) {
            writeCompositeData((CompositeData) o);
            return this;
        }

        if (o instanceof Class) {
            writeSimpleString(((Class) o).getName());
            return this;
        }

        ObjectWriter objWriter = null;
        if ((objWriter = ObjWriterMap.get(o.getClass())) != null) {
            objWriter.write(this, o);
            return this;
        }

        // Other Object type
        writeString(o.toString());

        // throw new IllegalArgumentException("not support type : " + o.getClass());
        return this;
    }

    public Object writeArray(Object[] array) {
        if (array == null) {
            writeNull();
            return this;
        }

        write('[');
        boolean first = true;
        for (int i = 0; i < array.length; ++i) {
            if (!first) {
                write(',');
            }
            else {
                first = false;
            }
            writeObject(array[i]);
        }

        write(']');
        return this;
    }

    public Object writeCollection(Collection<Object> list) {
        if (list == null) {
            writeNull();
            return this;
        }

        write('[');
        boolean first = true;
        for (Object entry : list) {
            if (!first) {
                write(',');
            }
            else {
                first = false;
            }
            writeObject(entry);
        }

        write(']');
        return this;
    }

    public Object writeMap(Map<String, Object> map) {
        if (map == null) {
            writeNull();
            return this;
        }

        write('{');
        boolean first = true;
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (!first) {
                write(',');
            }
            else {
                first = false;
            }

            writeString(entry.getKey());
            write(':');
            writeObject(entry.getValue());
        }

        write('}');
        return this;
    }

    @SuppressWarnings("unchecked")
    public void writeString(String text) {
        if (text == null) {
            writeNull();
            return;
        }
        ObjWriterMap.get(String.class).write(this, text);
    }

    @SuppressWarnings("unchecked")
    public void writeDate(Date date) {
        if (date == null) {
            writeNull();
            return;
        }
        ObjWriterMap.get(Date.class).write(this, date);
    }

    @SuppressWarnings("unchecked")
    public Object writeError(Throwable error) {
        if (error == null) {
            writeNull();
            return this;
        }
        ObjWriterMap.get(Throwable.class).write(this, error);
        return this;
    }

    public Object writeTabularData(TabularData tabularData) {
        if (tabularData == null) {
            writeNull();
            return this;
        }

        int entryIndex = 0;
        write('[');

        for (Object item : tabularData.values()) {
            if (entryIndex != 0) {
                write(',');
            }
            CompositeData row = (CompositeData) item;
            writeCompositeData(row);

            entryIndex++;
        }
        write(']');
        return this;
    }

    public Object writeCompositeData(CompositeData compositeData) {
        if (compositeData == null) {
            writeNull();
            return this;
        }

        int entryIndex = 0;
        write('{');

        for (Object key : compositeData.getCompositeType().keySet()) {
            if (entryIndex != 0) {
                write(',');
            }
            writeString((String) key);
            write(':');
            Object value = compositeData.get((String) key);
            writeObject(value);
            entryIndex++;
        }

        write('}');
        return this;
    }

    public void writeNull() {
        out.append("null");
    }

    public void writeSimpleString(CharSequence text) {
        if (text == null) {
            writeNull();
            return;
        }
        out.append("\"").append(text).append("\"");
    }

    public void write(CharSequence text) {
        out.append(text);
    }

    public void write(char[] value, int offset, int count) {
        out.append(String.valueOf(value, offset, count));
    }

    public void write(char c) {
        out.append(c);
    }

    public void write(int c) {
        out.append(c);
    }

    public void write(long c) {
        out.append(c);
    }

    public String toString() {
        return out.toString();
    }

}