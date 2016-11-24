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

import java.util.List;
import java.util.Locale;
import java.util.Properties;

/**
 * 基于Properties文件的KeyValueMsg的实现。
 * <br>
 * 借助于 I18nMsgProperties （Locale.ROOT） 作为内部实现。
 * <br>
 * 配置示例：
 * <bean id="keyValueMsg" class="org.zollty.util.msg.KeyValueMsgProperties">
    <property name="modulePath" value="config/properties/ErrorMessage_zh_CN" />
    <property name="defaultFile">
      <list>
        <value>global_error_message.properties</value>
      </list>
    </property>
  </bean>
 * 
 * @author zollty
 * @since 2016-3-20
 */
public class KeyValueMsgProperties implements KeyValueMsg {
    
    private static final Locale DEFAULT_LOCALE = Locale.ROOT;
    
    private I18nMsgProperties i18nMsg;
    
    private String modulePath;
    
    private List<String> defaultFile;
    
    public KeyValueMsgProperties() {
    	this.setModulePath(null);
    }

    /**
     * @param modulePath properties的文件名（全路径）
     */
    public KeyValueMsgProperties(String modulePath) {
        this.setModulePath(modulePath);
    }
    
    /**
     * 用于从外部自行添加Properties文件
     */
    public void addProps(List<Properties> props) {
        this.i18nMsg.addProps(DEFAULT_LOCALE, props);
    }
    
    /**
     * 用于从外部自行添加Properties文件
     * @param fileName Properties文件名称（全路径）
     */
    public void addFile(String fileName) {
        this.i18nMsg.addFile(DEFAULT_LOCALE, fileName);
    }
	
    /**
     * 设置modulePath（即文件名去掉properties后缀），配置不会立即加载，第一次用到时获取
     * @param modulePath properties的文件名（去掉properties后缀）
     */
	public void setModulePath(String modulePath) {
		this.modulePath = modulePath;
		this.i18nMsg = new I18nMsgProperties(modulePath);
	}
	
	/**
	 * 设置defaultFile（即默认引入的properties的文件名），
	 * 会按顺序把配置加载到队列的前面。使用时优先从这些properties的文件中获取值。
	 * @param defaultFile 默认引入的properties的文件名（全路径）
	 */
	public void setDefaultFile(List<String> defaultFile) {
		if (defaultFile == null || defaultFile.isEmpty()) {
			return;
		}
		this.defaultFile = defaultFile;
		for (String fileName : this.defaultFile) {
			this.addFile(fileName);
		}
	}

    @Override
    public String getString(String key) {
        return this.i18nMsg.getString(key, DEFAULT_LOCALE);
    }

    @Override
    public Long getLong(String key) {
        return this.i18nMsg.getLong(key, DEFAULT_LOCALE);
    }

    @Override
    public Integer getInteger(String key) {
        return this.i18nMsg.getInteger(key, DEFAULT_LOCALE);
    }

    @Override
    public Double getDouble(String key) {
        return this.i18nMsg.getDouble(key, DEFAULT_LOCALE);
    }

    @Override
    public Float getFloat(String key) {
        return this.i18nMsg.getFloat(key, DEFAULT_LOCALE);
    }

    @Override
    public Byte getByte(String key) {
        return this.i18nMsg.getByte(key, DEFAULT_LOCALE);
    }

    public String getModulePath() {
        return modulePath;
    }

	public List<String> getDefaultFile() {
		return defaultFile;
	}

}
