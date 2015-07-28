package org.zollty.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

public class CollectionUtilsTest {
    
    @Test
    public void testIsNullOrEmpty() {
        Collection<String> coll = null;
        
        Assert.assertTrue( CollectionUtils.isNullOrEmpty(coll) );
        
        coll = new ArrayList<String>();
        
        Assert.assertTrue( CollectionUtils.isNullOrEmpty(coll) );
        
        coll.add("");
        
        Assert.assertFalse( CollectionUtils.isNullOrEmpty(coll) );
        
        
        Enumeration<COMMON_TYPE> enums = new TestEnumerator(list);
        
        Assert.assertFalse( CollectionUtils.isNullOrEmpty(enums) );
        Collections.list(enums); // 消耗完enums
        Assert.assertTrue( CollectionUtils.isNullOrEmpty(enums) );
    }
    
    
    @Test
    public void testIsNotEmpty() {
        Collection<String> coll = null;
        
        Assert.assertFalse( CollectionUtils.isNotEmpty(coll) );
        
        coll = new ArrayList<String>();
        
        Assert.assertFalse( CollectionUtils.isNotEmpty(coll) );
        
        coll.add("");
        
        Assert.assertTrue( CollectionUtils.isNotEmpty(coll) );
    }
    
    @Test
    public void testToString(){
        Set<String> set = new LinkedHashSet<String>();
        set.add("aa");
        set.add("bb");
        set.add("cc");
        set.add("dd");
        set.add("ee");
        
        Assert.assertEquals("aa, bb, cc, dd, ee", CollectionUtils.toString(set, ", "));
    }
    
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Test
    public void toArrayTest() {
        String[] res = new String[]{"aa", "bbb", "ccc"};
        Collection<String> coll1 = Arrays.asList(res);
        
        // use origin collection toArray
        String[] ary = coll1.toArray(new String[coll1.size()]);
        Assert.assertArrayEquals(res, ary);
        
        // use CollectionUtils.toArray
        ary = (String[]) CollectionUtils.toArray(coll1, String.class);
        Assert.assertArrayEquals(res, ary);
        
        
        Collection<String> coll2 = new LinkedHashSet<String>(Arrays.asList(res));
        
        // use origin collection toArray
        ary = coll2.toArray(new String[coll2.size()]);
        Assert.assertArrayEquals(res, ary);
        
        // use CollectionUtils.toArray
        ary = (String[]) CollectionUtils.toArray(coll2, String.class);
        Assert.assertArrayEquals(res, ary);
        
        
        
        Collection coll3 = new LinkedHashSet();
        coll3.add(new Integer(5));
        coll3.add(new Integer(8));
        
        // use origin collection toArray
        Integer[] ary3 = (Integer[]) coll3.toArray(new Integer[coll3.size()]);
        System.out.println(Arrays.toString(ary3));
        
        // use CollectionUtils.toArray
        Integer[] ary4 = (Integer[]) CollectionUtils.toArray(coll3, Integer.class);
        System.out.println(Arrays.toString(ary4));
        
        
        Enumeration<COMMON_TYPE> enumeration = new TestEnumerator(list);
        COMMON_TYPE[] aa = CollectionUtils.toArray(enumeration, COMMON_TYPE.class);
        
        Assert.assertArrayEquals(list.toArray(), aa);
        
        aa = CollectionUtils.toArray(enumeration, COMMON_TYPE.class);
        Assert.assertArrayEquals(new COMMON_TYPE[0], aa);
        
        
        Enumeration en2 = new TestEnumerator(list);
        Object bbo = CollectionUtils.toArray(en2, COMMON_TYPE.class);
        COMMON_TYPE[] bb = (COMMON_TYPE[]) bbo;
        Assert.assertArrayEquals(list.toArray(), bb);
        
        bbo = CollectionUtils.toArray(en2, COMMON_TYPE.class);
        bb = (COMMON_TYPE[]) bbo;
        Assert.assertArrayEquals(new COMMON_TYPE[0], bb);
    }
    
    
    private static List<COMMON_TYPE> list = new ArrayList<COMMON_TYPE>();
    static {
        list.add(COMMON_TYPE.BOOLEAN);
        list.add(COMMON_TYPE.INT);
    }
    
    private class TestEnumerator implements Enumeration<COMMON_TYPE> {
        Iterator<COMMON_TYPE> it = null;
        
        // 构造器
        public TestEnumerator(List<COMMON_TYPE> list) {
            it = list.iterator();
        }

        public boolean hasMoreElements() {
            return it.hasNext();
        }

        public COMMON_TYPE nextElement() {
            return it.next();
        }
    }
    
    
    private enum COMMON_TYPE {

        INT("java.lang.Integer"), LONG("java.lang.Long"), DOUBLE("java.lang.Double"), FLOAT("java.lang.Float"), SHORT("java.lang.Short"), BYTE("java.lang.Byte"), CHAR(
                "java.lang.Character"), BOOLEAN("java.lang.Boolean"), STRING("java.lang.String");

        @SuppressWarnings("unused")
        private final String value;

        private COMMON_TYPE(String value) {
            this.value = value;
        }
    }

}
