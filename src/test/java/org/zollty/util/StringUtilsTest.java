package org.zollty.util;

import org.junit.Test;

public class StringUtilsTest {
    
    @Test
    public void testReplaceParams(){
        
        org.junit.Assert.assertEquals(StringUtils.replaceParams("dss{}-{}-{}ssssssss", new String[]{"1111", null, "2222"}),
                "dss1111-null-2222ssssssss");
        
        org.junit.Assert.assertEquals(StringUtils.replaceParams("dssssssssss", new String[]{"1111", null, "2222"}),
                "dssssssssss 1111 null 2222");
        
        org.junit.Assert.assertEquals(StringUtils.replaceParams("dsssss{}sssss", new String[]{"1111", null, "2222"}),
                "dsssss1111sssss null 2222");
        
        org.junit.Assert.assertEquals(StringUtils.replaceParams("dssssssssss{}", new String[]{"1111", null, "2222"}),
                "dssssssssss1111 null 2222");
    }

}
