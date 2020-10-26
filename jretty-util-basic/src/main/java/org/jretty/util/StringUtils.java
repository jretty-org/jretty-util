/* 
 * Copyright (C) 2013-2020 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License").
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * Create by ZollTy on 2013-6-02 (http://blog.zollty.com/, zollty@163.com)
 */
package org.jretty.util;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

/**
 * 通用字符串工具类
 * 
 * @author zollty (高效的算法保障)
 * @since 2013-06-02
 */
public class StringUtils {
    
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓
    // String 空值判断和处理 相关算法工具
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛

    /**
     * Checks if a String is null or empty ("").
     * 
     * <pre>
     * StringUtils.isEmpty(null) = true
     * StringUtils.isEmpty("") = true
     * StringUtils.isEmpty(" ") = false
     * StringUtils.isEmpty("12345") = false
     * StringUtils.isEmpty(" 12345 ") = false
     * </pre>
     */
    public static boolean isEmpty(CharSequence str) {
        return str == null || str.length() == 0;
    }
    
    /**
     * Checks if a String is null or empty ("").
     * 
     * <p> the same as {@link #isEmpty(CharSequence)} </p>
     */
    public static boolean isNullOrEmpty(CharSequence str) {
        return str == null || str.length() == 0;
    }
    
    /**
     * Checks if a String is null or empty ("").
     */
    public static boolean isNotEmpty(CharSequence str) {
        return str != null && str.length() != 0;
    }
    
    /**
     * Checks if all the String are null or empty ("").
     */
    public static boolean areNotEmpty(CharSequence... values) {
        boolean result = true;
        if (values == null || values.length == 0) {
            result = false;
        } else {
            for (CharSequence value : values) {
                result &= !isEmpty(value);
            }
        }
        return result;
    }

    /**
     * Checks if a String is empty (""), null or whitespace(e.g. " ", "\t", "\n").
     * 
     * <pre>
     * StringUtils.isBlank(null) = true
     * StringUtils.isBlank("") = true
     * StringUtils.isBlank(" ") = true
     * StringUtils.isBlank("12345") = false
     * StringUtils.isBlank(" 12345 ") = false
     * </pre>
     */
    public static boolean isBlank(CharSequence str) {
        int strLen;
        // empty ("") or null
        if (str == null || (strLen = str.length()) == 0) {
            return true;
        }
        // length > 0 then check every character
        for (int i = 0; i < strLen; i++) {
            if (Character.isWhitespace(str.charAt(i)) == false) {
                // find char is not whitespace
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if a String is empty (""), null or whitespace(e.g. " ", "\t", "\n").
     */
    public static boolean isNotBlank(CharSequence str) {
        int strLen;
        // empty ("") or null
        if (str == null || (strLen = str.length()) == 0) {
            return false;
        }
        // length > 0 then check every character
        for (int i = 0; i < strLen; i++) {
            if (Character.isWhitespace(str.charAt(i)) == false) {
                // find char is not whitespace
                return true;
            }
        }
        return false;
    }
    
    /**
     * Checks if all the String are empty (""), null or whitespace(e.g. " ", "\t", "\n").
     */
    public static boolean areNotBlank(CharSequence... values) {
        boolean result = true;
        if (values == null || values.length == 0) {
            result = false;
        } else {
            for (CharSequence value : values) {
                result &= !isBlank(value);
            }
        }
        return result;
    }

    /**
     * 判断两个字符串是否相等，包含空字符串的情况
     */
    public static boolean equals(String str1, String str2) {
        return str1 != null && str1.equals(str2);
    }

    /**
     * 将null转换成空字符串""
     */
    public static String nullToEmpty(String str) {
        return str == null ? Const.STRING_LEN0 : str;
    }
    
    /**
     * 裁剪字符串，最多只取 maxLen 个字符(maxLen>=20)，规则如下： <br>
     * 【只保留前面8/11的字符+后面3/11的字符】 <br>
     * 例如一个errorMessage长度可达1000个字符，用此方法裁剪后, <br>
     * 假设maxLen=550，那就只保留前400个字符+后150个字符。
     * 
     * @param str 原始字符串
     * @param maxLen 截取字符串的最大长度(>=20)，比如500、4000等
     * @return 裁剪后的字符串（如果没超过maxLen则原样返回，否则将裁剪成maxLen长度）
     */
    public static String cutString(String str, int maxLen) {
        if (str == null) {
            return null;
        }
        if (maxLen < 20) {
            throw new IllegalArgumentException("the param 'maxLen' must >= 20");
        }
        int strLen = str.length();
        // 小于等于20或maxLen的字符，不做处理
        if (strLen <= maxLen) {
            return str;
        }
        int front = maxLen * 8 / 11;
        return str.substring(0, front - 3) + "......" + str.substring(strLen - maxLen + front + 3, strLen);
    }
    
    
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓
    // String Charset-字符编码 相关工具
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛

    public static String decideCharSet(String charSet) {
        if (StringUtils.isNullOrEmpty(charSet)) {
            return Const.DEFAULT_CHARSET;
        }
        return charSet;
    }

    public static Charset toCharSet(String charSet) {
        if (StringUtils.isNullOrEmpty(charSet)) {
            return Charset.forName(Const.DEFAULT_CHARSET);
        }
        return Charset.forName(charSet);
    }

    /**
     * 当前环境的默认编码(即file.encoding)
     */
    public static String getJvmEncoding() {
        return Charset.defaultCharset().displayName();
    }
    
    /**
     * 重新定义了String.getBytes()。默认用UTF-8编码，以便去除与平台的相关性。
     * <p>
     * 且将CheckedException转换成了RuntimeException。
     * 
     * @param str 原字符串
     * @return str.getBytes("UTF-8")的结果
     */
    public static byte[] getBytes(String str) {
        if (str == null) {
            return null;
        }
        try {
            return str.getBytes(Const.DEFAULT_CHARSET);
        }
        catch (UnsupportedEncodingException e) {
            throw new NestedRuntimeException(e);
        }
    }

    /**
     * String.getBytes()，将CheckedException转换成了RuntimeException。
     * <p>
     * 默认用UTF-8编码，以便去除与平台的相关性。
     * 
     * @param str 原字符串
     * @return str.getBytes("UTF-8")的结果
     */
    public static byte[] getBytes(final String str, final String charSet) {
        if (str == null) {
            return null;
        }
        try {
            return str.getBytes(decideCharSet(charSet));
        }
        catch (UnsupportedEncodingException e) {
            throw new NestedRuntimeException(e);
        }
    }

    public static String newString(final byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        try {
            return new String(bytes, Const.DEFAULT_CHARSET);
        }
        catch (UnsupportedEncodingException e) {
            throw new NestedRuntimeException(e);
        }
    }

    public static String newString(final byte[] bytes, final String charSet) {
        if (bytes == null) {
            return null;
        }
        try {
            return new String(bytes, decideCharSet(charSet));
        }
        catch (final UnsupportedEncodingException e) {
            throw new NestedRuntimeException(e);
        }
    }

    /**
     * 字符串编码转换
     * @param newCharset 新编码，例如UTF-8
     */
    public static String changeEncode(final String str, final String newCharset) {
        if (str == null || str.length() == 0)
            return str;
        try {
            String iso = new String(str.getBytes(newCharset), Const.ISO_8859_1); // ISO-8859-1
            return new String(iso.getBytes(Const.ISO_8859_1), newCharset);
        }
        catch (UnsupportedEncodingException e) {
            throw new NestedRuntimeException(e);
        }
    }
    

    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓
    // String 替换处理 相关算法工具
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛
    
    /**
     * Replace all occurences of a substring within a string with another string.
     * <p>[非正则表达式，区别于String.replace()方法]</p>
     * 
     * @param inString String to examine
     * @param oldPattern String to replace
     * @param newPattern String to insert
     * @return a String with the replacements
     */
    public static String replace(String inString, String oldPattern, String newPattern) {
        if (isNullOrEmpty(inString) || isNullOrEmpty(oldPattern) || newPattern == null) {
            return inString;
        }
        StringBuilder sb = new StringBuilder();
        // our position in the old string
        int pos = 0;
        int index = inString.indexOf(oldPattern);
        // the index of an occurrence we've found, or -1
        int patLen = oldPattern.length();
        while (index >= 0) {
            sb.append(inString.substring(pos, index));
            sb.append(newPattern);
            pos = index + patLen;
            index = inString.indexOf(oldPattern, pos);
        }
        sb.append(inString.substring(pos));
        // remember to append any characters to the right of a match
        return sb.toString();
    }

    private static final String REPLACE_LABEL = "{}";

    /**
     * 用objs[]的值去替换字符串s中的{}符号
     */
    public static String replaceParams(Object msg, Object... objs) {
        if (msg == null)
            return null;
        String s = msg.toString();
        if (objs == null || objs.length == 0)
            return s;
        if (s.indexOf(REPLACE_LABEL) == -1) {
            StringBuilder result = new StringBuilder();
            result.append(s);
            for (Object obj : objs) {
                result.append(" ").append(obj);
            }
            return result.toString();
        }

        String[] stra = new String[objs.length];
        int len = s.length();
        for (int i = 0; i < objs.length; i++) {
            if (objs[i] != null)
                stra[i] = objs[i].toString();
            else {
                stra[i] = Const.STRING_NULL;
            }
            len += stra[i].length();
        }

        StringBuilder result = new StringBuilder(len);
        int cursor = 0;
        int index = 0;
        for (int start; (start = s.indexOf(REPLACE_LABEL, cursor)) != -1;) {
            result.append(s.substring(cursor, start));
            if (index < stra.length) {
                result.append(stra[index]);
            }
            else {
                result.append(REPLACE_LABEL);
            }
            cursor = start + 2;
            index++;
        }
        result.append(s.substring(cursor, s.length()));
        if (index < objs.length) {
            for (int i = index; i < objs.length; i++) {
                result.append(" ").append(objs[i]);
            }
        }
        return result.toString();
    }

    /**
     * @see #replaceParams(String, Object...)
     */
    public static String replaceParams(String s, String... objs) {
        return StringUtils.replaceParams(s, (Object[]) objs);
    }

    /**
     * 转换html里面的5个特殊字符：<code>&, <, >, ', and "</code>,
     */
    public static String simpleHtmlEscape(String str) {
        if (str == null)
            return Const.STRING_LEN0;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); ++i) {
            char c = str.charAt(i);
            switch (c) {
            case '&':
                sb.append("&amp;");
                break;
            case '<':
                sb.append("&lt;");
                break;
            case '>':
                sb.append("&gt;");
                break;
            case '\"':
                sb.append("&quot;");
                break;
            case '\'':
                sb.append("&#39;");
                break;
            default:
                sb.append(c);
                break;
            }
        }
        return sb.toString();
    }
    
    public static String simpleHtmlUnEscape(String str) {
        str = replace(str, "&amp;", "&");
        str = replace(str, "&lt;", "<");
        str = replace(str, "&gt;", ">");
        str = replace(str, "&quot;", "\"");
        str = replace(str, "&#39;", "\'");
        return str;
    }
    
    /**
     * 英文单词 驼峰 转 下划线（全小写） 格式
     * 例如 
     * <p> dsaTimeDS == dsa_Time_DS
     * <p> dsaTimeDSEr == dsa_Time_DS_Er
     */
    public static String camel2Underline(String param) {
        if (param == null || param.length() == 0) {
            return Const.STRING_LEN0;
        }
        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
            if (Character.isUpperCase(c)) {
                if ((i > 0 && Character.isLowerCase(param.charAt(i - 1)))
                        || (i + 1 < len && Character.isLowerCase(param.charAt(i + 1)))) {
                    sb.append(Const.UNDERLINE);
                }
                sb.append(c);
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 英文单词 下划线 转 驼峰 格式
     */
    public static String underline2Camel(String param) {
        if (param == null || param.length() == 0) {
            return Const.STRING_LEN0;
        }
        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        char underLine = Const.UNDERLINE.charAt(0);
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
            if (c == underLine) {
                if (++i < len) {
                    sb.append(Character.toUpperCase(param.charAt(i)));
                }
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
    
    
    /**
     * 类似于 String.substring(int index)，只不过index从后面算起
     * <p>等价于：str.substring(0, str.length() - lastIndex)
     */
    public static String lastSubstring(String str, int lastIndex) {
        int idx = str.length();
        idx = idx - lastIndex;
        if (idx <= 0) {
            return Const.STRING_LEN0;
        }
        return str.substring(0, idx);
    }
    
    
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓
    // String Index 查找 相关算法工具
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛
    
    /*
     * 类似于 replace(str, index, "")，但是只会替换匹配到的第一个值
     */
    public static String stripIndex(String str, String index) {
        int idx = str.indexOf(index);
        if (idx == -1) {
            return null;
        }
        return str.substring(0, idx) 
                + str.substring(idx + index.length(), str.length());
    }

    public static String afterIndex(String str, String index) {
        int idx = str.indexOf(index);
        if (idx == -1) {
            return null;
        }
        return str.substring(idx + index.length(), str.length());
    }
    
    public static String afterLastIndex(String str, String index) {
        int idx = str.lastIndexOf(index);
        if (idx == -1) {
            return null;
        }
        return str.substring(idx + index.length(), str.length());
    }

    public static String beforeIndex(String str, String index) {
        int idx = str.indexOf(index);
        if (idx == -1) {
            return null;
        }
        return str.substring(0, idx);
    }
    
    public static String beforeLastIndex(String str, String index) {
        int idx = str.lastIndexOf(index);
        if (idx == -1) {
            return null;
        }
        return str.substring(0, idx);
    }

    /**
     * ("01234567890123", "012", "012") = "3456789"
     */
    public static String middleOfIndex(String str, String index1, String index2) {
        int idx1 = str.indexOf(index1);
        if (idx1 == -1) {
            return null;
        }
        str = str.substring(idx1 + index1.length(), str.length());
        int idx2 = str.indexOf(index2);
        if (idx2 == -1) {
            return null;
        }
        return str.substring(0, idx2);
    }
    
    /**
     * ("01234567890123", "89", "12") = "012345673"
     */
    public static String stripMiddleOfIndex(String str, String index1, String index2) {
        int idx1 = str.indexOf(index1);
        if (idx1 == -1) {
            return str;
        }
        String org = str;
        String before = org.substring(0, idx1);
        str = org.substring(idx1 + index1.length(), org.length());
        int idx2 = str.indexOf(index2);
        if (idx2 == -1) {
            return org;
        }
        return before + str.substring(idx2 + index2.length(), str.length());
    }
    
    /**
     * ("01234567890123", "89", "12", "aaa") = "0123456789aaa123"
     */
    public static String replaceBetween(String str, String index1, String index2, String newStr) {
        int idx1 = str.indexOf(index1);
        if (idx1 == -1) {
            return str;
        }
        String org = str;
        String before = org.substring(0, idx1);
        str = org.substring(idx1 + index1.length(), org.length());
        int idx2 = str.indexOf(index2);
        if (idx2 == -1) {
            return org;
        }
        return before + index1 + newStr + index2
                + str.substring(idx2 + index2.length(), str.length());
    }
    
    /**
     * ("01234567890123", "89", "12", "aaa") = "01234567aaa3"
     */
    public static String replaceMiddleOfIndex(String str, String index1, String index2, String newStr) {
        int idx1 = str.indexOf(index1);
        if (idx1 == -1) {
            return str;
        }
        String org = str;
        String before = org.substring(0, idx1);
        str = org.substring(idx1 + index1.length(), org.length());
        int idx2 = str.indexOf(index2);
        if (idx2 == -1) {
            return org;
        }
        return before + newStr + str.substring(idx2 + index2.length(), str.length());
    }
    
    /**
     * Test whether the given string start with the given substring at the given index.
     * @param str the original string (or StringBuilder)
     * @param index the index in the original string to start matching against
     * @param substring the substring to match at the given index
     */
    public static boolean startsWithFromIndex(CharSequence str, int index, CharSequence substring) {
        if (index + substring.length() > str.length()) {
            return false;
        }
        for (int i = 0; i < substring.length(); i++) {
            if (str.charAt(index + i) != substring.charAt(i)) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * 从某个位置开始匹配，如果匹配到，则返回匹配位置的index，否则返回-1
     * @param str the original string (or StringBuilder)
     * @param index the index in the original string to start matching against
     * @param match the substring to match at the given index
     */
    public static int matchFromIndex(CharSequence str, int index, String match) {
        while (index < str.length()) {
            boolean flag = true;
            for (int i = 0; i < match.length(); i++) {
                if (str.charAt(index + i) != match.charAt(i)) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                return index;
            }
            index++;
        }
        return -1;
    }
    
    
    /**
     * Count the occurrences of the substring in string s.
     * 
     * @param str string to search in. Return 0 if this is null.
     * @param sub string to search for. Return 0 if this is null.
     */
    public static int countOccurrencesOf(String str, String sub) {
        if (str == null || sub == null || str.length() == 0 || sub.length() == 0) {
            return 0;
        }
        int count = 0;
        int pos = 0;
        int idx;
        while ((idx = str.indexOf(sub, pos)) != -1) {
            ++count;
            pos = idx + sub.length();
        }
        return count;
    }
    
    /**
     * 判断两个字符是否相等（忽略大小写）。
     * @return 若是英文字母，不区分大小写，相等true，不等返回false； 
     *  若不是则区分，相等返回true，不等返回false。
     */
    public static boolean charEqualIgnoreCase(char c1, char c2) {
        // 先判断c1和c2都是字母（65-90,97-122），然后判断c1==c2
        return (((97 <= c1 && c1 <= 122) || (65 <= c1 && c1 <= 90)) 
                && ((97 <= c2 && c2 <= 122) || (65 <= c2 && c2 <= 90))
                && ((c1 - c2 == 32) || (c2 - c1 == 32))) ? true : c1 == c2;
    }
    
    /**
     * Returns true if and only if this string contains the specified
     * sequence of char values. (Case in-sensitive)
     *
     * @param subject  the String to check, may be null
     * @param search  the String to search for
     * @return true if this string contains {@code search}, false otherwise
     */
    public static boolean containsIgnoreCase(String subject, String search) {
        return indexIgnoreCase(subject, search) > -1;
    }
    
    /**
     * <p>Case in-sensitive find of the first index within a String.</p>
     *
     * <p>A <code>null</code> String will return <code>-1</code>.
     * A negative start position is treated as zero.
     * An empty ("") search String always matches.
     * A start position greater than the string length only matches
     * an empty search String.</p>
     *
     * <pre>
     * StringUtils.indexIgnoreCase(null, *)          = -1
     * StringUtils.indexIgnoreCase(*, null)          = -1
     * StringUtils.indexIgnoreCase("", "")           = 0
     * StringUtils.indexIgnoreCase("aabaabaa", "a")  = 0
     * StringUtils.indexIgnoreCase("aabaabaa", "b")  = 2
     * StringUtils.indexIgnoreCase("aabaabaa", "ab") = 1
     * </pre>
     *
     * @param subject  the String to check, may be null
     * @param search  the String to find, may be null
     * @return the first index of the search String,
     *  -1 if no match or <code>null</code> string input
     */
    public static int indexIgnoreCase(String subject, String search) {
        return indexIgnoreCase(subject, search, 0);
    }
    
    /**
     * see {@link #indexIgnoreCase(String, String)}
     */
    public static int indexIgnoreCase(String subject, String search, int fromIndex) {
        if (subject == null || search == null) {
            return -1;
        }
        if (fromIndex < 0) {
            fromIndex = 0;
        }
        if (search.length() == 0) {
            return fromIndex;
        }
        int index1 = fromIndex;
        int index2 = 0;

        char c1;
        char c2;
        int slen = subject.length();
        int tlen = search.length() - 1;
        loop1: while (true) {

            if (index1 < slen) {
                c1 = subject.charAt(index1);
                c2 = search.charAt(index2);

            } else {
                break loop1;
            }

            while (true) {
                if (StringUtils.charEqualIgnoreCase(c1, c2)) {

                    if (index1 < slen - 1 && index2 < tlen) {

                        c1 = subject.charAt(++index1);
                        c2 = search.charAt(++index2);
                    } else if (index2 == tlen) {

                        return fromIndex;
                    } else {

                        break loop1;
                    }

                } else {
                    // 在比较时，发现查找子字符串中某个字符不匹配，则重新开始查找子字符串
                    index2 = 0;
                    break;
                }
            }
            // 重新查找子字符串的位置
            index1 = ++fromIndex;
        }

        return -1;
    }
    
    /**
     * <p>Case in-sensitive find of the last index within a String.</p>
     *
     * <p>A <code>null</code> String will return <code>-1</code>.
     * A negative start position returns <code>-1</code>.
     * An empty ("") search String always matches unless the start position is negative.
     * A start position greater than the string length searches the whole string.</p>
     *
     * <pre>
     * StringUtils.lastIndexOfIgnoreCase(null, *)          = -1
     * StringUtils.lastIndexOfIgnoreCase(*, null)          = -1
     * StringUtils.lastIndexOfIgnoreCase("aabaabaa", "A")  = 7
     * StringUtils.lastIndexOfIgnoreCase("aabaabaa", "B")  = 5
     * StringUtils.lastIndexOfIgnoreCase("aabaabaa", "AB") = 4
     * </pre>
     *
     * @param subject  the String to check, may be null
     * @param search  the String to find, may be null
     * @return the first index of the search String,
     *  -1 if no match or <code>null</code> string input
     */
    public static int lastIndexIgnoreCase(String subject, String search) {
        return lastIndexIgnoreCase(subject, search, subject.length());
    }

    /**
     * see {@link #lastIndexIgnoreCase(String, String)}
     */
    public static int lastIndexIgnoreCase(String subject, String search, int fromIndex) {
        if (subject == null || search == null) {
            return -1;
        }
        int slen = subject.length();
        int tlen = search.length();
        if (fromIndex > (slen - tlen)) {
            fromIndex = slen - tlen;
        }
        if (fromIndex < 0) {
            return -1;
        }
        if (search.length() == 0) {
            return fromIndex;
        }

        int index1 = fromIndex;
        int index2 = 0;

        char c1;
        char c2;
        tlen = tlen - 1;
        loop1: while (true) {

            if (index1 >= 0) {
                c1 = subject.charAt(index1);
                c2 = search.charAt(index2);
            } else {
                break loop1;
            }

            while (true) {
                if (StringUtils.charEqualIgnoreCase(c1, c2)) {
                    if (index1 < slen - 1 && index2 < tlen) {

                        c1 = subject.charAt(++index1);
                        c2 = search.charAt(++index2);
                    } else if (index2 == tlen) {

                        return fromIndex;
                    } else {

                        break loop1;
                    }
                } else {
                    // 在比较时，发现查找子字符串中某个字符不匹配，则重新开始查找子字符串
                    index2 = 0;
                    break;
                }
            }
            // 重新查找子字符串的位置
            index1 = --fromIndex;
        }

        return -1;
    }
    
    
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓
    // String fileName and Path 相关工具
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛
    
    /**
     * @deprecated please use PathUtils
     */
    public static String normalPath(String path) {
        return PathUtils.normalPath(path);
    }

    /**
     * @deprecated please use PathUtils
     */
    public static String getFilenameFromPath(String path) {
        return PathUtils.getFilenameFromPath(path);
    }

    /**
     * @deprecated please use PathUtils
     */
    public static String stripFilenameFromPath(String path) {
        return PathUtils.stripFilenameFromPath(path);
    }

    /**
     * @deprecated please use PathUtils
     */
    public static String getFilenameWithoutExtension(String path) {
        return PathUtils.getFilenameWithoutExtension(path);
    }

    /**
     * @deprecated please use PathUtils
     */
    public static String getFilenameExtension(String path) {
        return PathUtils.getFilenameExtension(path);
    }

    /**
     * @deprecated please use PathUtils
     */
    public static String applyRelativePath(String path, String relativePath) {
        return PathUtils.applyRelativePath(path, relativePath);
    }
    
    /**
     * @deprecated please use PathUtils
     */
    public static String connectPaths(String ...paths) {
        return PathUtils.connectPaths(paths[0], paths[1]);
    }

    /**
     * @deprecated please use PathUtils
     */
    public static String cleanPath(String path) {
        return PathUtils.cleanPath(path);
    }

}
