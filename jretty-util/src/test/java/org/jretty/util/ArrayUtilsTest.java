package org.jretty.util;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

public class ArrayUtilsTest {
    
    @Test
    public void arrayToSetTest() {
        
        Double[] d1 = new Double[] { 0.000001, 3.1215926 };
        Set<Double> set = new LinkedHashSet<Double>(Arrays.asList(d1));
        
        // [1.0E-6, 3.1215926]
        Assert.assertEquals("[1.0E-6, 3.1215926]", set.toString());
    }
    
    @Test
    public void addTest() {
        Double[] d1 = new Double[] { 0.000001, 3.1215926 };
        
        Double[] d2 = ArrayUtils.add(d1, 0.34354);
        
        Assert.assertEquals("1.0E-6, 3.1215926, 0.34354", ArrayUtils.toString(d2, ", "));
        
        d2 = ArrayUtils.add(null, new Double(0.34354));
        Assert.assertEquals("0.34354", ArrayUtils.toString(d2, ", "));
        
    }
    
    @Test
    public void arrayRemove() {
        
        String[] arr = null;
        
        Assert.assertNull( ArrayUtils.remove(arr, 1) );
        
        
        arr = new String[]{"aa"};
        
        Assert.assertFalse( ArrayUtils.isNullOrEmpty( arr ) );
        Assert.assertTrue( ArrayUtils.isNullOrEmpty( ArrayUtils.remove(arr, 0) ) );
        
        arr = new String[]{"aa","bb"};
        Assert.assertEquals(1, ArrayUtils.remove(arr, 0).length);
        Assert.assertEquals("bb", ArrayUtils.remove(arr, 0)[0]);
        
        
        Double[] d1 = new Double[] { 0.000001, 3.1215926 };
        
        Double[] d2 = ArrayUtils.remove(d1, 0);
        
        Assert.assertEquals("3.1215926", ArrayUtils.toString(d2, ", "));
        
        //d2 = ArrayUtils.add(null, new Double(0.34354));
        //Assert.assertEquals("0.34354", ArrayUtils.toString(d2, ", "));
        
        
    }
    
//    // 6936
//    @Test
//    public void arrayRemovePerf() throws Exception {
//        List<String> alist = new ArrayList<String>();
//        for(int i=0; i<10000; i++) {
//            alist.add("aaa"+i);
//        }
//        
//        final String[] arr = alist.toArray(new String[alist.size()]);
//        
//        org.zollty.tesper.TestTools.loopExecute(new LoopExecute() {
//            
//            @Override
//            public int getLoopTimes() {
//                return 1000000;
//            }
//            
//            @Override
//            public void execute() throws Exception {
//                ArrayUtils.remove(arr, 5000);
//            }
//        });
//    }
    
    @Test
    public void toStringTest() {
        Double[] d1 = new Double[] { 0.000001, 3.1215926 };
        // 1.0E-6,3.1215926
        Assert.assertEquals("1.0E-6,3.1215926", ArrayUtils.toString(d1));
    }
    
    
    @Test
    public void isEqualsTest() {

        double[] d1 = new double[] { 0.000001, 3.1215926 };

        double[] d2 = new double[] { 0.000001, 3.1215926 };

        boolean ret = ArrayUtils.isEquals(d1, d2);

        Assert.assertTrue(ret);

        // 多一个0： 0.0000
        d2 = new double[] { 0.0000001, 3.1215926 };

        ret = ArrayUtils.isEquals(d1, d2);

        Assert.assertFalse(ret);

        d2 = null;

        ret = ArrayUtils.isEquals(d1, d2);

        Assert.assertFalse(ret);
        
        
        d1 = null;

        ret = ArrayUtils.isEquals(d1, d2);

        Assert.assertTrue(ret);

    }

}
