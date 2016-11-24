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
