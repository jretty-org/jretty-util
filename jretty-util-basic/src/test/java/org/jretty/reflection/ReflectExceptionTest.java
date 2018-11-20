package org.jretty.reflection;

import org.jretty.log.LogFactory;
import org.jretty.log.Logger;
import org.jretty.util.ReflectionMethodUtils;
import org.jretty.util.ReflectionUtils;

public class ReflectExceptionTest {

    static Logger LOG = LogFactory.getLogger();

    public static void main(String[] args) {
        // test1("privateMethod");
        // System.out.println("=====================");
        // test1("getName");
        test2("statiMethod");
    }

    public static void test1(String methodName) {
        try {
            ReflectionMethodUtils.invokeMethod(methodName, ReflectionUtils.newInstance(ReflectExceptionTestObject.class));
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(e, "ERRRRR!!!");
        }
    }

    // InvocationTargetException, ExceptionInInitializerError, RuntimeErrorException, SAXException, MBeanException
    public static void test2(String methodName) {
        try {
            ReflectionMethodUtils.invokeStaticMethod(methodName, ReflectExceptionTestObject.class);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(e, "ERRRRR!!!");
        }
    }

}