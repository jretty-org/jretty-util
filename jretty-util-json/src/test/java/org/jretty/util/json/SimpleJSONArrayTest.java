package org.jretty.util.json;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class SimpleJSONArrayTest {
    
    
    @Test
    public void test() {
        
        List<SimpleJSON> list = new ArrayList<SimpleJSON>();
        list.add(SimpleJSON.getInstance().addItem("xxxa", 1234));
        list.add(SimpleJSON.getInstance().addItem("bbbx", 0.34d));
        
        SimpleJSON ret = SimpleJSON.getInstance()
            .addItem("name", getClass())
            .addItem("list", SimpleJSON.toSimpleJSONArray(list));
        
        Assert.assertEquals(
                "{\"name\":\"org.jretty.util.json.SimpleJSONArrayTest\",\"list\":[{\"xxxa\":1234},{\"bbbx\":0.34}]}"
                , ret.toString());
    }
    
    
    @Test
    public void testNull() {
        
        SimpleJSON ret = SimpleJSON.getInstance()
            .addItem("name", getClass())
            .addItem("list", SimpleJSON.toSimpleJSONArray(null));
        
        Assert.assertEquals(
                "{\"name\":\"org.jretty.util.json.SimpleJSONArrayTest\",\"list\":null}"
                , ret.toString());
        
        
        ret = SimpleJSON.getInstance()
                .addItem("name", getClass())
                .addItem("list", SimpleJSON.toSimpleJSONArray(new ArrayList<SimpleJSON>()));
            
        Assert.assertEquals(
                "{\"name\":\"org.jretty.util.json.SimpleJSONArrayTest\",\"list\":[]}"
                , ret.toString());
    }

}
