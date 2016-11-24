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
package org.jretty.util.json.support;

import org.jretty.util.json.DefaultWriter;

/**
 * 
 * @author zollty
 * @since 2014-5-17
 */
public class SimpleStringWriter implements ObjectWriter<String> {
    
    @Override
    public void write(DefaultWriter out, String text) {
        
        out.write('"');
        for (int i = 0; i < text.length(); ++i) {
            char c = text.charAt(i);
            if (c == '"') {
                out.write("\\\"");
            } else if (c == '\n') {
                out.write("\\n");
            } else if (c == '\r') {
                out.write("\\r");
            } else if (c == '\\') {
                out.write("\\\\");
            } else if (c == '\t') {
                out.write("\\t");
            } else if (c < 16) {
                out.write("\\u000");
                out.write(Integer.toHexString(c));
            } else if (c < 32) {
                out.write("\\u00");
                out.write(Integer.toHexString(c));
            } else if (c >= 0x7f && c <= 0xA0) {
                out.write("\\u00");
                out.write(Integer.toHexString(c));
            } else {
                out.write(c);
            }
        }
        out.write('"');
        
    }

}
