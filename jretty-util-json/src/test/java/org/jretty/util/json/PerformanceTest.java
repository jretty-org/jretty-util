package org.jretty.util.json;

import java.util.HashMap;
import java.util.Map;

import org.jretty.tesper.LoopExecute;
import org.jretty.tesper.TestTools;
import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;

/**
 * ~ 性能测试 ---
 * 
 * 结论：
 * 
 * FastJson : 635 ms  ( 6.35E-4 ms/n)
 * JSONUtils: 862 ms  ( 8.62E-4 ms/n)
 * Gson     : 1283 ms ( 0.001283 ms/n)
 * 
 * toJSONString 的速度 比 FastJson 稍慢，接近FastJson，但比 Gson快很多。
 * 
 * 比 FastJson 慢一点的原因在于，FastJson对class的类型是精准匹配，
 * 而JSONUtils 采用的是模糊匹配，这样做虽然速度比FastJson慢一点，但是代码量要小得多。
 * 日常使用 JSONUtils 性能足够了，和FastJson在一个数量级，
 * 如下面测试的例子，每次toJSONString 只要 0.000862 ms。
 */
public class PerformanceTest {
    
    // @org.junit.Test
    public void testArray() throws Exception{
        
        Map<String, String> innerMap = new HashMap<String, String>();
        innerMap.put("aa", "11");
        final Map<String, Object> map = new HashMap<String, Object>();
        map.put("aa", new String[]{"oooo", "-----"});
        map.put("bb", innerMap);
        
        Assert.assertEquals(
                JSON.toJSONString(map), 
                JSONUtils.toJSONString(map)
                );
        
        TestTools.loopExecute(new LoopExecute() {
            
            @Override
            public int getLoopTimes() {
                return 1000;
            }
            
            @Override
            public void execute() throws Exception {
                JSON.toJSONString(map);
                JSONUtils.toJSONString(map);
            }
        });
        
        final int n = 1000000;
        TestTools.loopExecute(new LoopExecute() {
            
            @Override
            public int getLoopTimes() {
                return n;
            }
            
            @Override
            public void execute() throws Exception {
                JSONUtils.toJSONString(map);
            }
        });
        TestTools.loopExecute(new LoopExecute() {
            
            @Override
            public int getLoopTimes() {
                return n;
            }
            
            @Override
            public void execute() throws Exception {
                JSON.toJSONString(map);
            }
        });
        
        final Gson gson = new Gson();
        TestTools.loopExecute(new LoopExecute() {
            
            @Override
            public int getLoopTimes() {
                return n;
            }
            
            @Override
            public void execute() throws Exception {
                gson.toJson(map);
            }
        });
    }

}
