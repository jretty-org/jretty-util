package org.zollty.util;

import org.junit.Test;
import org.junit.Assert;

public class ObjectUtilsTest {
    
    @Test
    public void testArrayToString () {
        
        Object[] params = new Object[]{"asdsda", new Long(1223), null, new Boolean(true)};
        
        String result = ObjectUtils.arrayToString(params);
        Assert.assertEquals("{[asdsda],[1223],null,[true]}", result);
        
        params = new Object[]{null};
        result = ObjectUtils.arrayToString(params);
        Assert.assertEquals("{null}", result);
        
        params = null;
        result = ObjectUtils.arrayToString(params);
        Assert.assertEquals("null", result);
    }

}
