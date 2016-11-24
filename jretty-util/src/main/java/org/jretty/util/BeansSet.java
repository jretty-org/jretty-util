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
 * Create by ZollTy on 2013-6-27 (http://blog.zollty.com/, zollty@163.com)
 */
package org.jretty.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * OPGA设计模式(One Parameter Go Anywhere)中的参数Bean容器<br>
 * 用法和Map类似，任何类型的对象都可以add进来，然后通过get(id)方法获取<br>
 * <br>
 * 注意：BeansSet设计为线程私有的变量，故不支持线程同步。(放弃对线程同步的支持，以提高效率)
 * @author zollty 
 * @since 2013-06-27
 */
public class BeansSet {

    private Map<String, ObjectBean<?>> objects = new HashMap<String, ObjectBean<?>>();

    public BeansSet() {
    }

    public BeansSet(ObjectBean<?>... objs) {
        for (ObjectBean<?> obj : objs) {
            if (StringUtils.isNotEmpty(obj.getId())) {
                objects.put(obj.getId(), obj);
            }
        }
    }

    /**
     * 向Set容器中添加一个bean对象，注意bean的id不能重复
     * 
     * @return return true if add success
     */
    public boolean add(ObjectBean<?> obj) {
        String id = obj.getId();
        if (StringUtils.isNullOrEmpty(id)) {
            return false;
        }
        if (objects.containsKey(id)) {
            return false;
        }
        objects.put(id, obj);
        return true;
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
     * 获取指定id的bean对象，同时把它从Set容器中移除
     */
    @SuppressWarnings("unchecked")
    public <T> T poll(String id) {
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
        if (objects.containsKey(id)) {
            objects.remove(id);
            return true;
        }
        return false;
    }

    /**
     * 替换或者新增一个bean
     */
    public <T> T replaceOrAdd(String id, T newObj) {
        if (StringUtils.isNullOrEmpty(id))
            return null;
        ObjectBean<T> obj = new ObjectBean<T>(id, newObj);
        objects.put(id, obj);
        return newObj;
    }

    public void clear() {
        objects.clear();
    }

    public boolean contains(ObjectBean<?> obj) {
        if (objects.containsKey(obj.getId())) {
            return true;
        }
        return false;
    }

    public boolean contains(String id) {
        if (objects.containsKey(id)) {
            return true;
        }
        return false;
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
        public <T> T next() { // nextObj
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
