package org.jretty.util;

import org.junit.Test;

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

    
}