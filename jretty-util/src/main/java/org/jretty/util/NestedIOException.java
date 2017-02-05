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
package org.jretty.util;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;

import org.jretty.util.support.ExceptionDelegateSupport;
import org.jretty.util.support.NestedExceptionDelegate;

/**
 * NestedIOException的作用：
 * 如下例：
 <pre>
 public URI getURI() throws IOException {
    URL url = getURL();
    try {
        return UrlUtils.toURI(url);
    }
    catch (URISyntaxException ex) {
        throw new NestedIOException(ex);
    }
  }
 </pre>
 * 如果外层只捕获 IOException，则内部抛出的其他异常无法捕获，所以必须要将内部其它异常 转换成IOException。
 * 如果直接new IOException(ex)则堆栈重复了，所以使用嵌套异常 new NestedIOException(ex);
 * 
 * 另外，也可以在方法内部将IO异常处理了，然后再抛出一个无堆栈信息的IO异常。例如：
 <pre>
 public void doPost() throws IOException {
    OutputStream in = getInput();
    try {
        do something with IOException...
    }
    catch (IOException ex) {
        close(in);
        logger.error(ex);
        throw new NestedIOException("post error, please retry...");
    }
  }
 </pre>
 * 但是，对于上面这种情况，‘一般’不推荐这么做。建议 使用return false/true 来返回，或者，如果有返回值，则可以用 return Result<T>对象。
 * 
 * @see NestedExceptionDelegate
 * 
 * @author zollty
 * @since 2014-12-13
 */
public class NestedIOException extends IOException implements NestedException, ExceptionDelegateSupport {
    
    private static final long serialVersionUID = 7246460157526737440L;

    private final Throwable exception;
    
    private final NestedExceptionDelegate delegate;
    
    public static final String EXCEPTION_PRIFIX = "org.jretty.IOException: ";

    /**
     * @param message 自定义错误信息
     * @param args 占位符参数--[ 变长参数，用于替换message字符串里面的占位符"{}" ]
     */
    public NestedIOException(Object message, Object... args) {
        this.exception = null;
        this.delegate = new NestedExceptionDelegate(EXCEPTION_PRIFIX, message, args);
    }
    
    /**
     * @param e
     */
    public NestedIOException(Throwable e) {
        this.exception = e;
        this.delegate = new NestedExceptionDelegate(EXCEPTION_PRIFIX, exception);
    }
    
    /**
     * @param e
     * @param message 自定义错误信息
     * @param args 占位符参数--[ 变长参数，用于替换message字符串里面的占位符"{}" ]
     */
    public NestedIOException(Throwable e, Object message, Object... args) {
        this.exception = e;
        this.delegate = new NestedExceptionDelegate(EXCEPTION_PRIFIX, exception, message, args);
    }
    
    
    @Override
    public String getStackTraceStr() {
        return delegate.getStackTraceStr(null);
    }
    
    
    @Override
    public String getMessage() {
        return delegate.getMessage();
    }
    

    @Override
    public void printStackTrace() {
        delegate.printStackTrace();
    }

    @Override
    public void printStackTrace(PrintWriter err) {
        delegate.printStackTrace(err);
    }

    @Override
    public void printStackTrace(PrintStream err) {
        delegate.printStackTrace(err);
    }

    @Override
    public StackTraceElement[] getStackTrace() {
        if (null != exception) {
            return exception.getStackTrace();
        }
        // 保留原始堆栈信息，必要时可以调用 {@link #getOrigException()} 获取 包含堆栈信息的原始异常
        return super.getStackTrace();
    }
    
    /**
     * 重造一个原始的、包含堆栈信息的IOException
     */
    public IOException getOrigException() {
        if (null != exception) {
            if (NestedIOException.class.isInstance(exception)) {
                return ((NestedIOException) exception).getOrigException();
            }
        }
        IOException e = new IOException(getMessage());
        e.setStackTrace(getStackTrace());
        return e;
    }

    @Override
    public String toString() {
        return delegate.toString();
    }


    /**
     * 获取最原始的那个异常对象
     */
    @Override
    public Throwable getCause() {
        return delegate.getCause();
    }

    
    @Override
    public NestedExceptionDelegate getDelegate() {
        return delegate;
    }

}
