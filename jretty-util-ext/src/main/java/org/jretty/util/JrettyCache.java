/* 
 * Copyright (C) 2013-2018 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License").
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * Create by ZollTy on 2018-3-6 (http://blog.zollty.com/, zollty@163.com)
 */
package org.jretty.util;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * 一种特殊的容器，其作用是，将某key & value保留一段时间，比如5秒，
 * 在这段时间内，相同的key无法再添加进来，只有key过期之后，才能重新加进来。
 * 另外，容器有容量限制，超过容量，则不再添加元素 且 putIfAbsent 返回true（相当于每次都是新元素，只是不保存，直接丢弃）
 * 
 * @see #putIfAbsent(String, Object, int)
 * 
 * @author zollty
 * @since 2018年3月6日
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class JrettyCache {

    private static int DEFAULT_CAPACITY = 2048;
    // 默认过期时间 20秒
    private static final long DEFUALT_EXPIRE_TIME = 20 * 1000;
    private final long expireTime;
    private final int limitSize;
    private final Map<Object, CacheEntry> cacheEntry;

    public JrettyCache() {
        this(DEFAULT_CAPACITY);
    }

    public JrettyCache(long expireTime) {
        this(DEFAULT_CAPACITY, expireTime);
    }

    public JrettyCache(int initialCapacity) {
        this(initialCapacity, DEFUALT_EXPIRE_TIME);
    }

    public JrettyCache(int initialCapacity, long expireTime) {
        int capacity = 1;
        while (capacity < initialCapacity) {
            capacity <<= 1;
        }
        limitSize = (int) (capacity * 0.75);
        cacheEntry = new WeakHashMap<Object, CacheEntry>(capacity);
        this.expireTime = expireTime;
    }

    /**
     * 判断容器内是否有这个item（过期的先删掉），如果没有，返回true；如果有则返回false。
     */
    public synchronized boolean putIfAbsent(Object key, Object value) {
        if (get(key) != null) {
            return false;
        }
//        System.out.println("-----------------st");
//        System.out.println(cacheEntry.get(key));
        put(key, value);
//        System.out.println(cacheEntry.get(key));
//        System.out.println(size());
//        System.out.println("-----------------end");
        return true;
    }

    /**
     * 判断容器内是否有这个item（过期的先删掉），如果没有，返回true；如果有则返回false。
     */
    public synchronized boolean putIfAbsent(Object key, Object value, long expireTimeMs) {
        if (get(key) != null) {
            return false;
        }
        put(key, value, expireTimeMs);
        return true;
    }

    public synchronized void clear() {
        cacheEntry.clear();
    }

    public synchronized int size() {
        return cacheEntry.size();
    }

    public synchronized <T> T getData(Object key) {
        CacheEntry<T> en = get(key);
        return en != null ? en.getData() : null;
    }

    protected boolean exits(Object key) {
        return get(key) != null;
    }
    
    protected <T> CacheEntry<T> get(Object key) {
        CacheEntry<T> data = cacheEntry.get(key);
        if (data == null) {
            return null;
        }
        if (data.getSaveTime() < System.currentTimeMillis()) {
            cacheEntry.remove(key);
            return null;
        }
        return data;
    }

    /**
     * 如果没超过容量，则将元素添加到容器；如果超过容量，则丢弃该元素。
     */
    protected boolean put(Object key, Object data, long expireTimeMs) {
        if (cacheEntry.size() >= limitSize) {
            clearExpireData();
        }
        if (cacheEntry.size() >= limitSize) {
            return false;
        }
        cacheEntry.put(key, new CacheEntry(data, expireTimeMs));
        return true;
    }

    protected boolean put(Object key, Object data) {
        if (cacheEntry.size() >= limitSize) {
            clearExpireData();
        }
        if (cacheEntry.size() >= limitSize) {
            return false;
        }
        cacheEntry.put(key, new CacheEntry(data, expireTime));
        return true;
    }

    protected void clearExpireData() {
        List<Object> list = new LinkedList<Object>();
        for (Map.Entry<Object, CacheEntry> entry : cacheEntry.entrySet()) {
            if (entry.getValue().getSaveTime() < System.currentTimeMillis()) {
                list.add(entry.getKey());
            }
        }
        for (Object key : list) {
            cacheEntry.remove(key);
        }
    }

    private class CacheEntry<T> {
        private final T data;
        private final long saveTime; // 存活时间
        private final long expire; // 过期时间 小于等于0标识永久存活

        CacheEntry(T t, long expire) {
            this.data = t;
            this.expire = expire <= 0 ? 0 : expire;
            this.saveTime = System.currentTimeMillis() + this.expire;
        }

        public T getData() {
            return data;
        }

        public long getSaveTime() {
            return saveTime;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("CacheEntry [data=");
            builder.append(data);
            builder.append(", saveTime=");
            builder.append(saveTime);
            builder.append(", expire=");
            builder.append(expire);
            builder.append("]");
            return builder.toString();
        }
    }
}