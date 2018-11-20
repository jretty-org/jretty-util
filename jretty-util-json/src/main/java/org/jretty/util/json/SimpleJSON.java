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
 * Create by ZollTy on 2014-5-07 (http://blog.zollty.com/, zollty@163.com)
 */
package org.jretty.util.json;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author zollty
 * @since 2014-5-7
 */
public class SimpleJSON {

    private Map<String, Object> map;

    public static SimpleJSON getInstance() {
        return new SimpleJSON();
    }

    public SimpleJSON() {
        map = new LinkedHashMap<String, Object>();
    }

    public Map<String, Object> getMap() {
        return map;
    }

    public SimpleJSON addItem(String key, Object value) {
        if (key == null) {
            throw new IllegalArgumentException("key can't be null");
        }
        if (value != null) {
            map.put(key, value);
        }
        else {
            map.put(key, "");
        }
        return this;
    }

    public static SimpleJSONArray toSimpleJSONArray(Collection<SimpleJSON> list) {
        StringBuilder sbr = new StringBuilder();
        if (list == null) {
            // return new SimpleJSONArray("\"null\"");
            return new SimpleJSONArray("null");
        }
        sbr.append('[');
        boolean first = true;
        for (SimpleJSON entry : list) {
            if (!first) {
                sbr.append(',');
            }
            else {
                first = false;
            }
            sbr.append(entry.toString());
        }
        sbr.append(']');
        return new SimpleJSONArray(sbr.toString());
    }

    @Override
    public String toString() {
        // return toJsonString();
        return JSONUtils.toJSONString(map);
    }
}
