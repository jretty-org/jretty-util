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

import java.util.Date;

/**
 * 
 * @author zollty
 * @since 2014-5-17
 */
public interface DefaultWriter extends JSONWriter {
    
    
    // ~ inner basic write method ----
    /** inner basic write method: write text to out stream directly */
    void write(CharSequence text);
    /** inner basic write method: write data to out stream directly */
    void write(char[] data, int offset, int count);
    /** inner basic write method: write CHAR to out stream directly */
    void write(char c);
    /** inner basic write method: write INT to out stream directly */
    void write(int c);
    /** inner basic write method: write LONG to out stream directly */
    void write(long c);
    
    // ~ end---------------------------
    
    
    
    // ~ inner assistant method 
    /** inner assistant method: write null json value: null */
    void writeNull();
    /** 
     * inner assistant method: write String json value: "text". 
     * Look up: this method will not change or escape the string, distinguish from {@code writeString(String)}
     * @see #writeString(String)
     */
    void writeSimpleString(CharSequence text);
    
    // ~ end---------------------------
    
    
    
    // default support
    /** 
     * default support method: write String json value: "text". 
     * Look up: this method will change the string to compatible standard json format 
     * ( and may support other transform purpose such as browser html escape. )
     */
    void writeString(String value);
    
    // ~ end---------------------------
    
    
    
    // ~ extend methods ---------------
    /** 
     * extend method: write Date json value ,such as: "2014-12-03". 
     */
    void writeDate(Date date);
    
    // ~ end---------------------------
    

}
