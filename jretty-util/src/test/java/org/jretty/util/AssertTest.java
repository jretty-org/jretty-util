package org.jretty.util;

import org.junit.Test;


public class AssertTest {
    
    
    @Test
    public void assertExceptionTest() {
        int line = 25; // 对应 doService第三行的行号
        try {
            doService(null, "assert it's true but false....");
        } catch(Exception e) {
            StackTraceElement[] st = e.getStackTrace();
            // e.printStackTrace();
            org.junit.Assert.assertEquals(
                    "org.jretty.util.AssertTest.doService(AssertTest.java:"+line+")",
                    st[0].toString());
        }
    }
    
    public static void doService(String expression, String message) {
        System.out.println("pass..");
        Assert.hasLength(expression); //line number
        System.out.println("OK");
    }
    
    
    
    
    /**
     * Assert a boolean expression, throwing <code>IllegalArgumentException</code>
     * if the test result is <code>false</code>.
     * <pre class="code">Assert.isTrue(i &gt; 0, "The value must be greater than zero");</pre>
     * @param expression a boolean expression
     * @param message the exception message to use if the assertion fails
     * @throws IllegalArgumentException if expression is <code>false</code>
     */
    public static void isTrue(boolean expression, String message) {
        if (!expression) {
            IllegalArgumentException e = new IllegalArgumentException(message);
            
            StackTraceElement[] st = e.getStackTrace();
            st = ArrayUtils.remove(st, 0);
            e.setStackTrace(st);
            //e.printStackTrace();
            throw e;
        }
    }
    
    public static void hasLength(String text) {
//        try {
        hasText(text,
                "this exception is just for unit test. [Assertion failed] - this String argument must have length; it must not be null or empty");
//        } catch(IllegalArgumentException e) {
//            throw changeIAE(e);
//        }
    }
    
    public static RuntimeException changeIAE(RuntimeException e){
        StackTraceElement[] st = e.getStackTrace();
        st = ArrayUtils.remove(st, 0);
        e.setStackTrace(st);
        return e;
    }

    /**
     * Assert that the given String has valid text content; that is, it must not
     * be <code>null</code> and must contain at least one non-whitespace character.
     * <pre class="code">Assert.hasText(name, "'name' must not be empty");</pre>
     * @param text the String to check
     * @param message the exception message to use if the assertion fails
     * @see StringUtils#hasText
     */
    public static void hasText(String text, String message) {
//        try {
//        notNull(text);
//        } catch(IllegalArgumentException e) {
//            throw changeIAE(e);
//        }
//        if (StringUtils.isBlank(text)) {
//            throw changeIAE(new IllegalArgumentException(message));
//        }
        
        notNull(text);
        if (StringUtils.isBlank(text)) {
            throw new IllegalArgumentException(message);
        }
    }
    
    
    /**
     * Assert that an object is not <code>null</code> .
     * <pre class="code">Assert.notNull(clazz);</pre>
     * @param object the object to check
     * @throws IllegalArgumentException if the object is <code>null</code>
     */
    public static void notNull(Object object) {
//        try {
//        notNull(object, "[Assertion failed] - this argument is required; it must not be null");
//        } catch(IllegalArgumentException e) {
//            throw changeIAE(e);
//        }
        notNull(object, "[Assertion failed] - this argument is required; it must not be null");
    }

    /**
     * Assert that an object is not <code>null</code> .
     * <pre class="code">Assert.notNull(clazz, "The class must not be null");</pre>
     * @param object the object to check
     * @param message the exception message to use if the assertion fails
     * @throws IllegalArgumentException if the object is <code>null</code>
     */
    public static void notNull(Object object, String message) {
        if (object == null) {
//            throw changeIAE(new IllegalArgumentException(message));
            throw new IllegalArgumentException(message);
        }
    }

}
