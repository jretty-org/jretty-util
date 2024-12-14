/* 
 * Copyright (C) 2013-2024 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License").
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * Create by ZollTy on 2013-6-27 (http://blog.zollty.com/, zollty@163.com)
 */
package org.jretty.util.bean;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import org.jretty.util.StringUtils;

/**
 * OPGA设计模式(One Parameter Go Anywhere)中的参数Bean容器<br>
 * 用法和Map类似，任何类型的对象都可以add进来，然后通过get(id)方法获取<br>
 * <br>
 * 注意：BeansSet设计为线程私有的变量，故不支持线程同步。(放弃对线程同步的支持，以提高效率)
 * @author zollty 
 * @since 2013-06-27
 */
public class BeansSet {

    private LinkedHashMap<String, ObjectBean<?>> objects = new LinkedHashMap<String, ObjectBean<?>>();
    private boolean arrayMode = false;

    public BeansSet() {
    }
    
    public BeansSet(boolean arrayMode) {
        this.arrayMode = arrayMode;
    }

    public BeansSet(ObjectBean<?>... objs) {
        this.arrayMode = true;
        for (ObjectBean<?> obj : objs) {
            if (StringUtils.isNotEmpty(obj.getId())) {
                objects.put(obj.getId(), obj);
            }
        }
    }

    /**
     * 替换或者新增一个bean
     */
    public <T> T add(String id, T newObj) {
        if (StringUtils.isNullOrEmpty(id)) {
            return null;
        }
        ObjectBean<T> obj = new ObjectBean<T>(id, newObj);
        objects.put(id, obj);
        return newObj;
    }

    /**
     * 获取指定id的bean对象
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String id) {
        ObjectBean<?> ob = objects.get(id);
        if (null == ob) {
            return null;
        }
        return (T) ob.getObject();
    }
    
    /**
     * 替换或者新增一个bean，key为添加序号（从0自增）
     */
    public <T> T add(T newObj) {
        this.arrayMode = true;
        int i = objects.size();
        String id = String.valueOf(i);
        ObjectBean<T> obj = new ObjectBean<T>(id, newObj);
        objects.put(id, obj);
        return newObj;
    }
    
    /**
     * 根据序号获取
     */
    @SuppressWarnings("unchecked")
    public <T> T get(int index) {
        Iterator<Entry<String, ObjectBean<?>>> iterator = objects.entrySet().iterator();
        int i = 0;
        while (iterator.hasNext()) {
            if (i++ == index) {
                return (T) iterator.next().getValue().getObject();
            }
            iterator.next();
        }
        return null;
    }

    /**
     * 获取指定id的bean对象，同时把它从Set容器中移除
     */
    @SuppressWarnings("unchecked")
    public <T> T poll(String id) {
        if (arrayMode) {
            // warning 不运行
            return get(id);
        }
        ObjectBean<?> ob = objects.remove(id);
        if (null == ob) {
            return null;
        }
        return (T) ob.getObject();
    }

    /**
     * 从Set容器中移除指定id的bean对象
     */
    public boolean remove(String id) {
        if (arrayMode) {
            // warning 不运行
            return false;
        }
        if (objects.containsKey(id)) {
            objects.remove(id);
            return true;
        }
        return false;
    }

    public void clear() {
        objects.clear();
    }

    public boolean contains(ObjectBean<?> obj) {
        return objects.containsKey(obj.getId());
    }

    public boolean contains(String id) {
        return objects.containsKey(id);
    }

    /**
     * 支持迭代
     */
    public BeansSetItertor iterator() {
        return new BeansSetItertor();
    }

    public boolean isEmpty() {
        return objects.isEmpty();
    }

    public int size() {
        return objects.size();
    }

    public String[] getBeanNames() {
        String[] array = new String[objects.size()];
        Iterator<Entry<String, ObjectBean<?>>> iterator = objects.entrySet().iterator();
        int i = 0;
        while (iterator.hasNext()) {
            array[i++] = iterator.next().getValue().getId();
        }
        return array;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Iterator<Entry<String, ObjectBean<?>>> iterator = objects.entrySet().iterator();
        while (iterator.hasNext()) {
            sb.append(iterator.next().getValue().getId()).append(", ");
        }
        return sb.toString();
    }
    
    /**
     * @return 是否为数组模式
     */
    public boolean isArrayMode() {
        return arrayMode;
    }

    /**
     * @param arrayMode 是否为数组模式
     */
    public void setArrayMode(boolean arrayMode) {
        this.arrayMode = arrayMode;
    }

    // private Object[] lock = new Object[0]; // 放弃对线程同步的支持，以提高效率。

    public class BeansSetItertor {

        private Iterator<Entry<String, ObjectBean<?>>> iterator = objects.entrySet().iterator();

        public boolean hasNext() {
            // synchronized (lock) {
            return iterator.hasNext();
            // }
        }

        // public ObjectBean<?> next() {
        // if(iterator.hasNext()){
        // return iterator.next().getValue();
        // }
        // return null;
        // }

        @SuppressWarnings("unchecked")
        public <T> T next() {
            if (iterator.hasNext()) {
                return (T) iterator.next().getValue().getObject();
            }
            return null;
        }

        // @Override
        // public void remove() {
        // throw new RuntimeException("remove operation is forbidden, " +
        // "you may use the poll() method of BeansSet to do this! ");
        // }
    }

}
