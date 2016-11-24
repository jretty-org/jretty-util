/* 
 * Copyright (C) 2014-2015 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License").
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * Create by ZollTy on 2015-3-17 (http://blog.zollty.com/, zollty@163.com)
 */
package org.jretty.util.support.subexception;

import org.jretty.util.NestedCheckedException;

/**
 * @author zollty
 * @since 2015-3-17
 */
public class SubNestedException extends NestedCheckedException {

    private static final long serialVersionUID = -4736985020926462922L;
    
    public static final String EXCEPTION_PRIFIX = "org.zollty.SubNestedException: ";

    public SubNestedException(Throwable e) {
        super(e);
        this.getDelegate().setExceptionPrefix(EXCEPTION_PRIFIX);
    }

    /**
     * @param e
     * @param message 自定义错误信息
     * @param args 占位符参数--[ 变长参数，用于替换message字符串里面的占位符"{}" ]
     */
    public SubNestedException(Throwable e, String message, String... args) {
        super(e, message, args);
        this.getDelegate().setExceptionPrefix(EXCEPTION_PRIFIX);
    }

    /**
     * @param message 自定义错误信息
     * @param args 占位符参数--[ 变长参数，用于替换message字符串里面的占位符"{}" ]
     */
    public SubNestedException(String message, String... args) {
        super(message, args);
        this.getDelegate().setExceptionPrefix(EXCEPTION_PRIFIX);
    }

}
