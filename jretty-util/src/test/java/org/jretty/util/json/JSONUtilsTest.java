/*
 * @(#)JSONUtilsTest.java
 * Travelsky Report Engine (TRE) Source Code, Version 1.0.0
 * Author(s): 
 * Zollty Tsou (http://blog.csdn.net/zollty, zouty@travelsky.com)
 * Copyright (C) 2014-2015 Travelsky Technology. All rights reserved.
 */
package org.jretty.util.json;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;

/**
 * @author zollty
 * @since 2014-5-7
 */
public class JSONUtilsTest {
    
    @Test
    public void testParse() throws Exception {
        
        SimpleJSON jsonObj = new SimpleJSON();
        jsonObj.addItem("特殊字符", "\n=\r=\t=\'=\"=\b=\f=\\=/=\u5201");
        jsonObj.addItem("普通字符", "-5&5D测试。。，測試+-@#$%$%^^8**~`'");
        jsonObj.addItem("等价字符", "\4==\u0004-----'=\'");
        
        Object obj = JSONUtils.parse("{\"特殊字符\":\"\\n=\\r=\\t='=\\\"=\\b=\\f=\\\\=\\/=\u5201\",\"普通字符\":\"-5&5D测试。。，測試+-@#$%$%^^8**~`'\",\"等价字符\":\"\4==\4-----'='\"}");
        
        boolean isDebug = false;
        if(isDebug) {
            
            System.out.println(jsonObj.getMap());
            System.out.println(obj);
        }
        
        Assert.assertEquals(jsonObj.getMap(), obj);
        
        // ~ Map嵌套测试 ---
        
        Map<String, String> innerMap = new HashMap<String, String>();
        innerMap.put("aa", "11");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("aa", "11");
        map.put("bb", innerMap);
        
        // Map嵌套
        obj = JSONUtils.parse("{\"aa\":\"11\", \"bb\":{\"aa\":\"11\"}}");
        
        Assert.assertEquals(map, obj);
        
    }
    
    @Test
    public void testArrayToJSON() throws Exception {
        
        // ~ Array嵌套测试 ---
        
        Map<String, String> innerMap = new HashMap<String, String>();
        innerMap.put("aa", "11");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("aa", new String[]{"oooo", "-----"});
        map.put("bb", innerMap);
        
        Assert.assertEquals(JSON.toJSONString(map), JSONUtils.toJSONString(map));
        
        
        // map数组
        Map<String, String> innerMap1 = new HashMap<String, String>();
        innerMap.put("aa", "11");
        Map<String, String> innerMap2 = new HashMap<String, String>();
        innerMap.put("aa", "11");
        Map<String, String> innerMap3 = new HashMap<String, String>();
        innerMap.put("aa", "11");
        map = new HashMap<String, Object>();
        map.put("aa", new Map[]{innerMap1, innerMap2}); // map数组
        map.put("bb", innerMap3);
        
        Assert.assertEquals(JSON.toJSONString(map), JSONUtils.toJSONString(map));
        
    }
    
    @Test
    public void testToJSONString() {
        // 根据JSON.org标准。字符串定义如下：
        // A string is a sequence of zero or more Unicode characters, wrapped in double quotes
        // , using backslash escapes. A character is represented as a single character string.
        // 也就是说，可以分成两部分：
        // 第一部分，一般字符：Any Unicode character except " or \ or control character
        // 第二部分，特殊字符，需要用 反斜杠转义，包括： \ + ( " \ / b f n r t u+4位十六进制数 )
        
        
        // 首先注意下面的“等价”字符
        
        boolean ret = "'".equals("\'"); // \可以省略
        Assert.assertTrue(ret);
        
        //ret = ('"'=='\"'); // \可以省略
        //Assert.assertTrue(ret);
        
        ret = "\u5201".equals("刁");
        Assert.assertTrue(ret);
        
        ret = "\4".equals("\u0004"); // 简写的\\u字符，类似的还有\5 \6 \1 等
        Assert.assertTrue(ret);
        
        // \b 就是 \8
        ret = "\b".equals("\u0008"); // 简写的\\u字符
        Assert.assertTrue(ret);
        /** // 类似的简写字符还有：（可能不全）
        replaceChars['\b'] = 'b'; // 8
        replaceChars['\t'] = 't'; // 9
        replaceChars['\n'] = 'n'; // 10
        replaceChars['\u000B'] = 'v'; // 11
        replaceChars['\f'] = 'f'; // 12
        replaceChars['\r'] = 'r'; // 13
        replaceChars['\"'] = '"'; // 34
        replaceChars['\''] = '\''; // 39
        replaceChars['/'] = '/'; // 47
        replaceChars['\\'] = '\\'; // 92
         */
        
        boolean isDebug = false;
        if(isDebug) {
            
            // 然后注意 下面的特殊字符
            System.out.println( "\"" );
            System.out.println( "\\" );
            System.out.println( "/" );
            System.out.println( "\b" );
            System.out.println( "\f" );
            System.out.println( "\n" );
            System.out.println( "\r" );
            System.out.println( "\t" );
            System.out.println( "\u5201" );
        }
       
        SimpleJSON jsonObj = new SimpleJSON();
        jsonObj.addItem("特殊字符", "\n=\r=\t=\'=\"=\b=\f=\\=/=\u5201");
        //jsonObj.addItem("等价字符", "\4==\u0004-----'=\'");
        jsonObj.addItem("普通字符", "-5&5D测试。。，測試+-@#$%$%^^8**~`'");
        
        Assert.assertEquals(
                JSON.toJSONString(jsonObj.getMap()), 
                jsonObj.toString()
                );
        
        if(isDebug) {
            System.out.println(jsonObj.toString()); // OK
            System.out.println();
            
            System.out.println("--------------");
            System.out.println();
            System.out.println(JSON.toJSONString(jsonObj.getMap()));
            
            jsonObj.addItem("等价字符", "\4==\u0004-----'=\'");
            System.out.println();
            System.out.println(jsonObj.toString());
            System.out.println();
            System.out.println(JSON.toJSONString(jsonObj.getMap()));
            System.out.println();
            System.out.println(JSON.toJSONString(jsonObj.getMap(),SerializerFeature.BrowserCompatible));
        }
        
//        SimpleJsonWrapper jjj = new SimpleJsonWrapper();
//        jjj.addItem("特殊字符", "\n=\r=\t=\'=\"=\b=\f=\\=/=\u5201");
//        jjj.addItem("等价字符", "\4==\u0004-----'=\'");
//        jjj.addItem("普通字符", "-5&5D测试。。，測試+-@#$%$%^^8**~`'");
//        System.out.println(jjj.toString());
//        System.out.println();
        
    }
    
    @Test
    public void testToJSONString2() throws Exception {
        SimpleJSON ss1 = SimpleJSON.getInstance()
                .addItem("Level", "\n")
                .addItem("MM", false)
                .addItem("Name", "SLf4j");
        
        final SimpleJSON ss2 = SimpleJSON.getInstance()
                .addItem("str1", ss1)
                .addItem("Name", "SLf4j")
                .addItem("LL", 125L)
                .addItem("now", new Date());

        //System.out.println(ss2.toString());
        
        final SimpleJSON ss3 = SimpleJSON.getInstance()
                .addItem("str1", ss1.getMap())
                .addItem("Name", "SLf4j")
                .addItem("LL", 125L)
                .addItem("now", new Date());
        final SerializeConfig mapping = new SerializeConfig();
        mapping.put(Date.class, new SimpleDateFormatSerializer("yyyy-MM-dd HH:mm:ss"));
        
        Assert.assertEquals(JSON.toJSONString(ss3.getMap(), mapping), 
                ss2.toString());
    }

    @Test
    public void testSimpleJSONArrayToJSON() {
        List<SimpleJSON> list = new ArrayList<SimpleJSON>();
        for (int i = 0; i < 5; i++) {
            list.add(SimpleJSON.getInstance().addItem("Level", "\n" + i).addItem("Name", "SLf4j" + i));
        }
        SimpleJSON ret = SimpleJSON.getInstance()
            .addItem("lsit", SimpleJSON.toSimpleJSONArray(list))
            .addItem("KKK", "asnsdds")
            .addItem("WFD", 106);
        
        //System.out.println(ret.toJsonString());
        //System.out.println(ret.toString());
        
        Assert.assertNotNull(ret.toString());
    }

}
