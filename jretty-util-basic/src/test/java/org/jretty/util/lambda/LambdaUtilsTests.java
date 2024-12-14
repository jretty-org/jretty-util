package org.jretty.util.lambda;

import java.util.function.Function;

import org.jretty.util.LambdaUtils;
import org.junit.Ignore;
import org.junit.Test;

/**
 * 所有工具类都需要经过严格的单元测试
 */
@Ignore
public class LambdaUtilsTests {
    
    <T> void extract(Function<T, ?> func) {
        Class<?> clazz = func.getClass();
        String name = clazz.getName();
        System.out.println(name);
        System.out.println(func.toString());
    }
    
    @Test
    public void lambdaMeta() {
        extract(User::getAge);
        extract(User::getAge);
        LambdaMeta lm = LambdaUtils.extract(User::getAge);
        String implMethodName = lm.getImplMethodName();
        System.out.println(implMethodName);
        String fieldName = PropertyNamer.methodToProperty(implMethodName);
        System.out.println(fieldName);
        
        System.out.println(lm.getInstantiatedClass().getName());
        lm = LambdaUtils.extract(User::getUserName);
        System.out.println(lm.getImplMethodName());
        implMethodName = lm.getImplMethodName();
        System.out.println(implMethodName);
        fieldName = PropertyNamer.methodToProperty(implMethodName);
        System.out.println(fieldName);
        
        System.out.println(lm.getInstantiatedClass().getName());
    }

}
