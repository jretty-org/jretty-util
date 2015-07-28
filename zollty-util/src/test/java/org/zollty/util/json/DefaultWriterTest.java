package org.zollty.util.json;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

public class DefaultWriterTest {
    
    @Test
    public void writeString() {
        
        Assert.assertFalse( stringInstanceof(new StringBuilder("str")));
        
        Assert.assertTrue( charSequenceInstanceof(new StringBuilder("str")));
        Assert.assertTrue( charSequenceInstanceof("str"));
        
        DefaultWriter writer = new JSONWriterDirector();
        writer.writeString("特殊字符" + "\n=\r=\t=\'=\"=\b=\f=\\=/=\u5201");
        String ret = writer.toString();
        
        Assert.assertEquals("\"特殊字符\\n=\\r=\\t='=\\\"=\\b=\\f=\\\\=/=刁\"", ret);
    }
    
    
    @Test
    public void writeDate() {
        DefaultWriter writer = new JSONWriterDirector();
        writer.writeDate(new Date(1434620908674L));
        String ret = writer.toString();
        
        Assert.assertEquals("\"2015-06-18 09:48:28\"", ret);
    }
    
    
    
    
    private boolean stringInstanceof(CharSequence o) {
        
        return o instanceof String;
    }
    
    private boolean charSequenceInstanceof(CharSequence o) {
        
        return o instanceof CharSequence;
    }

}
