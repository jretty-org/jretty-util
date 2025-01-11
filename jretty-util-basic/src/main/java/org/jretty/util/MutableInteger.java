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

/**
 * 可变的整数，API同AtomicInteger，但非线程安全，效率比AtomicInteger高得多。
 *
 * @author zollty
 * @since 2016-09-14
 */
public class MutableInteger {

    private int value;

    public MutableInteger() {
    }

    public MutableInteger(int n) {
        value = n;
    }

    /**
     * 获取当前的值
     */
    public final int get() {
        return value;
    }

    /**
     * 取当前的值，并设置新的值
     *
     * @return 设置前的值
     */
    public final int getAndSet(int newValue) {
        int ret = value;
        value = newValue;
        return ret;
    }

    /**
     * 获取当前的值，并自增
     *
     * @return 自增后的值
     */
    public final int getAndIncrement() {
        return value++;
    }

    /**
     * 获取当前的值，并自减
     *
     * @return 递减后的值
     */
    public final int getAndDecrement() {
        return value--;
    }

    /**
     * 重置为n
     */
    public final void reset(int n) {
        value = n;
    }

    /**
     * 获取当前的值，并加上预期的值
     *
     * @param delta 增加的值
     * @return 增加之前的值
     */
    public final int getAndAdd(int delta) {
        int ret = value;
        value += delta;
        return ret;
    }

}
