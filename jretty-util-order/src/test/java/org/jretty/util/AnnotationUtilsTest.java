/**
 * 
 */
package org.jretty.util;

import static org.junit.Assert.assertEquals;
import java.lang.reflect.Method;

import org.junit.Test;

/**
 * 
 * @author zollty
 * @since 2018年9月30日
 */
public class AnnotationUtilsTest {

    @Order(2)
    private void test() {
    }

    @Test
    public void testFindAnnotation() {
        Method obj = ReflectionUtils.findMethod(AnnotationUtilsTest.class, "test");
        Order ann = AnnotationUtils.findAnnotation((Method) obj, Order.class);
        assertEquals(2, ann.value());
        ann = AnnotationUtils.findAnnotation((Method) obj, Order.class);
        assertEquals(2, ann.value());
    }

}
