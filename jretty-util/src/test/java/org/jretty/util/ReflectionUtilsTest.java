package org.jretty.util;

import java.lang.reflect.Field;
import java.util.Map;

import org.jretty.test.Bar;
import org.junit.Test;

@SuppressWarnings("unused")
public class ReflectionUtilsTest {

    static class Foo extends Exception {
        private static final long serialVersionUID = 2698756158937323201L;

        private final void privateMethod() {
        }

        void test() {
            privateMethod();
        }

        static void staticMethod() {
        }

        @Override
        public String toString() {
            return null;
        }

    }

    @Test
    public void getAllDeclaredMethodsTest() {
        // List<Method> mths =
        ReflectionUtils.getUniqueDeclaredMethods(Foo.class);

        // System.out.println( CollectionUtils.toString(mths, "\n") );
    }

    @Test
    public void getAllFieldsTest() {
        // Map<String, Field> map =
        ReflectionUtils.getAllNonStaticFields(Foo.class);
        
        // System.out.println( CollectionUtils.toString(map.entrySet(), "\n") );
    }
    
    
    @Test
    public void getFieldTest() {
        Field field = ReflectionUtils.findField(Bar.class, "hadValue");
        ReflectionUtils.makeAccessible(field);
        Object value = ReflectionUtils.getField(field, new Bar());
        // System.out.println(value);
    }
    
    @Test
    public void toMapTest() {
        Map<String, Object> map = ReflectionUtils.toMap(new Bar());
        
        System.out.println( CollectionUtils.toString(map.entrySet(), "\n") );
    }
    
}