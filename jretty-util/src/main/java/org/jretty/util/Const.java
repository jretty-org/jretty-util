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
 * Create by ZollTy on 2013-6-13 (http://blog.zollty.com/, zollty@163.com)
 */
package org.jretty.util;

/**
 * 常用字符串常量
 * 
 * @author zollty
 * @since 2013-6-13
 */
public class Const {

    /** ISO-8859-1编码(ISO-LATIN-1)，通常用于网络数据传输的默认编码 range 0000-00FF (0-255)  */
    public static final String ISO_8859_1 = "ISO-8859-1";
    /** Seven-bit ASCII, also known as ISO646-US, also known as the Basic Latin block of the Unicode character set. */
    public static final String US_ASCII = "US-ASCII";
    /** UTF-8编码 */
    public static final String UTF_8 = "UTF-8";
    /** UTF-16编码 */
    public static final String UTF_16 = "UTF-16";
    /** GBK编码 */
    public static final String GBK = "GBK";
    /** GB2312编码 */
    public static final String GB2312 = "GB2312";
    
    /** 全局默认编码 */
    public static final String DEFAULT_CHARSET = UTF_8;
    
    /** 文件分隔符'/'，用于Unix等系统 */
    public static final String FOLDER_SEPARATOR = "/";
    /** WIN系统文件分隔符'\\'用于MS Windows系统 */
    public static final String WINDOWS_FOLDER_SEPARATOR = "\\";
    
    /** 路径标识：当前目录 */
    public static final String CURRENT_PATH = ".";
    /** 路径标识：上一级目录 */
    public static final String TOP_PATH = "..";
    
}
