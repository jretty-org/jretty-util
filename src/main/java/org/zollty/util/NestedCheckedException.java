/* @(#)NestedCheckedException.java 
 * Copyright (C) 2013-2014 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License").
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * Create by zollty on 2013-7-05 [http://blog.csdn.net/zollty (or GitHub)]
 */
package org.zollty.util;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * NestedCheckedException
 * 用于包装异常信息
 * @author zollty 
 * @since 2013-7-05
 */
public class NestedCheckedException extends BasicCheckedException {

	private static final long serialVersionUID = -1444087151971883657L;
	private final String errorMsg;
	private final String[] params;
	private final Throwable exception;

	/**
	 * @param message 自定义错误信息
	 * @param args 占位符参数--[ 变长参数，用于替换message字符串里面的占位符"{}" ]
	 */
	public NestedCheckedException(String message, String... args) {
		this.errorMsg = message;
		this.params = args;
		this.exception = null;
	}
	
	/**
	 * @param e
	 */
	public NestedCheckedException(Throwable e) {
		this.errorMsg = null;
		this.params = null;
		this.exception = e;
	}
	
	/**
	 * @param e
	 * @param message 自定义错误信息
	 * @param args 占位符参数--[ 变长参数，用于替换message字符串里面的占位符"{}" ]
	 */
	public NestedCheckedException(Throwable e, String message, String... args) {
		this.errorMsg = message;
		this.params = args;
		this.exception = e;
	}
	
	/**
	 * 获取最原始的那个异常对象
	 */
    public Throwable getOriginalException() {
        if (null == exception) {
            return this;
        }
        if (NestedCheckedException.class.isInstance(exception)) {
            return ((NestedCheckedException) exception).getOriginalException();
        }
        return this;
    }
	
	@Override
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

    private String getStackTraceStr(String message) {
        if (null == exception) {
            if (message != null && message.length() != 0) {
                return getExceptionPrifix() + message + MSG_SPLIT + StringUtils.replaceParams(errorMsg, params);
            }
            return getExceptionPrifix() + StringUtils.replaceParams(errorMsg, params);
        }
        if (NestedCheckedException.class.isInstance(exception)) {
            if (message != null && message.length() != 0) {
                return ((NestedCheckedException) exception).getStackTraceStr(message + MSG_SPLIT
                        + StringUtils.replaceParams(errorMsg, params));
            }
            return ((NestedCheckedException) exception).getStackTraceStr(StringUtils.replaceParams(errorMsg, params));
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

    @Override
    public void printStackTrace() {
        System.err.println(getStackTraceStr());
    }

    @Override
    public void printStackTrace(PrintWriter err) {
        appendTo(err, getStackTraceStr());
    }

    @Override
    public void printStackTrace(PrintStream err) {
        appendTo(err, getStackTraceStr());
    }

    @Override
    public StackTraceElement[] getStackTrace() {
        if (null != exception) {
            return exception.getStackTrace();
        }
        return super.getStackTrace();
    }

    private String getExceptionName() {
        if (null == exception) {
            return getClass().getName();
        }
        // if( exception.getClass()==NestedCheckedException.class ){ keep
        if (NestedCheckedException.class.isInstance(exception)) {
            return ((NestedCheckedException) exception).getExceptionName();
        }
        return exception.getClass().getName();
    }

    @Override
    public String toString() {
        String msg = getMessage();
        String name = getExceptionName();
        if (msg == null) {
            return name;
        } else {
            int length = name.length() + 2 + msg.length();
            StringBuilder buffer = new StringBuilder(length);
            return buffer.append(name).append(": ").append(msg).toString();
        }
    }

    /**
     * 获取原始异常对象
     */
    public Throwable getException() {
        return exception;
    }

    @Override
    public Throwable getCause() {
        if (exception == null) {
            return null;
        }
        if (NestedCheckedException.class.isInstance(exception)) {
            return exception.getCause();
        }
        return exception;
    }

    /**
     * 异常的前缀名 默认为"org.zollty.NestedException: " 子类可以重载此方法，自定义前缀
     */
    protected String getExceptionPrifix() {
        return EXCEPTION_PRIFIX;
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

    private static final String MSG_SPLIT = " |- ";
    private static final String CAUSED_BY = "\nCaused by: ";
    private static final String EXCEPTION_PRIFIX = "org.zollty.NestedException: ";

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
