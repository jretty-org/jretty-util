package org.jretty.array;

import org.jretty.entity.Foo;
import org.jretty.tesper.LoopExecute;
import org.jretty.tesper.TestTools;
import org.jretty.util.Arrays;
import org.junit.Assert;
import org.junit.Test;

/**
 * 
 * @author zollty
 * @since 2019年6月10日
 */
public class ArraysTest {
    
    @Test
    public void test_toString() {
        final int[] s1 = new int[] {1,2,3,4};
        final Integer[] s2 = new Integer[] {1,2,3,4};
        final String[] s3 = new String[] {"1","2","3","4"};
        String ret = "1,2,3,4";
        Assert.assertEquals(ret, 
                Arrays.from(s1).toString());
        Assert.assertEquals(ret, 
                Arrays.from(s2).toString());
        Assert.assertEquals(ret, 
                Arrays.from(s3).toString());
    }
    
    @Test
    public void test_toStringArray() {
        final String[] s3 = new String[] {"1","2","3","4"};
        
        final int[] s1 = new int[] {1,2,3,4};
        Assert.assertArrayEquals(s3, 
                Arrays.from(s1).toStringArray());
        
        final Integer[] s2 = new Integer[] {1,2,3,4};
        Assert.assertArrayEquals(s3, 
                Arrays.from(s2).toStringArray());
        
        Assert.assertArrayEquals(s3, 
                Arrays.from(s3).toStringArray());
        
        final int n = 1000000;
        TestTools.loopExecute(new LoopExecute(n) {
            @Override
            public void execute() throws Exception {
                Arrays.from(s2).toStringArray();
            }
        }, "toStringArray.2");
        
        TestTools.loopExecute(new LoopExecute(n) {
            @Override
            public void execute() throws Exception {
                Arrays.from(s1).toStringArray();
            }
        }, "toStringArray.1");
        
        TestTools.loopExecute(new LoopExecute(n) {
            @Override
            public void execute() throws Exception {
                Arrays.from(s3).toStringArray();
            }
        }, "toStringArray.3");
    }
    
    @Test
    public void test_toIntegerArray() {
        
        final int[] s1 = new int[] {1,2,3,4};
        final Integer[] s2 = new Integer[] {1,2,3,4};
        Assert.assertArrayEquals(s1, 
                Arrays.from(s1).tointArray());
        Assert.assertArrayEquals(s2, 
                Arrays.from(s2).toIntegerArray());
        
        Assert.assertArrayEquals(s2, 
                Arrays.from(s1).toIntegerArray());
        Assert.assertArrayEquals(s1, 
                Arrays.from(s2).tointArray());
        
        
        final long[] s3 = new long[] {1L,2L,3L,4L};
        final Long[] s4 = new Long[] {1L,2L,3L,4L};
        
        Assert.assertArrayEquals(s1, 
                Arrays.from(s3).tointArray());
        Assert.assertArrayEquals(s2, 
                Arrays.from(s3).toIntegerArray());
        
        Assert.assertArrayEquals(s1, 
                Arrays.from(s4).tointArray());
        Assert.assertArrayEquals(s2, 
                Arrays.from(s4).toIntegerArray());
        
        
        final int n = 1000000;
        TestTools.loopExecute(new LoopExecute(n) {
            @Override
            public void execute() throws Exception {
                Arrays.from(s1).tointArray();
            }
        }, "test_toInteger.2");
        
        TestTools.loopExecute(new LoopExecute(n) {
            @Override
            public void execute() throws Exception {
                Arrays.from(s2).toIntegerArray();
            }
        }, "test_toInteger.1");
        
        TestTools.loopExecute(new LoopExecute(n) {
            @Override
            public void execute() throws Exception {
                Arrays.from(s1).toIntegerArray();
            }
        }, "test_toInteger.3");
        
        TestTools.loopExecute(new LoopExecute(n) {
            @Override
            public void execute() throws Exception {
                Arrays.from(s2).tointArray();
            }
        }, "test_toInteger.4");
    }


    @Test
    public void test_toLongArray() {
        final int[] s1 = new int[] {1,2,3,4};
        final Integer[] s2 = new Integer[] {1,2,3,4};
        
        final long[] s3 = new long[] {1L,2L,3L,4L};
        final Long[] s4 = new Long[] {1L,2L,3L,4L};
        
        Assert.assertArrayEquals(s3, 
                Arrays.from(s1).tolongArray());
        Assert.assertArrayEquals(s4, 
                Arrays.from(s1).toLongArray());
        
        Assert.assertArrayEquals(s3, 
                Arrays.from(s2).tolongArray());
        Assert.assertArrayEquals(s4, 
                Arrays.from(s2).toLongArray());
        
        Assert.assertArrayEquals(s3, 
                Arrays.from(s4).tolongArray());
        Assert.assertArrayEquals(s4, 
                Arrays.from(s3).toLongArray());
    }


    @Test
    public void test_isNotEmpty() {
        int[] arr = new int[] {1};
        Assert.assertEquals(true, Arrays.from(arr).isNotEmpty());
        for(int i=0; i<10000000; i++) {
            Arrays.from(arr).isEmpty();
        }
    }

    @Test
    public void test_isEmpty() {
        int[] arr = new int[0];
        Assert.assertEquals(true, Arrays.from(arr).isEmpty());
        for(int i=0; i<10000000; i++) {
            Arrays.from(arr).isEmpty();
        }
    }

    @Test
    public void test_add() {
        final int[] s1 = new int[] {1,2,3,4};
        final int[] s2 = new int[] {5,6};
        Assert.assertArrayEquals(new int[] {1,2,3,4,5,6}, 
                Arrays.from(s1).param(s2).add().tointArray());
        final String[] s3 = new String[] {"1","2","3","4"};
        final String[] s4 = new String[] {"5","6"};
        Assert.assertArrayEquals(new String[] {"1","2","3","4","5","6"}, 
                Arrays.from(s3).param(s4).add().toStringArray());
        
        for(int i=0; i<1000000; i++) {
            Arrays.from(s1).param(s2).add().tointArray();
            Arrays.from(s3).param(s4).add().toStringArray();
        }
        
        Foo f1  = new Foo();
        Foo f2  = new Foo();
        Foo f3  = new Foo();
        Foo[] foos = new Foo[] {f1, f2};
        
        Assert.assertArrayEquals(new Foo[] {f1, f2, f3}, 
                Arrays.from(foos).param(f3).add().toObjectArray());
        
        Foo[] foos2 = (Foo[]) Arrays.from(foos).param(f3).add().toObjectArray();
        
        Assert.assertArrayEquals(foos, 
                Arrays.from(foos2).param(2).remove().toObjectArray());
    }

    @Test
    public void test_remove() {
        final int[] s1 = new int[] {1,2,3,4};
        Assert.assertArrayEquals(new int[] {1,2,4}, 
                Arrays.from(s1).param(2).remove().tointArray());
        
        final Integer[] s2 = new Integer[] {1,2,3,4};
        Assert.assertArrayEquals(new Integer[] {1,2,4}, 
                Arrays.from(s2).param(2).remove().toIntegerArray());
        
        final String[] s3 = new String[] {"1","2","3","4"};
        Assert.assertArrayEquals(new String[] {"1","2","4"}, 
                Arrays.from(s3).param(2).remove().toStringArray());
        
        for(int i=0; i<500000; i++) {
            Arrays.from(s1).param(2).remove().tointArray();
            Arrays.from(s2).param(2).remove().toIntegerArray();
            Arrays.from(s3).param(2).remove().toStringArray();
        }
    }

}
