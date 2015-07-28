package org.zollty.util.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

public class JSONParserTest {
    
    @Test
    public void parseMap() {
        
        Map<String, String> innerMap = new HashMap<String, String>();
        innerMap.put("aa", "11");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("aa", "11");
        map.put("bb", innerMap);
        
        // Map嵌套
        JSONParser parser = new JSONParserDirector("{\"aa\":\"11\", \"bb\":{\"aa\":\"11\"}}");
        Map<String, Object> obj = parser.parseMap();
        
        Assert.assertEquals(map, obj);
        
    }
    
    @Test
    public void parseArray() {
        List<Object> ary = new ArrayList<Object>();
        ary.add("特殊字符" + "\n=\r=\t=\'=\"=\b=\f=\\=/=\u5201");
        ary.add("-----");
        
        // Map嵌套
        JSONParser parser = new JSONParserDirector("[\"特殊字符\\n=\\r=\\t='=\\\"=\\b=\\f=\\\\=/=刁\",\"-----\"]");
        List<Object> obj = parser.parseArray();
        
        Assert.assertEquals(ary, obj);
        
    }
    

}
