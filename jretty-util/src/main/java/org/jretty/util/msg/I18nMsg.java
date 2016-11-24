/* 
 * Copyright (C) 2016-2017 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License").
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * Create by ZollTy on 2016-3-20 (http://blog.zollty.com/, zollty@163.com)
 */
package org.jretty.util.msg;

import java.util.Locale;

/**
 * 根据key和{@link java.util.Locale Locale}（语言_国家_地区），获取国际化的消息内容
 * 
 * @author zollty
 * @since 2016-3-20
 */
public interface I18nMsg {
    
	/**
	 * 根据key和Locale（语言_国家_地区），获取国际化的消息内容
	 */
    public String getString(String key, Locale locale);
    
    /**
	 * 根据key和Locale（语言_国家_地区），获取国际化的消息内容
	 */
    public Long getLong(String key, Locale locale);
    
    /**
	 * 根据key和Locale（语言_国家_地区），获取国际化的消息内容
	 */
    public Integer getInteger(String key, Locale locale);
    
    /**
	 * 根据key和Locale（语言_国家_地区），获取国际化的消息内容
	 */
    public Double getDouble(String key, Locale locale);
    
    /**
	 * 根据key和Locale（语言_国家_地区），获取国际化的消息内容
	 */
    public Float getFloat(String key, Locale locale);
    
    /**
	 * 根据key和Locale（语言_国家_地区），获取国际化的消息内容
	 */
    public Byte getByte(String key, Locale locale);
    
    /**
	 * 根据key和Locale（语言_国家_地区），获取国际化的消息内容
	 */
    public String getString(String key, String locale);
    
    /**
	 * 根据key和Locale（语言_国家_地区），获取国际化的消息内容
	 */
    public Long getLong(String key, String locale);
    
    /**
	 * 根据key和Locale（语言_国家_地区），获取国际化的消息内容
	 */
    public Integer getInteger(String key, String locale);
    
    /**
	 * 根据key和Locale（语言_国家_地区），获取国际化的消息内容
	 */
    public Double getDouble(String key, String locale);
    
    /**
	 * 根据key和Locale（语言_国家_地区），获取国际化的消息内容
	 */
    public Float getFloat(String key, String locale);
    
    /**
	 * 根据key和Locale（语言_国家_地区），获取国际化的消息内容
	 */
    public Byte getByte(String key, String locale);
    
}
