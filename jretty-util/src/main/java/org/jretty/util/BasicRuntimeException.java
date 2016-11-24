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
 * 最基础的Runtime异常类，所有其他的Runtime异常都继承于本类
 * 
 * @author zollty 
 * @since 2013-6-27
 */
public class BasicRuntimeException extends RuntimeException {

    private static final long serialVersionUID = 4668460935887569748L;

    public BasicRuntimeException() {
        super();
    }

    /**
     * @param message 支持占位符{}，例如 "error, userID={}, opt={}."
     * @param args 对应message占位符{}的值
     */
    public BasicRuntimeException(String message, String... args) {
        super(StringUtils.replaceParams(message, args));
    }

    public BasicRuntimeException(Throwable e) {
        super(e);
    }

    /**
     * @param e 原始异常
     * @param message 支持占位符{}，例如 "error, userID={}, opt={}."
     * @param args 对应message占位符{}的值
     */
    public BasicRuntimeException(Throwable e, String message, String... args) {
        super(StringUtils.replaceParams(message, args), e);
    }

}