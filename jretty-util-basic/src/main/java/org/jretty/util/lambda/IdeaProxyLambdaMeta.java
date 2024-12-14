package org.jretty.util.lambda;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Executable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

import org.jretty.util.NestedRuntimeException;
import org.jretty.util.ReflectionUtils;

/**
 * 在 IDEA 的 Evaluate 中执行的 Lambda 表达式元数据需要使用该类处理元数据
 */
public class IdeaProxyLambdaMeta implements LambdaMeta {
    private final Class<?> clazz;
    private final String name;

    public IdeaProxyLambdaMeta(Proxy func) {
        InvocationHandler handler = Proxy.getInvocationHandler(func);
        try {
            ReflectionUtils.setField(null, func, handler);
            MethodHandle dmh = (MethodHandle) SetAccessibleAction.setAccessible(handler.getClass().getDeclaredField("val$target")).get(handler);
            Executable executable = MethodHandles.reflectAs(Executable.class, dmh);
            clazz = executable.getDeclaringClass();
            name = executable.getName();
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new NestedRuntimeException(e);
        }
    }

    @Override
    public String getImplMethodName() {
        return name;
    }

    @Override
    public Class<?> getInstantiatedClass() {
        return clazz;
    }

    @Override
    public String toString() {
        return clazz.getSimpleName() + "::" + name;
    }

}