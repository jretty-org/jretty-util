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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import org.jretty.log.LogFactory;
import org.jretty.log.Logger;
import org.jretty.util.CollectionUtils;
import org.jretty.util.LocaleUtils;
import org.jretty.util.NestedRuntimeException;
import org.jretty.util.ResourceUtils;
import org.jretty.util.resource.Resource;

/**
 * 基于Properties文件的I18nMsg的实现。
 * <br>
 * 在classpath*下面可以有多个相同名称的Properties文件。
 * 其中优先读取classpath下面的文件，再读取jar包中的文件。
 * <br>
 * 可以扩展message的读取范围，例如每个子项目有一个msg文件，然后还有一个共有的gloable_msg.properties文件，
 * 则可以通过{@link #addFile(Locale, String)}、{@link #addProps(Locale, List)}
 * 等方法把公用文件添加进去。
 * 
 * @author zollty
 * @since 2016-3-20
 */
public class I18nMsgProperties implements I18nMsg {
	
	private static final Logger LOG = LogFactory.getLogger(I18nMsgProperties.class);
    
    /**
     * Map<Locale, List<MSG_Properties>>
     */
    private Map<Locale, List<Properties>> msgHolder = new HashMap<Locale, List<Properties>>();
    
    private Map<Locale, List<Properties>> extMsgHolder = new HashMap<Locale, List<Properties>>();
    
    /**
     * 不包含Locale（语言_国家_地区）后缀的Properties文件路径（去掉.properties后缀后的全路径）
     */
    private String modulePath;
    
    public I18nMsgProperties() {
    }
    
    /**
     * @param modulePath 不包含Locale（语言_国家_地区）后缀的文件名（全路径）
     */
    public I18nMsgProperties(String modulePath) {
        this.modulePath = modulePath;
    }
    
    /**
     * 用于从外部自行添加Properties文件
     */
    public void addProps(String localeStr, List<Properties> props) {
        if(CollectionUtils.isNotEmpty(props)) {
            addProps(LocaleUtils.toLocale(localeStr), props);
        }
    }
    
    /**
     * 用于从外部自行添加Properties文件
     */
    public void addProps(Locale locale, List<Properties> props) {
        if(CollectionUtils.isNotEmpty(props)) {
            List<Properties> old = extMsgHolder.get(locale);
            if (old == null) {
                extMsgHolder.put(locale, props);
            } else {
                old.addAll(props);
            }
        }
    }
    
    /**
     * 用于从外部自行添加Properties文件
     * @param fileName Properties文件名（全路径）
     */
    public void addFile(String localeStr, String fileName) {
    	List<Properties> props;
    	try {
            props = getProps(fileName);
        } catch (IOException e) {
            throw new NestedRuntimeException(e);
        }
        
        addProps(localeStr, props);
    }
    
    /**
     * 用于从外部自行添加Properties文件
     * @param fileName Properties文件名（全路径）
     */
    public void addFile(Locale locale, String fileName) {
    	List<Properties> props;
    	try {
            props = getProps(fileName);
        } catch (IOException e) {
            throw new NestedRuntimeException(e);
        }
        
        addProps(locale, props);
    }

    /**
     * 根据baseName，即不包含Locale（语言_国家_地区）后缀的文件名（全路径），获取包含Locale的文件名
     * @param baseName 不包含Locale（语言_国家_地区）后缀的文件名（全路径）
     * @param locale
     * @return
     */
    protected static String toFileName(String baseName, Locale locale) {
        if (locale == Locale.ROOT) {
            return baseName + ".properties";
        }
        String loc = locale.toString();
        if (loc.length() != 0) {
            return baseName + "_" + loc + ".properties";
        } else {
            return baseName + ".properties";
        }
    }
    
    /**
     * 获取 "classpath*:" + fileName 下面的所有Properties
     * @param fileName Properties文件名（全路径）
     * @return
     * @throws IOException
     */
    protected static List<Properties> getProps(String fileName) throws IOException {
        Resource[] ress = ResourceUtils.getResourcesByPathMatchingResolver("classpath*:" + fileName);
        List<Properties> props = new ArrayList<Properties>();
        if (ress != null) {
        	LOG.debug("Got resource classpath*:{}, size={}", fileName, ress.length);
            for (int i = 0; i < ress.length; i++) {
                LOG.info("Got resource {}", ress[i].getURL());
                Properties prop = new Properties();
                prop.load(ress[i].getInputStream());
                props.add(prop);
            }
        }
        return props;
    }
    
    /**
     * @return 不包含Locale（语言_国家_地区）后缀的文件名（全路径）
     */
    public String getModulePath() {
        return modulePath;
    }
    
    /**
     * @param modulePath 不包含Locale（语言_国家_地区）后缀的文件名（全路径）
     */
    public void setModulePath(String modulePath) {
        this.modulePath = modulePath;
    }
    
    @Override
    public String getString(String key, Locale localLocale) {
        String ret = getStringFromModule(key, localLocale);
        if (ret == null) {
            List<Properties> props = extMsgHolder.get(localLocale);
            if (CollectionUtils.isNotEmpty(props)) {
                ret = findKeyInPropsList(key, props);
            }
        }
        return ret;
    }
    
    protected String getStringFromModule(String key, Locale localLocale) {
        String ret = null;
        List<Properties> props = msgHolder.get(localLocale);
        if (props == null) {
            synchronized (this) {
                props = msgHolder.get(localLocale);
                if (props == null) {
                    String fileName = toFileName(modulePath, localLocale);
                    try {
                        props = getProps(fileName);
                    } catch (IOException e) {
                        throw new NestedRuntimeException(e);
                    }
                    if (CollectionUtils.isNotEmpty(props)) {
                        msgHolder.put(localLocale, props);
                        ret = findKeyInPropsList(key, props);
                    }
                } else {
                    ret = findKeyInPropsList(key, props);
                }
            }
        } else {
            ret = findKeyInPropsList(key, props);
        }
        return ret;
    }
    
    private String findKeyInPropsList(String key, List<Properties> props) {
        String ret = null;
        for (Properties prop : props) {
            ret = prop.getProperty(key);
            if (ret != null) {
                break;
            }
        }
        return ret;
    }
    
    @Override
    public String getString(String key, String locale) {
        return getString(key, LocaleUtils.toLocale(locale));
    }

    @Override
    public Long getLong(String key, String locale) {
        return getLong(key, LocaleUtils.toLocale(locale));
    }

    @Override
    public Integer getInteger(String key, String locale) {
        return getInteger(key, LocaleUtils.toLocale(locale));
    }

    @Override
    public Double getDouble(String key, String locale) {
        return getDouble(key, LocaleUtils.toLocale(locale));
    }

    @Override
    public Float getFloat(String key, String locale) {
        return getFloat(key, LocaleUtils.toLocale(locale));
    }

    @Override
    public Byte getByte(String key, String locale) {
        return getByte(key, LocaleUtils.toLocale(locale));
    }

    @Override
    public Long getLong(String key, Locale locale) {
        String ret = getString(key, locale);
        return ret == null ? null : Long.valueOf(ret);
    }

    @Override
    public Integer getInteger(String key, Locale locale) {
        String ret = getString(key, locale);
        return ret == null ? null : Integer.valueOf(ret);
    }

    @Override
    public Double getDouble(String key, Locale locale) {
        String ret = getString(key, locale);
        return ret == null ? null : Double.valueOf(ret);
    }

    @Override
    public Float getFloat(String key, Locale locale) {
        String ret = getString(key, locale);
        return ret == null ? null : Float.valueOf(ret);
    }

    @Override
    public Byte getByte(String key, Locale locale) {
        String ret = getString(key, locale);
        return ret == null ? null : Byte.valueOf(ret);
    }

}
