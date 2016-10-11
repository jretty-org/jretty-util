package org.zollty.util.msg;

/**
 * Key-Value形式的信息
 * 
 * @author zollty
 * @since 2016-3-20
 */
public interface KeyValueMsg {
    
	/**
	 * 根据key获取value
	 */
    public String getString(final String key);
    
    /**
	 * 根据key获取value
	 */
    public Long getLong(final String key);
    
    /**
	 * 根据key获取value
	 */
    public Integer getInteger(final String key);
    
    /**
	 * 根据key获取value
	 */
    public Double getDouble(final String key);
    
    /**
	 * 根据key获取value
	 */
    public Float getFloat(final String key);
    
    /**
	 * 根据key获取value
	 */
    public Byte getByte(final String key);
    
}
