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
 * Create by ZollTy on 2013-12-10 (http://blog.zollty.com/, zollty@163.com)
 */
package org.zollty.tool.json;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zollty 
 * @since 2013-12-10
 * @deprecated use JSONUtils replace
 */
@Deprecated
public class SimpleJsonWrapper {

	private Map<String, String> map;
	
	public static SimpleJsonWrapper getInstance(){
		return new SimpleJsonWrapper();
	}
	
	public SimpleJsonWrapper(){
		map = new HashMap<String, String>();
	}
	
	public void addItem(String key, Object value){
		if(key==null) {
		    throw new IllegalArgumentException("key is null");
		}
		if(value!=null){
			if(value instanceof CharSequence){
				map.put(key, SimpleJsonUtils.escapeValueOrName( value.toString() ));
			}else{
				map.put(key, value.toString());
			}
		}else{
			map.put(key, "");
		}
	}
	
	@Override
	public String toString(){
		return SimpleJsonUtils.simpleMapToJson(map);
	}
	
}
