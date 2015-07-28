package org.zollty.util.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;

public class JSONUtilsTest3 {
    
    private boolean isDebug = false;
    
    @Test
    public void testNullParse() {
        
        
        Assert.assertEquals(
                JSON.parse(null)
                ,JSON.parse("null")
                );
        
        Assert.assertEquals(
                JSON.parse(null)
                ,JSONUtils.parse("null")
                );
        
        Assert.assertEquals(
                JSON.parse("{}")
                ,JSONUtils.parse("{}")
                );
        
        Assert.assertEquals(
                JSON.parse("[]")
                ,JSONUtils.parse("[]")
                );
        
        Map<String, Object> innerMap = new HashMap<String, Object>();
        innerMap.put("a", null);
        
        Assert.assertEquals(
                JSON.parse("{\"a\":{}}")
                ,JSONUtils.parse("{\"a\":{}}")
                );
        
        Assert.assertEquals(
                innerMap
                ,JSONUtils.parse("{\"a\":null}")
                );
        
        innerMap = new HashMap<String, Object>();
        innerMap.put("aa", null);
        innerMap.put("bb", new HashMap<String, Object>());
        
        Assert.assertEquals(
                innerMap
                ,JSONUtils.parse(JSONUtils.toJSONString(innerMap))
                );
        
        // 嵌套Map
        innerMap = new HashMap<String, Object>();
        innerMap.put("aa", null);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("aa", null);
        map.put("bb", innerMap);
        
        Assert.assertEquals(
                map
                ,JSONUtils.parse(JSONUtils.toJSONString(map))
                );
        
    }
    
    @Test
    public void testNull() {
        
        
        Assert.assertEquals(
                JSON.toJSONString(new Map[0])
                ,JSONUtils.toJSONString(new Map[0])
                );
        
        Assert.assertEquals(
                JSON.toJSONString(new ArrayList[0])
                ,JSONUtils.toJSONString(new ArrayList[0])
                );
        
        Assert.assertEquals(
                JSON.toJSONString(new Set[0])
                ,JSONUtils.toJSONString(new Set[0])
                );
        
        Assert.assertEquals(
                JSON.toJSONString(new String[0])
                ,JSONUtils.toJSONString(new String[0])
                );
        
        Assert.assertEquals(
                JSON.toJSONString(null)
                ,JSONUtils.toJSONString(null)
                );
        
        
        
        Gson gson = new Gson();
        
        if(isDebug) {
            Map<String, Object> innerMap = new HashMap<String, Object>();
            innerMap.put("aa", null);
            innerMap.put("bb", new HashMap<String, Object>());
            innerMap.put("cc", new String[0]);
            
            System.out.println(gson.toJson(innerMap));
            System.out.println(JSON.toJSONString(innerMap));
            System.out.println(JSONUtils.toJSONString(innerMap));
        }
        
    }

}
