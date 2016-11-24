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
 * Create by ZollTy on 2014-5-17 (http://blog.zollty.com/, zollty@163.com)
 */
package org.jretty.util.json;

import java.util.Collection;
import java.util.Map;

import javax.management.openmbean.CompositeData;
import javax.management.openmbean.TabularData;


/**
 * 
 * @author zollty
 * @since 2014-5-17
 */
public interface JSONWriter {
    
    /**
     * Facade of all JSON Writer
     */
    Object writeObject(Object o);
    
    
    
    // ~ Common support methods ---------------
    
    Object writeArray(Object[] array);
    
    Object writeCollection(Collection<Object> list);
    
    Object writeMap(Map<String, Object> map);
    
    
    
    // ~ Extend methods ---------------
    
    Object writeError(Throwable error);
    
    Object writeTabularData(TabularData tabularData);
    
    Object writeCompositeData(CompositeData compositeData);

}
