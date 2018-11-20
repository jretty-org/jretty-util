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
 * Create by ZollTy on 2014-5-07 (http://blog.zollty.com/, zollty@163.com)
 */
package org.jretty.util.json;

/**
 * @author zollty
 * @since 2014-5-7
 */
public class SimpleJSONArray {

    private String jsonArray;

    public SimpleJSONArray(String jsonArray) {
        this.jsonArray = jsonArray;
    }

    @Override
    public String toString() {
        return jsonArray;
    }
}