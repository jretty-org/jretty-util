package org.zollty.util;

import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

public class ConvertUtilsTest {
    
    @Test
    public void testCollectionToString(){
        Set<String> set = new HashSet<String>();
        set.add("aa");
        set.add("bb");
        set.add("cc");
        set.add("dd");
        set.add("ee");
        
        Assert.assertEquals("dd, ee, aa, bb, cc", ConvertUtils.collectionToString(set, ", "));
    }

}
