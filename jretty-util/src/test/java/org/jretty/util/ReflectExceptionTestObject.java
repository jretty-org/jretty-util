package org.jretty.util;

import java.lang.reflect.Method;

public class ReflectExceptionTestObject {
    
    private String name;
    
    public void foo() {
        privateMethod();
    }
    
    private int privateMethod() {
        int i=0;
        int b = 10/i;
        return b;
    }
    
    static int  b;
    static {
        int i=0;
        b = 10/i;
    }
    
    static public int statiMethod() {
        return b;
    }

    public String getName() {
        
        Method m = null;
        try {
            m = ReflectExceptionTestObject.class.getDeclaredMethod("privateMethod", new Class<?>[0]);
            
            m.setAccessible(true);
            
            m.invoke(this, new Object[0]);
            
        } catch (Exception e) {
            throw new NestedRuntimeException(e);
        }
        
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
