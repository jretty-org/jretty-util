package org.jretty.util.json;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.jretty.util.json.support.DateWriter;
import org.jretty.util.json.support.ObjectWriter;

import com.alibaba.fastjson.JSON;

public class JSONUtilsTest2 {
    
    @Test
    public void testDateWriter() throws Exception{
        
        // ~ Date测试 ---
        
        Map<String, String> innerMap = new HashMap<String, String>();
        innerMap.put("aa", "11");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("aa", new Date());
        map.put("bb", innerMap);
        
        Assert.assertEquals(
                JSON.toJSONString(map), 
                JSONUtils.toJSONString(map, new DateWriter(true))
                );
    }
    
    @Test
    public void testArray() throws Exception{
        
        // ~ Array测试 ---
        
        Map<String, String> innerMap = new HashMap<String, String>();
        innerMap.put("aa", "11");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("aa", new String[]{"oooo", "-----"});
        map.put("bb", innerMap);
        Assert.assertEquals(
                JSON.toJSONString(map), 
                JSONUtils.toJSONString(map)
                );
        
    }
    
    @Test
    public void testExtWriter() throws Exception{
        Map<String, String> innerMap = new HashMap<String, String>();
        innerMap.put("aa", "11");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("aa", new TestWriter());
        map.put("bb", innerMap);
        
        String exp = "{\"aa\":{\"Class\":\"org.jretty.util.json.JSONUtilsTest2$TestWriter\"},\"bb\":{\"aa\":\"11\"}}";
        
        Assert.assertEquals(
                exp, 
                JSONUtils.toJSONString(map, new TestWriter())
                );
    }
    
    private class TestWriter implements ObjectWriter<TestWriter> {

        @Override
        public void write(DefaultWriter out, TestWriter e) {
            out.write("{\"Class\":");
            out.writeSimpleString(e.getClass().getName());
            out.write('}');
        }

    }

}
