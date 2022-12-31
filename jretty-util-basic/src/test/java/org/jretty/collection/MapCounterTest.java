package org.jretty.collection;

import org.jretty.util.MapCounter;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * 
 * @author zollty
 * @since 2018年10月10日
 */
public class MapCounterTest {
    
    @Test
    public void test() {
        MapCounter<Object> mc = new MapCounter<Object>();
        mc.add("aaa");
        mc.add("aaa");
        mc.add("bbb");
        mc.add("aaa");
        
        assertEquals(mc.getMap().get("aaa").get(), 3);
        assertEquals(mc.getMap().get("bbb").get(), 1);
        
    }

}
