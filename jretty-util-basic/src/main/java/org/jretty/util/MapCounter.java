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
 * 利用MutableInteger实现，参见{@link MutableInteger}<br>
 * 
 * 注意，此为非线程安全的实现，不支持并发。可以将 add() 方法synchronized来支持线程安全。
 * 
 * @author zollty
 * @since 2016-09-14
 */
public class MapCounter<T> {

    private Map<T, MutableInteger> map = new HashMap<T, MutableInteger>();

    public int add(T key) {
        MutableInteger tmp = map.get(key);
        if (tmp == null) {
            map.put(key, new MutableInteger(1));
            return 1;
        } else {
            return tmp.getAndIncrement();
        }
    }
    
    public int add(T key, int delta) {
        MutableInteger tmp = map.get(key);
        if (tmp == null) {
            map.put(key, new MutableInteger(delta));
            return delta;
        } else {
            return tmp.getAndAdd(delta);
        }
    }
    
    public void remove(T key) {
        map.remove(key);
    }
    
    public int size() {
        return map.size();
    }
    
    public void clear() {
        map.clear();
    }

    public Map<T, MutableInteger> getMap() {
        return map;
    }

    public int getValue(T key) {
        return map.get(key).get();
    }

    /**
     * 转换成Map&ltString, Integer&gt
     */
    public Map<T, Integer> toNormalMap() {
        Map<T, Integer> ret = new HashMap<T, Integer>(map.size());
        for (Map.Entry<T, MutableInteger> entry : map.entrySet()) {
            ret.put(entry.getKey(), entry.getValue().get());
        }
        return ret;
    }
    
    @Override
    public String toString() {
        return toNormalMap().toString();
    }

}
