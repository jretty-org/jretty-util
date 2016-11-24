/* 
 * Copyright (C) 2013-2014 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License").
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * Create by ZollTy on 2013-12-11 (http://blog.zollty.com/, zollty@163.com)
 */
package org.jretty.util;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 高效的字符串split工具，非正则表达式，与java.lang.String#split的实现截然不同
 * 
 * @author zollty
 * @since 2013-12-11
 */
public class StringSplitUtils {
    
    /**
     * 说明：
     * 
     * <p>1<p>
     * StringSplitUtils.split 和 StringSplitUtils.tokenizeToStringArray 几乎是等价的，唯一不同的是：
     * <p>
     * split 把null分隔符视为以空白符分割（Null separator means use whitespace），
     * 而后者不允许使用null作为参数（java.util.StringTokenizer.hasMoreTokens(StringTokenizer.java:306) throw NullPointerException）
     * 
     * 补充一点：还有一处不同，tokenizeToStringArray得到的所有token值，默认都是 string.trim了的。
     * 
     * <p>
     * 例如：<pre>
     *     "ab  KKK\t de fg \n\r LLLL", null
     *     [split] [ab, KKK, de, fg, LLLL]
     * </pre>
     * 
     * <p>2<p>
     * StringSplitUtils.split 和 StringSplitUtils.splitByWholeSeparator 的唯一区别在于：
     * <p>
     * split（和tokenizeToStringArray）的分隔字符串里面的每个字符都视为分隔标识，而splitByWholeSeparator只视整个分割字符串为一个分隔标识
     * <p>
     * 典型的例子就是：<pre>
     *    "app-KKKKKK/-opt-/--/-was", "-/--/-"
     *    [split] [app, KKKKKK, opt, was]
     *    [splitByWholeSeparator] [app-KKKKKK/-opt, was]
     * </pre>
     * 
     * 
     * <p>3<p>
     * delimitedListToStringArray 和 string.split 类似，
     * 他们二者 和 前面提到的 split与splitByWholeSeparator 有显著的区别，
     * 即他们 按正常模式解析连续的分隔符（比如“opt///was/”），中间会算作空字符串（""），
     * 而前面提到的 split与splitByWholeSeparator 则会忽略所有的连续分隔符，不会计算空字符串。
     * 
     * <p>
     * delimitedListToStringArray 和 string.split 之间有两点区别：
     * <p>
     * 1、后者不允许以null作为分隔符参数，前者允许null，但是如果是null则直接返回new String[]{原字符串}<p>
     * 2、前者会计算分隔符前后的空格，后者只会计算分隔符前面的空格，
     * 例如:
     * <pre>
     *    "/app/opt///was/", "/"
     *    [split] [app, opt, was]

     *    [delimitedListToStringArray] [, app, opt, , , was, ]
     *    [string.split] [, app, opt, , , was]
     * </pre>
     */
    /**
     * 复杂度预测：
     * <p>
     * 1、由于 split 视所有spitStr里面的字符为分割标识，而且支持 null 分割空白字符(Whitespace)，预测它的复杂度比较高。
     * 2、delimitedListToStringArray 和 string.split 要取出所有重复分割符之间的空字符，所有复杂度会稍微高一些。
     * 
     * 
     * 测试结果：
     * <pre>
     *   [1][split] 281+307+363+375+426+441+507=2700
     *   [2][splitByWholeSeparator] 252+253+261+288+346+389+500=2289
     *   [3][delimitedListToStringArray] 117+126+141+143+155+261+369=1312
     *   [4][tokenizeToStringArray] 177+224+235+254+267+279+284=1720
     *   [5][string.split] 1083+470+559+631+634+688+757=4822
     *  </pre>
     *  可见，delimitedListToStringArray 的效率最高，最低的是 string.split，其余几个都差不多。
     *  
     *  
     *  <p>
     *  按实用性排行：
     *  <pre>
     *  1、delimitedListToStringArray 和 string.split 会保留空格，我认为是比较正常的做法。
     *  2、delimitedListToStringArray 和 splitByWholeSeparator 视整个spitStr为分隔符，是比较常见的做法。
     *  3、特殊情况：
     *      1）split 和 tokenizeToStringArray 会 视所有spitStr里面的字符为分割标识，在某些情况下，可能是必选方案。
     *      2）split 支持 null 分割“空白字符”(Whitespace)，在某些情况下，可能是必选方案。
     *  4、string.split 为 JDK String对象自带API，使用比较方便。
     *  </pre>
     *  <p>
     *  总结：
     *  日常使用 delimitedListToStringArray，特殊情况使用 split
     *  
     *  在 splitByWholeSeparator 使用的场合，换做 delimitedListToStringArray，去掉数组中的 "" 值即可。
     *  在 tokenizeToStringArray 使用的场合，换做 split，确保 src 不为 null即可。
     *  在 string.split 使用的场合，换做 delimitedListToStringArray，去掉数组最后面的 "" 值即可。
     *  
     *  又 delimitedListToStringArray <> splitByWholeSeparatorWorker(src, splitStr, -1, true) 
     *  
     *  故 split 配合 splitByWholeSeparator 即可
     *  
     */
    
    public static final String EMPTY = "";
    private static final String[] EMPTY_STRING_ARRAY = new String[0];
    
    /**
     * Performs the logic for the <code>split</code> and
     * <code>splitPreserveAllTokens</code> methods that return a maximum array
     * length.
     *
     * @param str
     *            the String to parse, may be <code>null</code>
     * @param separatorChars
     *            the separate character
     * @param preserveAllTokens
     *            if <code>true</code>, adjacent separators are treated as empty
     *            token separators; if <code>false</code>, adjacent separators
     *            are treated as one separator.
     * @return an array of parsed Strings, <code>null</code> if null String
     *         input
     */
    public static String[] splitWorker(String str, String separatorChars, boolean preserveAllTokens, boolean includeLastEmpty) {
        // Performance tuned for 2.0 (JDK1.4)
        // Direct code is quicker than StringTokenizer.
        // Also, StringTokenizer uses isSpace() not isWhitespace()

        if (str == null) {
            return null;
        }
        int len = str.length();
        if (len == 0) {
            return EMPTY_STRING_ARRAY;
        }
        List<String> list = new ArrayList<String>();
//        int sizePlus1 = 1;
        int i = 0, start = 0;
        boolean match = false;
        boolean lastMatch = false;
        if (separatorChars == null) {
            // Null separator means use whitespace
            while (i < len) {
                if (Character.isWhitespace(str.charAt(i))) {
                    if (match || preserveAllTokens) {
                        lastMatch = true;
//                        if (sizePlus1++ == max) {
//                            i = len;
//                            lastMatch = false;
//                        }
                        list.add(str.substring(start, i));
                        match = false;
                    }
                    start = ++i;
                    continue;
                }
                lastMatch = false;
                match = true;
                i++;
            }
        } else if (separatorChars.length() == 1) {
            // Optimise 1 character case
            char sep = separatorChars.charAt(0);
            while (i < len) {
                if (str.charAt(i) == sep) {
                    if (match || preserveAllTokens) {
                        lastMatch = true;
//                        if (sizePlus1++ == max) {
//                            i = len;
//                            lastMatch = false;
//                        }
                        list.add(str.substring(start, i));
                        match = false;
                    }
                    start = ++i;
                    continue;
                }
                lastMatch = false;
                match = true;
                i++;
            }
        } else {
            // standard case
            while (i < len) {
                if (separatorChars.indexOf(str.charAt(i)) >= 0) {
                    if (match || preserveAllTokens) {
                        lastMatch = true;
//                        if (sizePlus1++ == max) {
//                            i = len;
//                            lastMatch = false;
//                        }
                        list.add(str.substring(start, i));
                        match = false;
                    }
                    start = ++i;
                    continue;
                }
                lastMatch = false;
                match = true;
                i++;
            }
        }
        
        if (match || (preserveAllTokens && lastMatch)) {
           // list.add(str.substring(start, i));
           // Modified by zouty, change statege: e.g. make [, opt, was, ] to [, opt, was] with src = "/opt/was/" 
            String t = str.substring(start, i);
            if(includeLastEmpty){
                list.add(t);
            }
            else if(!t.equals(EMPTY)){
                list.add(t);
            }
        }
        return (String[]) list.toArray(EMPTY_STRING_ARRAY);
    }
    
    
    /**
     * Performs the logic for the <code>split</code> and
     * <code>splitPreserveAllTokens</code> methods that do not return a maximum
     * array length.
     *
     * @param str
     *            the String to parse, may be <code>null</code>
     * @param separatorChar
     *            the separate character
     * @param preserveAllTokens
     *            if <code>true</code>, adjacent separators are treated as empty
     *            token separators; if <code>false</code>, adjacent separators
     *            are treated as one separator.
     * @return an array of parsed Strings, <code>null</code> if null String
     *         input
     */
    public static String[] splitWorker(String str, char separatorChar,
            boolean preserveAllTokens, boolean includeLastEmpty) {
        // Performance tuned for 2.0 (JDK1.4)

        if (str == null) {
            return null;
        }
        int len = str.length();
        if (len == 0) {
            return EMPTY_STRING_ARRAY;
        }
        List<String> list = new ArrayList<String>();
        int i = 0, start = 0;
        boolean match = false;
        boolean lastMatch = false;
        while (i < len) {
            if (str.charAt(i) == separatorChar) {
                if (match || preserveAllTokens) {
                    list.add(str.substring(start, i));
                    match = false;
                    lastMatch = true;
                }
                start = ++i;
                continue;
            }
            lastMatch = false;
            match = true;
            i++;
        }
        if (match || (preserveAllTokens && lastMatch)) {
            // list.add(str.substring(start, i));
            // Modified by zouty, change statege: e.g. make [, opt, was, ] to [, opt, was] with src = "/opt/was/" 
            String t = str.substring(start, i);
            if(includeLastEmpty){
                list.add(t);
            }
            else if(!t.equals(EMPTY)){
                list.add(t);
            }
        }
        return list.toArray(EMPTY_STRING_ARRAY);
    }
    
    
    /**
     * Performs the logic for the <code>splitByWholeSeparatorPreserveAllTokens</code> methods.
     *
     * @param str  the String to parse, may be <code>null</code>
     * @param separator  String containing the String to be used as a delimiter,
     *  <code>null</code> splits on whitespace
     * @param preserveAllTokens if <code>true</code>, adjacent separators are
     * treated as empty token separators; if <code>false</code>, adjacent
     * separators are treated as one separator.
     * @return an array of parsed Strings, <code>null</code> if null String input
     */
    public static String[] splitByWholeSeparatorWorker(String str, String separator, 
                                                        boolean preserveAllTokens, boolean includeLastEmpty)
    {
        if (str == null) {
            return null;
        }

        int len = str.length();

        if (len == 0) {
            return EMPTY_STRING_ARRAY;
        }

        if ((separator == null) || (EMPTY.equals(separator))) {
            // Split on whitespace.
            return splitWorker(str, null, preserveAllTokens, includeLastEmpty);
        }

        int separatorLength = separator.length();

        ArrayList<String> substrings = new ArrayList<String>();
//        int numberOfSubstrings = 0;
        int beg = 0;
        int end = 0;
        while (end < len) {
            end = str.indexOf(separator, beg);

            if (end > -1) {
                if (end > beg) {
//                    numberOfSubstrings += 1;
//
//                    if (numberOfSubstrings == max) {
//                        end = len;
//                        substrings.add(str.substring(beg));
//                    } else {
                    
                        // The following is OK, because String.substring( beg, end ) excludes
                        // the character at the position 'end'.
                        // System.out.println("sub " + beg + "|" + end +"|" + str.substring(beg, end));
                        substrings.add(str.substring(beg, end));

                        // Set the starting point for the next search.
                        // The following is equivalent to beg = end + (separatorLength - 1) + 1,
                        // which is the right calculation:
                        beg = end + separatorLength;
//                    }
                } else {
                    // We found a consecutive occurrence of the separator, so skip it.
                    if (preserveAllTokens) {
//                        numberOfSubstrings += 1;
//                        if (numberOfSubstrings == max) {
//                            end = len;
//                            substrings.add(str.substring(beg));
//                        } else {
                            substrings.add(EMPTY);
//                        }
                    }
                    beg = end + separatorLength;
                }
            } else {
                // String.substring( beg ) goes from 'beg' to the end of the String.
                // System.out.println("sub~~ " + beg + "|" + end +"|" + str.substring(beg));
                String t = str.substring(beg);
                
                // if(!t.equals(EMPTY))
                //     substrings.add(str.substring(beg));
                // Modified by zouty, bugfix: e.g. make [, opt, was] to [, opt, was, ] with src = "/opt/was/" 
                
                if (preserveAllTokens && includeLastEmpty) {
                    substrings.add(t);
                } else if(!t.equals(EMPTY)){
                    substrings.add(t);
                }
                
                end = len;
            }
        }

        return substrings.toArray(EMPTY_STRING_ARRAY);
    }
    
    // 以下是二次封装的一些常用方法
    
    /**
     * 显示空值，且包含末尾的空值，例如 src = "/opt//was/"   sep=/  result = [, opt, , was, ]
     * @see #splitByWholeSeparatorWorker(String, String, boolean, boolean)
     */
    public static String[] splitByWholeSeparator(String str, String separator) {
        return splitByWholeSeparatorWorker(str, separator, true, true ) ;
    }
    
    /**
     * 显示空值，且包含末尾的空值，例如 src = "/opt//was/"   sep=/  result = [, opt, , was, ]
     * @see #splitWorker(String, char, boolean, boolean)
     */
    public static String[] split(String str, String separatorChars) {
        return splitWorker(str, separatorChars, true, true); // 显示空值
    }
    
    /**
     * 显示空值，且包含末尾的空值，例如 src = "/opt//was/"   sep=/  result = [, opt, , was, ]
     * @see #splitWorker(String, char, boolean, boolean)
     */
    public static String[] split(String str, char separatorChar) {
        return splitWorker(str, separatorChar, true, true);  // 显示空值
    }
    
    //~~~~~~~~~
    /**
     * 显示空值，但不包含末尾的空值，例如 src = "/opt//was/"   sep=/  result = [, opt, , was]
     * @see #splitByWholeSeparatorWorker(String, String, boolean, boolean)
     */
    public static String[] splitByWholeSeparatorNolastEmpty(String str, String separator) {
        return splitByWholeSeparatorWorker(str, separator, true, false ) ; // 显示空值
    }
    
    /**
     * 显示空值，但不包含末尾的空值，例如 src = "/opt//was/"   sep=/  result = [, opt, , was]
     * @see #splitWorker(String, char, boolean, boolean)
     */
    public static String[] splitNolastEmpty(String str, String separatorChars) {
        return splitWorker(str, separatorChars, true, false); // 显示空值
    }
    
    /**
     * 显示空值，但不包含末尾的空值，例如 src = "/opt//was/"   sep=/  result = [, opt, , was]
     * @see #splitWorker(String, char, boolean, boolean)
     */
    public static String[] splitNolastEmpty(String str, char separatorChar) {
        return splitWorker(str, separatorChar, true, false);  // 显示空值
    }
    
    //~~~~~~~~~
    
    /**
     * 不显示空值，例如 src = "/opt//was/"   sep=/  result = [opt, was]
     * @see #splitByWholeSeparatorWorker(String, String, boolean, boolean)
     */
    public static String[] splitByWholeSeparatorIgnoreEmpty(String str, String separator) {
        return splitByWholeSeparatorWorker(str, separator, false, false ) ; // 显示空值
    }
    
    /**
     * 不显示空值，例如 src = "/opt//was/"   sep=/  result = [opt, was]
     * @see #splitWorker(String, String, boolean, boolean)
     */
    public static String[] splitIgnoreEmpty(String str, String separatorChars) {
        return splitWorker(str, separatorChars, false, false); // 显示空值
    }
    
    /**
     * 不显示空值，例如 src = "/opt//was/"   sep=/  result = [opt, was]
     * @see #splitWorker(String, char, boolean, boolean)
     */
    public static String[] splitIgnoreEmpty(String str, char separatorChar) {
        return splitWorker(str, separatorChar, false, false);  // 显示空值
    }

}