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

/**
 * Bean工厂类，存放bean的实例和基本信息
 * 
 * @author zollty
 * @since 2013-06-27
 */
public interface FactoryBean<T> {

    /**
     * @return bean实例对象
     */
    T getObject();

    /**
     * @return bean对象的 Class Type
     */
    Class<?> getType();

    /**
     * @return bean对象的 id
     */
    String getId();

    /**
     * @return bean 是否为单实例
     */
    boolean isSingleton();
}
