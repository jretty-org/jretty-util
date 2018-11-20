package org.jretty.reflection;

import static org.junit.Assert.assertEquals;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.jretty.util.ClassUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * 
 * @author zollty
 * @since 2013-6-13
 */
public class ClassUtilsTest {

    @Test
    @SuppressWarnings("rawtypes")
    public void testGetAllSuperClass() {
        assertEquals(0, ClassUtils.getAllSuperClass(List.class).size());
        Set<Class> ifs = new LinkedHashSet<Class>();
        Class cl = LinkedList.class;
        ifs.add(cl.getSuperclass());
        ifs.add(cl.getSuperclass().getSuperclass());
        ifs.add(cl.getSuperclass().getSuperclass().getSuperclass());
        ifs.add(cl.getSuperclass().getSuperclass().getSuperclass().getSuperclass());
        assertEquals(ifs, ClassUtils.getAllSuperClass(LinkedList.class));
    }

    @Test
    @SuppressWarnings("rawtypes")
    public void testGetAllInterfaces() {
        Class[] ifs = new Class[] { java.util.List.class, java.util.Collection.class, java.lang.Iterable.class,
                java.util.Deque.class, java.util.Queue.class, java.lang.Cloneable.class, java.io.Serializable.class };

        assertEquals(Arrays.asList(ifs), new ArrayList<Class>(ClassUtils.getAllInterfaces(LinkedList.class)));
    }

    @Test
    @SuppressWarnings("rawtypes")
    public void testFindAllAssignableClass() {

        Class cl = AbstractList.class;
        Set<Class> ifs = new LinkedHashSet<Class>();
        ifs.add(cl);
        ifs.add(cl.getSuperclass());
        ifs.addAll(Arrays.asList(cl.getInterfaces()));
        ifs.addAll(Arrays.asList(cl.getSuperclass().getInterfaces()));
        ifs.addAll(Arrays.asList(List.class.getInterfaces()));
        ifs.addAll(Arrays.asList(Collection.class.getInterfaces()));
        Class[] ifs2 = new Class[] { java.util.AbstractList.class, java.util.List.class, java.util.Collection.class, 
                java.lang.Iterable.class, java.util.AbstractCollection.class};
        
        assertEquals(ifs, ClassUtils.findAllAssignableClass(AbstractList.class));
        assertEquals(Arrays.asList(ifs2), new ArrayList<Class>(ClassUtils.findAllAssignableClass(AbstractList.class)));
    }
    
    @Test
    public void testisAssignableFromWithNum() {
        Assert.assertTrue(ClassUtils.isAssignableWithNum(Double.class, double.class));
        Assert.assertTrue(ClassUtils.isAssignableWithNum(double.class, Double.class));
        
        Assert.assertTrue(ClassUtils.isAssignableWithNum(Double.class, Double.class));
        Assert.assertTrue(ClassUtils.isAssignableWithNum(double.class, double.class));
        
        Assert.assertTrue(ClassUtils.isAssignableWithNum(Float.class, float.class));
        Assert.assertTrue(ClassUtils.isAssignableWithNum(float.class, Float.class));
        
        Assert.assertTrue(!ClassUtils.isAssignableWithNum(Float.class, double.class));
        Assert.assertTrue(!ClassUtils.isAssignableWithNum(float.class, Double.class));
        
        Assert.assertTrue(ClassUtils.isAssignableWithNum(Double.class, Float.class));
        Assert.assertTrue(ClassUtils.isAssignableWithNum(double.class, float.class));
        
        Assert.assertTrue(!ClassUtils.isAssignableWithNum(Short.class, ClassUtilsTest.class));
        Assert.assertTrue(!ClassUtils.isAssignableWithNum(short.class, ClassUtilsTest.class));
        Assert.assertTrue(!ClassUtils.isAssignableWithNum(ClassUtilsTest.class, Short.class));
        Assert.assertTrue(!ClassUtils.isAssignableWithNum(ClassUtilsTest.class, short.class));
        Assert.assertTrue(ClassUtils.isAssignableWithNum(ClassUtilsTest.class, ClassUtilsTest.class));
    }

}