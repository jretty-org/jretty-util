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
 * Create by ZollTy on 2014-7-28 (http://blog.zollty.com/, zollty@163.com)
 */
package org.jretty.util;

import java.util.Locale;

/**
 * java.util.Locale增强工具，String转Locale对象
 * 
 * @author zollty
 * @since 2014-7-28
 */
public class LocaleUtils {
    
    private static final  Locale DEFAULT_LOCALE = Locale.US;

    public static final  String IETF_SEPARATOR = "-";

    public static final  String SEPARATOR = "_";

    public static final  String EMPTY_STRING = "";

    private LocaleUtils(){
    }
    
    /**
     * String转Locale对象
     * @param localeStr 例如 zh_CN
     * @return Locale对象
     */
    public static Locale toLocale(String localeStr) {
        return toLocale(localeStr, SEPARATOR, DEFAULT_LOCALE);
    }
    
    /**
     * String转Locale对象
     * @param localeStr 例如 zh_CN
     * @param defaultLocale 当localeStr为空的时候，返回的默认值
     * @return Locale对象
     */
    public static Locale toLocale(String localeStr, Locale defaultLocale) {
        return toLocale(localeStr, SEPARATOR, defaultLocale);
    }

    /**
     * String转Locale对象
     * @param localeStr 例如 zh_CN
     * @param separator 分割符，例如zh_CN分割符为"_"
     * @param defaultLocale 当localeStr为空的时候，返回的默认值
     * @return Locale对象
     */
    public static Locale toLocale(String localeStr, String separator, Locale defaultLocale) {
        if (StringUtils.isNullOrEmpty(localeStr)) {
            return defaultLocale;
        }
        if(StringUtils.isNullOrEmpty(separator)) {
            throw new IllegalArgumentException("separator can't be empty!");
        }
        
        String language = EMPTY_STRING;
        String country = EMPTY_STRING;
        String variant = EMPTY_STRING;
        
        int i1 = localeStr.indexOf(separator);
        if (i1 < 0) {
            language = localeStr;
        } else {
            language = localeStr.substring(0, i1);
            ++i1;
            int i2 = localeStr.indexOf(separator, i1);
            if (i2 < 0) {
                country = localeStr.substring(i1);
            } else {
                country = localeStr.substring(i1, i2);
                variant = localeStr.substring(i2 + 1);
            }
        }

        if (language.length() == 2) {
            language = language.toLowerCase();
        } else {
            language = EMPTY_STRING;
        }

        if (country.length() == 2) {
            country = country.toUpperCase();
        } else {
            country = EMPTY_STRING;
        }

        if ((variant.length() > 0)
                && ((language.length() == 2) || (country.length() == 2))) {
            variant = variant.toUpperCase();
        } else {
            variant = EMPTY_STRING;
        }

        return new Locale(language, country, variant);
    }
}