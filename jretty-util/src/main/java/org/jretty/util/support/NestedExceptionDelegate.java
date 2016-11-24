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
 * Create by ZollTy on 2014-12-13 (http://blog.zollty.com/, zollty@163.com)
 */
package org.jretty.util.support;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;

import org.jretty.util.StringUtils;

/**
 * 所有 NestedException 公用的逻辑处理类
 * 
 * @author zollty
 * @version 1.0
 * @since 2014-12-13
 */
public class NestedExceptionDelegate implements Serializable {
    
    private static final long serialVersionUID = -2100639812838514912L;
    public static final String MSG_SPLIT = " |- ";
    public static final String CAUSED_BY = "\nCaused by: ";
    public static final String EXCEPTION_PRIFIX = "org.zollty.NestedException: ";
    
    private final String errorMsg;
    private final String[] params;
    private final Throwable exception;
    private String prefix;

    /**
     * @param message 自定义错误信息
     * @param args 占位符参数--[ 变长参数，用于替换message字符串里面的占位符"{}" ]
     */
    public NestedExceptionDelegate(String prefix, String message, String... args) {
        this.errorMsg = message;
        this.params = args;
        this.exception = null;
        this.prefix = prefix;
    }
    
    /**
     * @param e
     */
    public NestedExceptionDelegate(String prefix, Throwable e) {
        this.errorMsg = null;
        this.params = null;
        this.exception = e;
        this.prefix = prefix;
    }
    
    /**
     * @param e
     * @param message 自定义错误信息
     * @param args 占位符参数--[ 变长参数，用于替换message字符串里面的占位符"{}" ]
     */
    public NestedExceptionDelegate(String prefix, Throwable e, String message, String... args) {
        this.errorMsg = message;
        this.params = args;
        this.exception = e;
        this.prefix = prefix;
    }
    
    
    public String getMessage() {
        if (null == exception) {
            return StringUtils.replaceParams(errorMsg, params);
        }
        if (errorMsg == null || errorMsg.length() < 1) {
            return exception.getMessage();
        }
        return StringUtils.replaceParams(errorMsg, params) + MSG_SPLIT + exception.getMessage();
    }
    
    public String getStackTraceStr() {
        return getStackTraceStr(null);
    }

    public String getStackTraceStr(String message) {
        if (null == exception) {
            if (message != null && message.length() != 0) {
                return getExceptionName() + message + MSG_SPLIT + StringUtils.replaceParams(errorMsg, params);
            }
            return getExceptionName() + StringUtils.replaceParams(errorMsg, params);
        }
        if (ExceptionDelegateSupport.class.isInstance(exception)) {
            if (message != null && message.length() != 0) {
                return ((ExceptionDelegateSupport) exception).getDelegate().getStackTraceStr(message + MSG_SPLIT
                        + StringUtils.replaceParams(errorMsg, params));
            }
            return ((ExceptionDelegateSupport) exception).getDelegate().getStackTraceStr(StringUtils.replaceParams(errorMsg, params));
        }

        if (message != null && message.length() != 0) {
            if (errorMsg != null && errorMsg.length() != 0) {
                return message + MSG_SPLIT + StringUtils.replaceParams(errorMsg, params) + CAUSED_BY
                        + appendStackTrace(exception);
            }
            return message + CAUSED_BY + appendStackTrace(exception);
        }
        if (errorMsg != null && errorMsg.length() != 0) {
            return StringUtils.replaceParams(errorMsg, params) + CAUSED_BY + appendStackTrace(exception);
        }
        return appendStackTrace(exception);
    }

    public void printStackTrace() {
        System.err.println(getStackTraceStr());
    }

    public void printStackTrace(PrintWriter pw) {
        appendTo(pw, getStackTraceStr());
    }

    public void printStackTrace(PrintStream ps) {
        appendTo(ps, getStackTraceStr());
    }
    
    public String getExceptionName() {
        if (null == exception) {
            return getExceptionPrefix();
        }
        if (ExceptionDelegateSupport.class.isInstance(exception)) {
            return ((ExceptionDelegateSupport) exception).getDelegate().getExceptionName();
        }
        return exception.getClass().getName()+": ";
    }
    
    public Throwable getCause() {
        if (null == exception) {
            return null;
        }
        if (ExceptionDelegateSupport.class.isInstance(exception)) {
            return ((ExceptionDelegateSupport) exception).getDelegate().getCause();
        }
        return exception;
    }
    
    @Override
    public String toString() {
        String msg = getMessage();
        String name = getExceptionName();
        if (msg == null) {
            return name;
        } else {
            int length = name.length() + msg.length();
            StringBuilder buffer = new StringBuilder(length);
            return buffer.append(name).append(msg).toString();
        }
    }
    
    /**
     * 异常的前缀名 默认为"org.zollty.NestedException: " 子类可以重载此方法，自定义前缀
     */
    public String getExceptionPrefix() {
        return prefix==null? EXCEPTION_PRIFIX : prefix;
    }
    
    public void setExceptionPrefix(String prefix) {
        this.prefix = prefix;
    }

    /**
     * Helper method for this class
     */
    protected static void appendTo(Appendable buf, CharSequence s) {
        try {
            buf.append(s);
        } catch (java.io.IOException e) {
        }
    }

    /**
     * Helper method for this class
     */
    protected static String appendStackTrace(Throwable e) {
        if (null == e) {
            return "";
        }
        StringWriter str = new StringWriter();
        PrintWriter out = new PrintWriter(str);
        try {
            e.printStackTrace(out);
        } finally {
            out.close();
        }
        return str.toString();
    }

}
