package org.jretty.util.lambda;

import java.util.LinkedHashSet;

import org.jretty.util.ClassUtils;
import org.jretty.util.NestedRuntimeException;

class ClassUtil {
    
    private static ClassLoader systemClassLoader;

    static {
        try {
            systemClassLoader = ClassLoader.getSystemClassLoader();
        } catch (SecurityException ignored) {
            // AccessControlException on Google App Engine
            systemClassLoader = null;
        }
    }
    
    private static LinkedHashSet<ClassLoader> classLoaders = new LinkedHashSet<>();
    
    public static LinkedHashSet<ClassLoader> initClassLoaders(ClassLoader cls) {
        classLoaders.clear();
        classLoaders.add(systemClassLoader);
        classLoaders.add(ClassUtils.class.getClassLoader());
        classLoaders.add(Thread.currentThread().getContextClassLoader());
        classLoaders.add(cls);
        return classLoaders;
    }
    
    public static LinkedHashSet<ClassLoader> initClassLoaders(ClassLoader cls, ClassLoader cls2) {
        initClassLoaders(cls);
        classLoaders.add(cls2);
        return classLoaders;
    }

    /**
     * <p>
     * 请仅在确定类存在的情况下调用该方法
     * </p>
     *
     * @param name 类名称
     * @return 返回转换后的 Class
     */
    public static Class<?> toClassConfident(String name) {
        return toClassConfident(name, null);
    }

    public static Class<?> toClassConfident(String name, ClassLoader classLoader) {
        try {
            if(classLoaders.isEmpty()) {
                initClassLoaders(classLoader);
            } else {
                classLoaders.add(classLoader);
            }
            return loadClass(name, classLoaders);
        } catch (ClassNotFoundException e) {
            throw new NestedRuntimeException(e, "找不到指定的class！请仅在明确确定会有 class 的时候，调用该方法");
        }
    }

    private static Class<?> loadClass(String className, LinkedHashSet<ClassLoader> classLoaders) throws ClassNotFoundException {
        ClassLoader[] clss = new ClassLoader[classLoaders.size()];
        classLoaders.toArray(clss);
        for (int i = clss.length - 1; i >= 0; i--) {
            if (clss[i] != null) {
                try {
                    return Class.forName(className, true, clss[i]);
                } catch (ClassNotFoundException e) {
                    // ignore
                }
            }
        }
        throw new ClassNotFoundException("Cannot find class: " + className);
    }


}
