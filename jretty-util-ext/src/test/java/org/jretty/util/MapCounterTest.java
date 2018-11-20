/**
 * 
 */
package org.jretty.util;

import org.junit.Test;

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
        
        System.out.println(mc);
        
    }

}
