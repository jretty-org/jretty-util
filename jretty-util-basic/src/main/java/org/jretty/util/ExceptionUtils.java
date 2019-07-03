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

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.StringReader;

/**
 * 设计目的：<br>
 * 1、有些错误信息(errorMsg)非常的长，比如Spring或Hibernate或JDBC的错误信息;<br>
 * 2、一些异常的堆栈特别大，动辄几十、上百行，例如Tomcat、WebSphere的异常信息。<br>
 * 为了精简错误信息，以及防止大量冗余信息记录到日志中，故设计了这个方案：截取核心的错误信息——详见下面的算法。
 * 
 * @author zollty
 * @since 2013-6-27
 */
public class ExceptionUtils {
    private static final String OMIT_PRE = "\t... ";
    private static final String OMIT_SUB = " more\n";
    private static final String SPLIT_SIGN = ": ";
    
    /**
     * 获得完整的异常信息（包括class name、message 和 堆栈）
     */
    public static String getStackTraceStr(Throwable e) {
        StringBuilderWriter sw = new StringBuilderWriter();
        PrintWriter p = new PrintWriter(sw);
        e.printStackTrace(p);
        return sw.toString();
    }

    /**
     * 智能将StackTrace堆栈信息转换成字符串
     * 
     * @param linechecker 堆栈过滤器，可为 null
     * @param e 异常实例
     * @return 堆栈信息String
     */
    public static String getStackTraceStr(LineChecker linechecker, Throwable e) {
        return getStackTraceStr(linechecker, e, null);
    }
    
    /**
     * 智能将StackTrace堆栈信息转换成字符串
     * 
     * @param linechecker 堆栈过滤器，可为 null
     * @param e 异常实例
     * @param prompt 附加提示，可为 null
     * @return 堆栈信息String
     */
    public static String getStackTraceStr(LineChecker linechecker, Throwable e, String prompt) {
        if (null == e) {
            return Const.STRING_LEN0;
        }
        // 注意：StringWriter/StringReader不需要close和flush
        StringBuilderWriter sw = new StringBuilderWriter();
        PrintWriter out = new PrintWriter(sw);
        e.printStackTrace(out);
        out = null;
        if (linechecker == null) {
            return sw.toString();
        }
        StringBuilder sb = new StringBuilder();
        if (StringUtils.isNotEmpty(prompt)) {
            sb.append(prompt);
            sb.append(MSG_SPLIT);
        }
        int at = 0;
        int skip = 0;
        String errorMsg = null;
        String back = null;
        BufferedReader br = new BufferedReader(new StringReader(sw.toString()));
        try {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.startsWith("\tat")) {
                    if (skip > 0) {
                        sb.append(OMIT_PRE).append(skip).append(OMIT_SUB);
                        skip = 0;
                    }
                    if (errorMsg != null) {
                        errorMsg += Const.LF + line;
                    }
                    else {
                        errorMsg = line;
                    }
                    // sb.append(line).append(Const.LF);
                    at = 3;
                    continue;
                }
                if (errorMsg != null) {
                    if (back != null && errorMsg.length() > 100 && back.length() > 100) { // 小于100的不做处理
                        boolean a = errorMsg.endsWith(back.substring(back.length() - 10));
                        int ia = errorMsg.indexOf(SPLIT_SIGN) + 2;
                        String ma = errorMsg.substring(ia, ia + 10); // 截取10个字符
                        int ib = back.indexOf(SPLIT_SIGN) + 2;
                        String mb = back.substring(ib, ib + 10); // 截取10个字符
                        boolean b = ma.equals(mb);
                        if (a && b) {
                            // 不重复记录错误信息
                            sb.append("Caused by: (ditto)\n");
                            errorMsg = null;
                        }
                        else {
                            sb.append(errorMsg).append(Const.LF);
                            back = errorMsg;
                            errorMsg = null;
                        }
                    }
                    else {
                        sb.append(errorMsg).append(Const.LF);
                        back = errorMsg;
                        errorMsg = null;
                    }
                }
                if (at > 0) {
                    sb.append(line).append(Const.LF);
                    at--;
                    continue;
                }
                if (linechecker == null) {
                    if (skip > 0) {
                        sb.append(OMIT_PRE).append(skip).append(OMIT_SUB);
                        skip = 0;
                    }
                    sb.append(line).append(Const.LF);
                    continue;
                }
                if (linechecker.checkLine(line)) {
                    if (skip > 0) {
                        sb.append(OMIT_PRE).append(skip).append(OMIT_SUB);
                        skip = 0;
                    }
                    sb.append(line).append(Const.LF);
                    continue;
                }
                skip++;
            }
        }
        catch (Exception exp) {
            throw new NestedRuntimeException(exp);
        }
        br = null;
        if (errorMsg != null) {
            sb.append(errorMsg);
        }
        return sb.toString();
    }

    public static interface LineChecker {
        /**
         * @return true = 不过滤，false = 过滤
         */
        boolean checkLine(String line);
    }

    private static final String MSG_SPLIT = " |- ";

    /**
     * 获取精简过的错误信息，(错误类型+错误描述)，默认截取440个字符（前320个+后120个）
     * 
     * @param prompt 附加提示，可为 null
     */
    public static String getExceptionProfile(Throwable e) {
        return getExceptionProfile(e, null, 440); // 默认440个字符
    }

    /**
     * 获取精简过的错误信息，(错误类型+错误描述)，默认截取440个字符（前320个+后120个）
     * 
     * @param prompt 附加提示，可为 null
     */
    public static String getExceptionProfile(Throwable e, String prompt) {
        return getExceptionProfile(e, prompt, 440); // 默认440个字符
    }

    /**
     * 获取精简过的错误信息，(错误类型+错误描述)，默认截取440个字符（前320个+后120个）
     * 
     * @param errorLen 截取错误字符串的最大长度，比如 500
     */
    public static String getExceptionProfile(Throwable e, int errorLen) {
        return getExceptionProfile(e, null, errorLen);
    }

    /**
     * 获取精简过的错误信息，(错误类型+错误描述)，截取 errorLen 个字符。
     * 
     * @param prompt
     *            附加提示，可为 null
     * @param errorLen
     *            截取错误字符串的最大长度，比如 500
     */
    public static String getExceptionProfile(Throwable e, String prompt, int errorLen) {
        // 如果error过长,则精简到errorLen个字符
        String error = errorMsgCut(e.toString(), errorLen);
        // getErrorDescription
        StringBuilder sb = new StringBuilder();
        if (StringUtils.isNotEmpty(prompt)) {
            sb.append(prompt);
            sb.append(MSG_SPLIT);
        }
        sb.append(error);

        return sb.toString();
    }

    /**
     * 裁剪错误信息，最多只取 maxLen 个字符(maxLen>=200)，规则如下： <br>
     * 【只保留前面8/11的字符+后面3/11的字符】 <br>
     * 例如一个数据库的errorMessage长度可达1000个字符，用此方法裁剪后, <br>
     * 假设maxLen=550，那就只保留前400个字符+后150个字符。
     * 
     * @param maxLen
     *            截取错误字符串的最大长度，比如500
     * @return 精简后的错误信息字符串
     * @author zollty 2013-7-27
     */
    public static String errorMsgCut(String errorMsg, int maxLen) {
        if (errorMsg == null) {
            return null;
        }
        int strLen = errorMsg.length();
        if (strLen < 200 || strLen <= maxLen) { // 小于200的字符，不做处理
            return errorMsg;
        }
        int front = maxLen * 8 / 11;
        return errorMsg.substring(0, front) + "......" + errorMsg.substring(strLen - maxLen + front, strLen);
    }
    
    
    /**
     * 改造堆栈信息，remove第一个堆栈，便于外部调用程序直接定位到自己的调用出处。
     * 例如，Assert内部的堆栈，用removeFirstStack改造后，堆栈信息为：
     * <pre>
     * java.lang.IllegalArgumentException: [Assertion failed] - this argument is required; it must not be null
        at org.zollty.util.AssertTest.doService(AssertTest.java:25)
        
     * </pre>
     * 改造前堆栈信息为：
     * <pre>
     * java.lang.IllegalArgumentException: [Assertion failed] - this argument is required; it must not be null
        at org.zollty.util.AssertTest.notNull(AssertTest.java:123)
        at org.zollty.util.AssertTest.notNull(AssertTest.java:110)
        at org.zollty.util.AssertTest.hasText(AssertTest.java:91)
        at org.zollty.util.AssertTest.hasLength(AssertTest.java:51)
        at org.zollty.util.AssertTest.doService(AssertTest.java:25)
        
     * </pre>
     */
    public static <T extends Throwable> T removeFirstStack(T e){
        StackTraceElement[] st = e.getStackTrace();
        st = (StackTraceElement[]) ArrayUtils.remove(st, 0);
        e.setStackTrace(st);
        return e;
    }

    /**
     * Run in try-catch Throwable, change Throwable to NestedCheckedException 
     * {捕获所有异常和错误}
     * 
     * @see doInSecure(final SecureRun<T> runHook)
     */
    public static interface SecureRun<T> {
        T run() throws Throwable;
    }

    /**
     * Run in try-catch Throwable, change Throwable to NestedCheckedException 
     * {捕获所有异常和错误}
     */
    public static <T> T doInSecure(final SecureRun<T> runHook) throws NestedCheckedException {
        try {
            return runHook.run();
        }
        catch (Throwable t) {
            throw new NestedCheckedException(t);
        }
    }
    
    /**
     * Handle {@code InvocationTargetException, ExceptionInInitializerError, 
     *   RuntimeErrorException, SAXException, MBeanException}, 
     * get its nested cause exception in the wrap of NestedRuntimeException.
     * @param e the nested exception (Probably InvocationTargetException, ExceptionInInitializerError...) to handle
     * @return a NestedRuntimeException
     */
    public static NestedRuntimeException causeException(Throwable e) {
        return new NestedRuntimeException(e.getCause());
    }

}