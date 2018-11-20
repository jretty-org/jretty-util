/* 
 * Copyright (C) 2016-2018 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License").
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * Create by ZollTy on 2016-9-14 (http://blog.zollty.com/, zollty@163.com)
 */
package org.jretty.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Map计数器，统计添加的String key的个数<br>
 * 
 * 利用MutableInteger实现，参见{@link MutableInteger}
 * 
 * @author zollty
 * @since 2016-09-14
 */
public class MapCounter {

    private Map<String, MutableInteger> map = new HashMap<String, MutableInteger>();

    public void add(String key) {
        MutableInteger tmp = map.get(key);
        if (tmp == null) {
            map.put(key, new MutableInteger(1));
        } else {
            tmp.getAndIncrement();
        }
    }
    
    public void add(String key, int delta) {
        MutableInteger tmp = map.get(key);
        if (tmp == null) {
            map.put(key, new MutableInteger(delta));
        } else {
            tmp.getAndAdd(delta);
        }
    }

    public Map<String, Integer> toNormalMap() {
        Map<String, Integer> ret = new HashMap<String, Integer>(map.size());
        for (Map.Entry<String, MutableInteger> entry : map.entrySet()) {
            ret.put(entry.getKey(), entry.getValue().get());
        }
        return ret;
    }

    public Map<String, MutableInteger> getMap() {
        return map;
    }
    
    public int size() {
        return map.size();
    }
    
    public void clear() {
        map.clear();
    }

    @Override
    public String toString() {
        return toNormalMap().toString();
    }

}