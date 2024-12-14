/*
 * Copyright (C) 2013-2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 */
package org.jretty.util;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import org.jretty.util.lambda.IdeaProxyLambdaMeta;
import org.jretty.util.lambda.LambdaMeta;
import org.jretty.util.lambda.PropertyNamer;
import org.jretty.util.lambda.ReflectLambdaMeta;
import org.jretty.util.lambda.SetAccessibleAction;
import org.jretty.util.lambda.ShadowLambdaMeta;

/**
 * Lambda 解析工具类
 * @author zollty
 * @since 2023年7月23日
 */
public class LambdaUtils {
    
    /**
     * 注意，每次转换Fn都会调用反射方法，有一定性能开销，建议将转换后的结果缓存，不要频繁调用。
     */
    @SafeVarargs
    public static <T> List<String> toString(Fn<T, ?> ...fns) {
        List<String> properties = new ArrayList<>();
        for(Fn<T, ?> fn: fns) {
            LambdaMeta lm = extract(fn);
            properties.add(PropertyNamer.methodToProperty(lm.getImplMethodName()));
        }
        return properties;
    }

    /**
     * 解析lambda对象信息（例如User::getName），注意每次调用User::getName都是一个全新的对象实例。建议使用者自己将结果缓存下来。
     * @param func 需要解析的 lambda 对象
     * @param <T> 类型，被调用的 Function 对象的目标类型
     * @return 返回解析后的结果
     */
    public static <T> LambdaMeta extract(Fn<T, ?> func) {
        // 1. IDEA 调试模式下 lambda 表达式是一个代理
        if (func instanceof Proxy) {
            return new IdeaProxyLambdaMeta((Proxy) func);
        }
        // 2. 反射读取 （使用 [M extends Function<T, R>, Serializable]类型参数，JVM将自动将xx::getXX转换成带writeReplace方法的类）
        try {
            Method method = func.getClass().getDeclaredMethod("writeReplace");
            LambdaMeta o = new ReflectLambdaMeta((SerializedLambda) SetAccessibleAction.setAccessible(method).invoke(func),
                    func.getClass().getClassLoader());
            return o;
        } catch (Throwable e) {
            // 3. 反射失败使用序列化的方式读取
            return new ShadowLambdaMeta(org.jretty.util.lambda.SerializedLambda.extract(func));
        }
    }
    
    public static LinkedHashSet<ClassLoader> initClassLoaders(ClassLoader cls) {
        return SetAccessibleAction.initClassLoaders(cls);
    }
    
    public static LinkedHashSet<ClassLoader> initClassLoaders(ClassLoader cls, ClassLoader cls2) {
        return SetAccessibleAction.initClassLoaders(cls, cls2);
    }
}