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
 * 最基础的Checked异常类，所有其他的Checked异常都继承于本类
 * 
 * @author zollty 
 * @since 2013-6-27
 */
public class BasicCheckedException extends Exception {

    private static final long serialVersionUID = 6575977961100372966L;

    public BasicCheckedException() {
        super();
    }
    
    /**
     * @param message 支持占位符{}，例如 "error, userID={}, opt={}."
     * @param args 对应message占位符{}的值
     */
    public BasicCheckedException(String message, String... args) {
        super(StringUtils.replaceParams(message, args));
    }

    public BasicCheckedException(Throwable arg0) {
        super(arg0);
    }

    /**
     * @param e 原始异常
     * @param message 支持占位符{}，例如 "error, userID={}, opt={}."
     * @param args 对应message占位符{}的值
     */
    public BasicCheckedException(Throwable e, String message, String... args) {
        super(StringUtils.replaceParams(message, args), e);
    }

}