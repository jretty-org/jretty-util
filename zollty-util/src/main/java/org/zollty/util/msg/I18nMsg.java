package org.zollty.util.msg;

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
