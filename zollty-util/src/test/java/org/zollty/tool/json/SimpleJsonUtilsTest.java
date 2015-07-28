/* 
 * Copyright (C) 2014-2015 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License").
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * Create by ZollTy on 2015-3-17 (http://blog.zollty.com/, zollty@163.com)
 */
package org.zollty.tool.json;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.zollty.tesper.LoopExecute;
import org.zollty.tesper.TestTools;

/**
 * @author zollty
 * @since 2015-3-17
 */
@SuppressWarnings("deprecation")
public class SimpleJsonUtilsTest {
    
    
    @Test
    public void testMapToJson() {
        final Map<String, String> map = new HashMap<String, String>();
        map.put("aa", "11");
        map.put("bb", "22");
        map.put("cc", "33");
        map.put("dd", "44");
        map.put("ee", "55");
        
        // {"dd": "44", "ee": "55", "aa": "11", "bb": "22", "cc": "33"}
        String s = SimpleJsonUtils.mapToJson(map);
        Assert.assertEquals("{\"dd\": \"44\", \"ee\": \"55\", \"aa\": \"11\", \"bb\": \"22\", \"cc\": \"33\"}", s);
        
        s = SimpleJsonUtils.simpleMapToJson(map);
        Assert.assertEquals("{\"dd\": \"44\", \"ee\": \"55\", \"aa\": \"11\", \"bb\": \"22\", \"cc\": \"33\"}", s);
    }
    
    // 测试结果：
    // 执行 一千万次
    // SimpleJsonUtils.mapToJson(): 11572 ms. ( 0.0011572 ms/n)
    // SimpleJsonUtils.simpleMapToJson(): 11005 ms. ( 0.0011005 ms/n)
    // 二者性能的差距很小
    public static void main(String[] args) throws Exception {
        final Map<String, String> map = new HashMap<String, String>();
        map.put("aa", "11");
        map.put("bb", "22");
        map.put("cc", "33");
        map.put("dd", "44");
        map.put("ee", "55");
        
        String s = SimpleJsonUtils.mapToJson(map);
        
        TestTools.loopExecute(new LoopExecute() {
            
            @Override
            public int getLoopTimes() {
                return 10000000;
            }
            
            @Override
            public void execute() throws Exception {
                SimpleJsonUtils.simpleMapToJson(map);
            }
        });
        
        System.out.println(s);
    }

}
