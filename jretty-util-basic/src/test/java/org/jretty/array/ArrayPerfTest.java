package org.jretty.array;

import org.jretty.tesper.LoopExecute;
import org.jretty.tesper.TestTools;
import org.jretty.util.ArrayUtils;
import org.jretty.util.Arrays;
import org.junit.Test;

/**
 * 
 * @author zollty
 * @since 2019年6月10日
 */
public class ArrayPerfTest {
    
    @Test
    public void test_xxx() {
        
    }
    
//    @Test
    public void test_toString() {
        int len = 2000;
        final int[] s1 = new int[len];
        for (int i = 0; i < len; i++) {
            s1[i] = i + 1000;
        }
        final Integer[] s2 = new Integer[len];
        for (Integer i = 0; i < len; i++) {
            s2[i] = new Integer(i+1000);
        }
        final String[] s3 = new String[len];
        for (Integer i = 0; i < len; i++) {
            s3[i] = new Integer(i+1000).toString();
        }
        
        final int n = 5000;
        TestTools.loopExecute(new LoopExecute(n) {
            @Override
            public void execute() throws Exception {
                Arrays.from(s3).toString();
            }
        }, "toString.3");
        
        TestTools.loopExecute(new LoopExecute(n) {
            @Override
            public void execute() throws Exception {
                Arrays.from(s2).toString();
            }
        }, "toString.2");
        
        TestTools.loopExecute(new LoopExecute(n) {
            @Override
            public void execute() throws Exception {
                Arrays.from(s1).toString();
            }
        }, "toString.1");
        
        TestTools.loopExecute(new LoopExecute(n) {
            @Override
            public void execute() throws Exception {
                ArrayUtils.toString(s3);
            }
        }, "ArrayUtils.toString.3");
        
        TestTools.loopExecute(new LoopExecute(n) {
            @Override
            public void execute() throws Exception {
                ArrayUtils.toString(s2);
            }
        }, "ArrayUtils.toString.2");
        
        TestTools.loopExecute(new LoopExecute(n) {
            @Override
            public void execute() throws Exception {
                java.util.Arrays.toString(s1);
            }
        }, "java.util.Arrays.toString.1");
        
        TestTools.loopExecute(new LoopExecute(n) {
            @Override
            public void execute() throws Exception {
                java.util.Arrays.toString(s2);
            }
        }, "java.util.Arrays.toString.2");
        
        TestTools.loopExecute(new LoopExecute(n) {
            @Override
            public void execute() throws Exception {
                java.util.Arrays.toString(s3);
            }
        }, "java.util.Arrays.toString.3");
    }
    
//    @Test
    public void test_toStringArray() throws Exception {
        int len = 2000;
        final int[] s1 = new int[len];
        for (int i = 0; i < len; i++) {
            s1[i] = i + 1000;
        }
        final Integer[] s2 = new Integer[len];
        for (Integer i = 0; i < len; i++) {
            s2[i] = new Integer(i+1000);
        }
        final String[] s3 = new String[len];
        for (Integer i = 0; i < len; i++) {
            s3[i] = new Integer(i+1000).toString();
        }
        
        final int n = 20000;
        TestTools.loopExecute(new LoopExecute(n) {
            @Override
            public void execute() throws Exception {
                Arrays.from(s3).toStringArray();
            }
        }, "toStringArray.3");
        
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
    }
    
//    @Test
    public void test_toIntegerArray() {
        
        int len = 2000;
        final int[] s1 = new int[len];
        for (int i = 0; i < len; i++) {
            s1[i] = i + 1000;
        }
        final Integer[] s2 = new Integer[len];
        for (Integer i = 0; i < len; i++) {
            s2[i] = new Integer(i + 1000);
        }
        
        final int n = 200000;
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


//    @Test
    public void test_toLongArray() {
        int len = 2000;
        final int[] s1 = new int[len];
        for (int i = 0; i < len; i++) {
            s1[i] = i + 1000;
        }
        final Integer[] s2 = new Integer[len];
        for (Integer i = 0; i < len; i++) {
            s2[i] = new Integer(i + 1000);
        }
        
        final long[] s3 = new long[len];
        for (int i = 0; i < len; i++) {
            s3[i] = i + 1000L;
        }
        final Long[] s4 = new Long[len];
        for (Integer i = 0; i < len; i++) {
            s4[i] = new Long(i + 1000);
        }
        
        final int n = 20000;
        
        TestTools.loopExecute(new LoopExecute(n) {
            @Override
            public void execute() throws Exception {
                Arrays.from(s2).tolongArray();
            }
        }, "toLong.3");
        
        TestTools.loopExecute(new LoopExecute(n) {
            @Override
            public void execute() throws Exception {
                Arrays.from(s1).tolongArray();
            }
        }, "toLong.2");
        
        TestTools.loopExecute(new LoopExecute(n) {
            @Override
            public void execute() throws Exception {
                Arrays.from(s2).toLongArray();
            }
        }, "toLong.4");
        
        TestTools.loopExecute(new LoopExecute(n) {
            @Override
            public void execute() throws Exception {
                Arrays.from(s4).tolongArray();
            }
        }, "toLong.5");
        
        TestTools.loopExecute(new LoopExecute(n) {
            @Override
            public void execute() throws Exception {
                Arrays.from(s1).toLongArray();
            }
        }, "toLong.1");
        
        TestTools.loopExecute(new LoopExecute(n) {
            @Override
            public void execute() throws Exception {
                Arrays.from(s3).toLongArray();
            }
        }, "toLong.6");
        
        
        
        // 
        TestTools.loopExecute(new LoopExecute(n) {
            @Override
            public void execute() throws Exception {
                ArrayUtils.changeType(s2);
            }
        }, "ArrayUtils.toLong.4");
        
        TestTools.loopExecute(new LoopExecute(n) {
            @Override
            public void execute() throws Exception {
                ArrayUtils.changeType(s4);
            }
        }, "ArrayUtils.toLong.5");
        
        TestTools.loopExecute(new LoopExecute(n) {
            @Override
            public void execute() throws Exception {
                ArrayUtils.changeType(s1);
            }
        }, "ArrayUtils.toLong.1");
        
        TestTools.loopExecute(new LoopExecute(n) {
            @Override
            public void execute() throws Exception {
                ArrayUtils.changeType(s3);
            }
        }, "ArrayUtils.toLong.6");
    }


//    @Test
    public void test_add() {
        int len = 2000;
        final int[] s1 = new int[len];
        for (int i = 0; i < len; i++) {
            s1[i] = i + 1000;
        }
        final Integer[] s2 = new Integer[len];
        for (Integer i = 0; i < len; i++) {
            s2[i] = new Integer(i+1000);
        }
        final String[] s3 = new String[len];
        for (Integer i = 0; i < len; i++) {
            s3[i] = new Integer(i+1000).toString();
        }
        
        final int[] s4 = s1.clone();
        final Integer[] s5 = s2.clone();
        final String[] s6 = s3.clone();
        
        final int n = 100000;
        TestTools.loopExecute(new LoopExecute(n) {
            @Override
            public void execute() throws Exception {
                Arrays.from(s2).param(s5).add().toIntegerArray();
            }
        }, "add.1");
        
        TestTools.loopExecute(new LoopExecute(n) {
            @Override
            public void execute() throws Exception {
                Arrays.from(s1).param(s4).add().tointArray();
            }
        }, "add.2");
        
        TestTools.loopExecute(new LoopExecute(n) {
            @Override
            public void execute() throws Exception {
                Arrays.from(s3).param(s6).add().toStringArray();
            }
        }, "add.3");
        
        
        // 
        TestTools.loopExecute(new LoopExecute(n) {
            @Override
            public void execute() throws Exception {
                ArrayUtils.addAll(s2, s5);
            }
        }, "ArrayUtils.add.1");
        
        TestTools.loopExecute(new LoopExecute(n) {
            @Override
            public void execute() throws Exception {
                ArrayUtils.addAll(s1, s4);
            }
        }, "ArrayUtils.add.2");
        
        TestTools.loopExecute(new LoopExecute(n) {
            @Override
            public void execute() throws Exception {
                ArrayUtils.addAll(s3, s6);
            }
        }, "ArrayUtils.add.3");
    }

//    @Test
    public void test_remove() {
        int len = 2000;
        final int[] s1 = new int[len];
        for (int i = 0; i < len; i++) {
            s1[i] = i + 1000;
        }
        final Integer[] s2 = new Integer[len];
        for (Integer i = 0; i < len; i++) {
            s2[i] = new Integer(i+1000);
        }
        final String[] s3 = new String[len];
        for (Integer i = 0; i < len; i++) {
            s3[i] = new Integer(i+1000).toString();
        }
        
        final int n = 200000;
        TestTools.loopExecute(new LoopExecute(n) {
            @Override
            public void execute() throws Exception {
                Arrays.from(s1).param(500).remove().tointArray();
            }
        }, "remove.1");
        
        TestTools.loopExecute(new LoopExecute(n) {
            @Override
            public void execute() throws Exception {
                Arrays.from(s2).param(500).remove().toIntegerArray();
            }
        }, "remove.2");
        
        TestTools.loopExecute(new LoopExecute(n) {
            @Override
            public void execute() throws Exception {
                Arrays.from(s3).param(500).remove().toStringArray();
            }
        }, "remove.3");
        
        
        // 
        TestTools.loopExecute(new LoopExecute(n) {
            @Override
            public void execute() throws Exception {
                ArrayUtils.remove(s1, 500);
            }
        }, "ArrayUtils.remove.1");
        
        TestTools.loopExecute(new LoopExecute(n) {
            @Override
            public void execute() throws Exception {
                ArrayUtils.remove(s2, 500);
            }
        }, "ArrayUtils.remove.2");
        
        TestTools.loopExecute(new LoopExecute(n) {
            @Override
            public void execute() throws Exception {
                ArrayUtils.remove(s3, 500);
            }
        }, "ArrayUtils.remove.3");
    }


}
