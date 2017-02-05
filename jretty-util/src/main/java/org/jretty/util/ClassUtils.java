/* 
 * Copyright (C) 2013-2015 the original author or authors.
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

import java.beans.Introspector;
import java.lang.reflect.Array;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Miscellaneous class utility methods. 
 * 
 * @author Juergen Hoeller
 * @author Keith Donald
 * @author Rob Harrop
 * @author Sam Brannen
 * @auther Zollty Tsou
 * 
 * @since 2013-6-13
 */
@SuppressWarnings("unchecked")
public class ClassUtils {
    
    /** Suffix for array class names: "[]" */
    public static final String ARRAY_SUFFIX = "[]";

    /** Prefix for internal array class names: "[" */
    private static final String INTERNAL_ARRAY_PREFIX = "[";

    /** Prefix for internal non-primitive array class names: "[L" */
    private static final String NON_PRIMITIVE_ARRAY_PREFIX = "[L";
    
    /** The package separator character '.' */
    private static final char PACKAGE_SEPARATOR = '.';

    /** The inner class separator character '$' */
    private static final char INNER_CLASS_SEPARATOR = '$';

    /** The ".class" file suffix */
    public static final String CLASS_FILE_SUFFIX = ".class";

    /**
     * Map with primitive wrapper type as key and corresponding primitive
     * type as value, for example: Integer.class -> int.class.
     */
    private static final Map<Class<?>, Class<?>> primitiveWrapperTypeMap = new HashMap<Class<?>, Class<?>>(8);

    /**
     * Map with primitive type as key and corresponding wrapper
     * type as value, for example: int.class -> Integer.class.
     */
    private static final Map<Class<?>, Class<?>> primitiveTypeToWrapperMap = new HashMap<Class<?>, Class<?>>(8);

    /**
     * Map with primitive type name as key and corresponding primitive
     * type as value, for example: "int" -> "int.class".
     */
    private static final Map<String, Class<?>> primitiveTypeNameMap = new HashMap<String, Class<?>>(32);

    /**
     * Map with common "java.lang" class name as key and corresponding Class as value.
     * Primarily for efficient deserialization of remote invocations.
     */
    private static final Map<String, Class<?>> commonClassCache = new HashMap<String, Class<?>>(32);
    
    static {
        primitiveWrapperTypeMap.put(Boolean.class, boolean.class);
        primitiveWrapperTypeMap.put(Byte.class, byte.class);
        primitiveWrapperTypeMap.put(Character.class, char.class);
        primitiveWrapperTypeMap.put(Double.class, double.class);
        primitiveWrapperTypeMap.put(Float.class, float.class);
        primitiveWrapperTypeMap.put(Integer.class, int.class);
        primitiveWrapperTypeMap.put(Long.class, long.class);
        primitiveWrapperTypeMap.put(Short.class, short.class);

        for (Map.Entry<Class<?>, Class<?>> entry : primitiveWrapperTypeMap.entrySet()) {
            primitiveTypeToWrapperMap.put(entry.getValue(), entry.getKey());
            registerCommonClasses(entry.getKey());
        }

        Set<Class<?>> primitiveTypes = new HashSet<Class<?>>(32);
        primitiveTypes.addAll(primitiveWrapperTypeMap.values());
        primitiveTypes.addAll(Arrays.asList(
                boolean[].class, byte[].class, char[].class, double[].class,
                float[].class, int[].class, long[].class, short[].class));
        primitiveTypes.add(void.class);
        for (Class<?> primitiveType : primitiveTypes) {
            primitiveTypeNameMap.put(primitiveType.getName(), primitiveType);
        }

        registerCommonClasses(Boolean[].class, Byte[].class, Character[].class, Double[].class,
                Float[].class, Integer[].class, Long[].class, Short[].class);
        registerCommonClasses(Number.class, Number[].class, String.class, String[].class,
                Object.class, Object[].class, Class.class, Class[].class);
        registerCommonClasses(Throwable.class, Exception.class, RuntimeException.class,
                Error.class, StackTraceElement.class, StackTraceElement[].class);
    }

    /**
     * Register the given common classes with the ClassUtils cache.
     */
    private static void registerCommonClasses(Class<?>... commonClasses) {
        for (Class<?> clazz : commonClasses) {
            commonClassCache.put(clazz.getName(), clazz);
        }
    }
    
    /**
     * Return the default ClassLoader to use: typically the thread context
     * ClassLoader, if available; the ClassLoader that loaded the ClassUtils
     * class will be used as fallback.
     * <p>Call this method if you intend to use the thread context ClassLoader
     * in a scenario where you absolutely need a non-null ClassLoader reference:
     * for example, for class path resource loading (but not necessarily for
     * <code>Class.forName</code>, which accepts a <code>null</code> ClassLoader
     * reference as well).
     * @return the default ClassLoader (never <code>null</code>)
     * @see java.lang.Thread#getContextClassLoader()
     */
    public static ClassLoader getDefaultClassLoader() {
        ClassLoader cl = null;
        try {
            cl = Thread.currentThread().getContextClassLoader();
        }
        catch (Throwable ex) {
            // Cannot access thread context ClassLoader - falling back to system class loader...
        }
        if (cl == null) {
            // No thread context class loader -> use class loader of this class.
            cl = ClassUtils.class.getClassLoader();
        }
        return cl;
    }
    
    public static <T> T newInstance(String name, ClassLoader classLoader) {
        try {
            return (T)forName(name, classLoader).newInstance();
        } catch (Exception e) {
            throw new NestedRuntimeException(e);
        }
    }
    
    /**
     * Replacement for <code>Class.forName()</code> that also returns Class instances
     * for primitives (e.g."int") and array class names (e.g. "String[]").
     * Furthermore, it is also capable of resolving inner class names in Java source
     * style (e.g. "java.lang.Thread.State" instead of "java.lang.Thread$State").
     * @param name the name of the Class
     * @param classLoader the class loader to use
     * (may be <code>null</code>, which indicates the default class loader)
     * @return Class instance for the supplied name
     * @throws ClassNotFoundException if the class was not found
     * @throws LinkageError if the class file could not be loaded
     * @see Class#forName(String, boolean, ClassLoader)
     */
    public static Class<?> forName(String name, ClassLoader classLoader) throws ClassNotFoundException, LinkageError {
        Assert.notNull(name, "Name must not be null");

        Class<?> clazz = resolvePrimitiveClassName(name);
        if (clazz == null) {
            clazz = commonClassCache.get(name);
        }
        if (clazz != null) {
            return clazz;
        }

        // "java.lang.String[]" style arrays
        if (name.endsWith(ARRAY_SUFFIX)) {
            String elementClassName = name.substring(0, name.length() - ARRAY_SUFFIX.length());
            Class<?> elementClass = forName(elementClassName, classLoader);
            return Array.newInstance(elementClass, 0).getClass();
        }

        // "[Ljava.lang.String;" style arrays
        if (name.startsWith(NON_PRIMITIVE_ARRAY_PREFIX) && name.endsWith(";")) {
            String elementName = name.substring(NON_PRIMITIVE_ARRAY_PREFIX.length(), name.length() - 1);
            Class<?> elementClass = forName(elementName, classLoader);
            return Array.newInstance(elementClass, 0).getClass();
        }

        // "[[I" or "[[Ljava.lang.String;" style arrays
        if (name.startsWith(INTERNAL_ARRAY_PREFIX)) {
            String elementName = name.substring(INTERNAL_ARRAY_PREFIX.length());
            Class<?> elementClass = forName(elementName, classLoader);
            return Array.newInstance(elementClass, 0).getClass();
        }

        ClassLoader classLoaderToUse = classLoader;
        if (classLoaderToUse == null) {
            classLoaderToUse = getDefaultClassLoader();
        }
        try {
            return classLoaderToUse.loadClass(name);
        }
        catch (ClassNotFoundException ex) {
            int lastDotIndex = name.lastIndexOf('.');
            if (lastDotIndex != -1) {
                String innerClassName = name.substring(0, lastDotIndex) + '$' + name.substring(lastDotIndex + 1);
                try {
                    return classLoaderToUse.loadClass(innerClassName);
                }
                catch (ClassNotFoundException ex2) {
                    // swallow - let original exception get through
                }
            }
            throw ex;
        }
    }

    /**
     * Resolve the given class name into a Class instance. Supports
     * primitives (like "int") and array class names (like "String[]").
     * <p>This is effectively equivalent to the <code>forName</code>
     * method with the same arguments, with the only difference being
     * the exceptions thrown in case of class loading failure.
     * @param className the name of the Class
     * @param classLoader the class loader to use
     * (may be <code>null</code>, which indicates the default class loader)
     * @return Class instance for the supplied name
     * @throws IllegalArgumentException if the class name was not resolvable
     * (that is, the class could not be found or the class file could not be loaded)
     * @see #forName(String, ClassLoader)
     */
    public static Class<?> resolveClassName(String className, ClassLoader classLoader) throws IllegalArgumentException {
        try {
            return forName(className, classLoader);
        }
        catch (ClassNotFoundException ex) {
            throw new IllegalArgumentException("Cannot find class [" + className + "]", ex);
        }
        catch (LinkageError ex) {
            throw new IllegalArgumentException(
                    "Error loading class [" + className + "]: problem with class file or dependent class.", ex);
        }
    }
    
    
    /**
     * Resolve the given class name as primitive class, if appropriate,
     * according to the JVM's naming rules for primitive classes.
     * <p>Also supports the JVM's internal class names for primitive arrays.
     * Does <i>not</i> support the "[]" suffix notation for primitive arrays;
     * this is only supported by {@link #forName(String, ClassLoader)}.
     * @param name the name of the potentially primitive class
     * @return the primitive class, or <code>null</code> if the name does not denote
     * a primitive class or primitive array class
     */
    public static Class<?> resolvePrimitiveClassName(String name) {
        Class<?> result = null;
        // Most class names will be quite long, considering that they
        // SHOULD sit in a package, so a length check is worthwhile.
        if (name != null && name.length() <= 8) {
            // Could be a primitive - likely.
            result = primitiveTypeNameMap.get(name);
        }
        return result;
    }

    /**
     * Determine whether the {@link Class} identified by the supplied name is present
     * and can be loaded. Will return <code>false</code> if either the class or
     * one of its dependencies is not present or cannot be loaded.
     * @param className the name of the class to check
     * @param classLoader the class loader to use
     * (may be <code>null</code>, which indicates the default class loader)
     * @return whether the specified class is present
     */
    public static boolean isPresent(String className, ClassLoader classLoader) {
        try {
            forName(className, classLoader);
            return true;
        }
        catch (Throwable ex) {
            // Class or one of its dependencies is not present...
            return false;
        }
    }

    /**
     * Check whether the given class is cache-safe in the given context,
     * i.e. whether it is loaded by the given ClassLoader or a parent of it.
     * @param clazz the class to analyze
     * @param classLoader the ClassLoader to potentially cache metadata in
     */
    public static boolean isCacheSafe(Class<?> clazz, ClassLoader classLoader) {
        Assert.notNull(clazz, "Class must not be null");
        ClassLoader target = clazz.getClassLoader();
        if (target == null) {
            return false;
        }
        ClassLoader cur = classLoader;
        if (cur == target) {
            return true;
        }
        while (cur != null) {
            cur = cur.getParent();
            if (cur == target) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get the class name without the qualified package name.
     * @param className the className to get the short name for
     * @return the class name of the class without the package name
     * @throws IllegalArgumentException if the className is empty
     */
    public static String getShortName(String className) {
        Assert.hasLength(className, "Class name must not be empty");
        int lastDotIndex = className.lastIndexOf(PACKAGE_SEPARATOR);
        int nameEndIndex = className.length();
        String shortName = className.substring(lastDotIndex + 1, nameEndIndex);
        shortName = shortName.replace(INNER_CLASS_SEPARATOR, PACKAGE_SEPARATOR);
        return shortName;
    }

    /**
     * Get the class name without the qualified package name.
     * @param clazz the class to get the short name for
     * @return the class name of the class without the package name
     */
    public static String getShortName(Class<?> clazz) {
        return getShortName(getQualifiedName(clazz));
    }

    /**
     * Return the short string name of a Java class in uncapitalized JavaBeans
     * property format. Strips the outer class name in case of an inner class.
     * @param clazz the class
     * @return the short name rendered in a standard JavaBeans property format
     * @see java.beans.Introspector#decapitalize(String)
     */
    public static String getShortNameAsProperty(Class<?> clazz) {
        String shortName = ClassUtils.getShortName(clazz);
        int dotIndex = shortName.lastIndexOf('.');
        shortName = (dotIndex != -1 ? shortName.substring(dotIndex + 1) : shortName);
        return Introspector.decapitalize(shortName);
    }

    /**
     * Determine the name of the class file, relative to the containing
     * package: e.g. "String.class"
     * @param clazz the class
     * @return the file name of the ".class" file
     */
    public static String getClassFileName(Class<?> clazz) {
        Assert.notNull(clazz, "Class must not be null");
        String className = clazz.getName();
        int lastDotIndex = className.lastIndexOf(PACKAGE_SEPARATOR);
        return className.substring(lastDotIndex + 1) + CLASS_FILE_SUFFIX;
    }

    /**
     * Determine the name of the package of the given class:
     * e.g. "java.lang" for the <code>java.lang.String</code> class.
     * @param clazz the class
     * @return the package name, or the empty String if the class
     * is defined in the default package
     */
    public static String getPackageName(Class<?> clazz) {
        Assert.notNull(clazz, "Class must not be null");
        String className = clazz.getName();
        int lastDotIndex = className.lastIndexOf(PACKAGE_SEPARATOR);
        return (lastDotIndex != -1 ? className.substring(0, lastDotIndex) : "");
    }

    /**
     * Return the qualified name of the given class: usually simply
     * the class name, but component type class name + "[]" for arrays.
     * @param clazz the class
     * @return the qualified name of the class
     */
    public static String getQualifiedName(Class<?> clazz) {
        Assert.notNull(clazz, "Class must not be null");
        if (clazz.isArray()) {
            return getQualifiedNameForArray(clazz);
        }
        else {
            return clazz.getName();
        }
    }

    /**
     * Build a nice qualified name for an array:
     * component type class name + "[]".
     * @param clazz the array class
     * @return a qualified name for the array class
     */
    private static String getQualifiedNameForArray(Class<?> clazz) {
        StringBuilder result = new StringBuilder();
        Class<?> tmp = clazz;
        while (tmp.isArray()) {
            tmp = tmp.getComponentType();
            result.append(ClassUtils.ARRAY_SUFFIX);
        }
        result.insert(0, tmp.getName());
        return result.toString();
    }

    /**
     * Return a descriptive name for the given object's type: usually simply
     * the class name, but component type class name + "[]" for arrays,
     * and an appended list of implemented interfaces for JDK proxies.
     * @param value the value to introspect
     * @return the qualified name of the class
     */
    public static String getDescriptiveType(Object value) {
        if (value == null) {
            return null;
        }
        Class<?> clazz = value.getClass();
        if (Proxy.isProxyClass(clazz)) {
            StringBuilder result = new StringBuilder(clazz.getName());
            result.append(" implementing ");
            Class<?>[] ifcs = clazz.getInterfaces();
            for (int i = 0; i < ifcs.length; i++) {
                result.append(ifcs[i].getName());
                if (i < ifcs.length - 1) {
                    result.append(',');
                }
            }
            return result.toString();
        }
        else if (clazz.isArray()) {
            return getQualifiedNameForArray(clazz);
        }
        else {
            return clazz.getName();
        }
    }

    /**
     * Check whether the given class matches the user-specified type name.
     * @param clazz the class to check
     * @param typeName the type name to match
     */
    public static boolean matchesTypeName(Class<?> clazz, String typeName) {
        return (typeName != null &&
                (typeName.equals(clazz.getName()) || typeName.equals(clazz.getSimpleName()) ||
                (clazz.isArray() && typeName.equals(getQualifiedNameForArray(clazz)))));
    }

    /**
     * Check if the given class represents a primitive wrapper,
     * i.e. Boolean, Byte, Character, Short, Integer, Long, Float, or Double.
     * @param clazz the class to check
     * @return whether the given class is a primitive wrapper class
     */
    public static boolean isPrimitiveWrapper(Class<?> clazz) {
        Assert.notNull(clazz, "Class must not be null");
        return primitiveWrapperTypeMap.containsKey(clazz);
    }

    /**
     * Check if the given class represents a primitive (i.e. boolean, byte,
     * char, short, int, long, float, or double) or a primitive wrapper
     * (i.e. Boolean, Byte, Character, Short, Integer, Long, Float, or Double).
     * @param clazz the class to check
     * @return whether the given class is a primitive or primitive wrapper class
     */
    public static boolean isPrimitiveOrWrapper(Class<?> clazz) {
        Assert.notNull(clazz, "Class must not be null");
        return (clazz.isPrimitive() || isPrimitiveWrapper(clazz));
    }

    /**
     * Check if the given class represents an array of primitives,
     * i.e. boolean, byte, char, short, int, long, float, or double.
     * @param clazz the class to check
     * @return whether the given class is a primitive array class
     */
    public static boolean isPrimitiveArray(Class<?> clazz) {
        Assert.notNull(clazz, "Class must not be null");
        return (clazz.isArray() && clazz.getComponentType().isPrimitive());
    }

    /**
     * Check if the given class represents an array of primitive wrappers,
     * i.e. Boolean, Byte, Character, Short, Integer, Long, Float, or Double.
     * @param clazz the class to check
     * @return whether the given class is a primitive wrapper array class
     */
    public static boolean isPrimitiveWrapperArray(Class<?> clazz) {
        Assert.notNull(clazz, "Class must not be null");
        return (clazz.isArray() && isPrimitiveWrapper(clazz.getComponentType()));
    }

    /**
     * Resolve the given class if it is a primitive class,
     * returning the corresponding primitive wrapper type instead.
     * @param clazz the class to check
     * @return the original class, or a primitive wrapper for the original primitive type
     */
    public static Class<?> resolvePrimitiveIfNecessary(Class<?> clazz) {
        Assert.notNull(clazz, "Class must not be null");
        return (clazz.isPrimitive() && clazz != void.class ? primitiveTypeToWrapperMap.get(clazz) : clazz);
    }

    /**
     * Check if the right-hand side type may be assigned to the left-hand side
     * type, assuming setting by reflection. Considers primitive wrapper
     * classes as assignable to the corresponding primitive types.
     * @param lhsType the target type
     * @param rhsType the value type that should be assigned to the target type
     * @return if the target type is assignable from the value type
     */
    @SuppressWarnings("rawtypes")
    public static boolean isAssignable(Class<?> lhsType, Class<?> rhsType) {
        Assert.notNull(lhsType, "Left-hand side type must not be null");
        Assert.notNull(rhsType, "Right-hand side type must not be null");
        if (lhsType.isAssignableFrom(rhsType)) {
            return true;
        }
        if (lhsType.isPrimitive()) {
            Class resolvedPrimitive = primitiveWrapperTypeMap.get(rhsType);
            if (resolvedPrimitive != null && lhsType.equals(resolvedPrimitive)) {
                return true;
            }
        }
        else {
            Class resolvedWrapper = primitiveTypeToWrapperMap.get(rhsType);
            if (resolvedWrapper != null && lhsType.isAssignableFrom(resolvedWrapper)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Determine if the given type is assignable from the given value,
     * assuming setting by reflection. Considers primitive wrapper classes
     * as assignable to the corresponding primitive types.
     * @param type the target type
     * @param value the value that should be assigned to the type
     * @return if the type is assignable from the value
     */
    public static boolean isAssignableValue(Class<?> type, Object value) {
        Assert.notNull(type, "Type must not be null");
        return (value != null ? isAssignable(type, value.getClass()) : !type.isPrimitive());
    }

    /**
     * Return a path suitable for use with <code>ClassLoader.getResource</code>
     * (also suitable for use with <code>Class.getResource</code> by prepending a
     * slash ('/') to the return value). Built by taking the package of the specified
     * class file, converting all dots ('.') to slashes ('/'), adding a trailing slash
     * if necessary, and concatenating the specified resource name to this.
     * <br/>As such, this function may be used to build a path suitable for
     * loading a resource file that is in the same package as a class file,
     * although {@link org.springframework.core.io.ClassPathResource} is usually
     * even more convenient.
     * @param clazz the Class whose package will be used as the base
     * @param resourceName the resource name to append. A leading slash is optional.
     * @return the built-up resource path
     * @see java.lang.ClassLoader#getResource
     * @see java.lang.Class#getResource
     */
    public static String addResourcePathToPackagePath(Class<?> clazz, String resourceName) {
        Assert.notNull(resourceName, "Resource name must not be null");
        if (!resourceName.startsWith("/")) {
            return classPackageAsResourcePath(clazz) + "/" + resourceName;
        }
        return classPackageAsResourcePath(clazz) + resourceName;
    }

    /**
     * Given an input class object, return a string which consists of the
     * class's package name as a pathname, i.e., all dots ('.') are replaced by
     * slashes ('/'). Neither a leading nor trailing slash is added. The result
     * could be concatenated with a slash and the name of a resource and fed
     * directly to <code>ClassLoader.getResource()</code>. For it to be fed to
     * <code>Class.getResource</code> instead, a leading slash would also have
     * to be prepended to the returned value.
     * @param clazz the input class. A <code>null</code> value or the default
     * (empty) package will result in an empty string ("") being returned.
     * @return a path which represents the package name
     * @see ClassLoader#getResource
     * @see Class#getResource
     */
    public static String classPackageAsResourcePath(Class<?> clazz) {
        if (clazz == null) {
            return "";
        }
        String className = clazz.getName();
        int packageEndIndex = className.lastIndexOf('.');
        if (packageEndIndex == -1) {
            return "";
        }
        String packageName = className.substring(0, packageEndIndex);
        return packageName.replace('.', '/');
    }

    /**
     * Return all interfaces that the given class implements as Set,
     * including ones implemented by superclasses.
     * <p>注意：If the class itself is an interface, it gets returned as sole interface.
     * @see {@link #getAllInterfaces(Class)}
     * 
     * @param clazz the class to analyze for interfaces
     * @return all interfaces that the given object implements as Set
     */
    @SuppressWarnings("rawtypes")
    public static Set<Class> getAllInterfacesForClassAsSet(Class clazz) {
        return getAllInterfacesForClassAsSet(clazz, null);
    }

    /**
     * Return all interfaces that the given class implements as Set,
     * including ones implemented by superclasses.
     * <p>注意：If the class itself is an interface, it gets returned as sole interface.
     * @see {@link #getAllInterfaces(Class)}
     * 
     * @param clazz the class to analyze for interfaces
     * @param classLoader the ClassLoader that the interfaces need to be visible in
     * (may be <code>null</code> when accepting all declared interfaces)
     * @return all interfaces that the given object implements as Set
     */
    @SuppressWarnings("rawtypes")
    public static Set<Class> getAllInterfacesForClassAsSet(Class clazz, ClassLoader classLoader) {
        Assert.notNull(clazz, "Class must not be null");
        if (clazz.isInterface() && isVisible(clazz, classLoader)) {
            return Collections.singleton(clazz);
        }
        Set<Class> interfaces = new LinkedHashSet<Class>();
        Class tmp = clazz;
        while (tmp != null) {
            Class<?>[] ifcs = tmp.getInterfaces();
            for (Class<?> ifc : ifcs) {
                interfaces.addAll(getAllInterfacesForClassAsSet(ifc, classLoader));
            }
            tmp = tmp.getSuperclass();
        }
        return interfaces;
    }

    /**
     * Check whether the given class is visible in the given ClassLoader.
     * @param clazz the class to check (typically an interface)
     * @param classLoader the ClassLoader to check against (may be <code>null</code>,
     * in which case this method will always return <code>true</code>)
     */
    public static boolean isVisible(Class<?> clazz, ClassLoader classLoader) {
        if (classLoader == null) {
            return true;
        }
        try {
            Class<?> actualClass = classLoader.loadClass(clazz.getName());
            return (clazz == actualClass);
            // Else: different interface class found...
        }
        catch (ClassNotFoundException ex) {
            // No interface class found...
            return false;
        }
    }
    
    /**
     * Get all super classes of the class.
     * 
     * @param clazz
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static Set<Class> getAllSuperClass(Class<?> clazz) {
        Set<Class> set = new LinkedHashSet<Class>();
        Class tmp = clazz;
        while (tmp != null) {
            Class<?> superClass = tmp.getSuperclass();
            if (superClass != null) {
                set.add(superClass);
            }
            tmp = superClass;
        }
        return set;
    }

    /**
     * @see {@link #findAllAssignableClass(Class, ClassLoader)}
     * @param clazz
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static Set<Class> findAllAssignableClass(Class<?> clazz) {
        return findAllAssignableClass(clazz, null);
    }

    /**
     * Find all the classes which is either the same as, or is a superclass or superinterface of.
     * 
     * @param clazz
     * @param classLoader
     *            ClassLoader for check isVisible.
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static Set<Class> findAllAssignableClass(Class<?> clazz, ClassLoader classLoader) {
        Set<Class> interfaces = getInterfaces0(clazz, classLoader);
        Set<Class> supers = getAllSuperClass(clazz);
        for (Class c : supers) {
            if (c.equals(Object.class)) {
                continue;
            }
            Set<Class> tmp = getInterfaces0(c, classLoader);
            interfaces.addAll(tmp);
        }
        return interfaces;
    }

    /**
     * Get the Set includes the class and all it's interfaces and superinterfaces.
     */
    @SuppressWarnings("rawtypes")
    private static Set<Class> getInterfaces0(Class<?> c, ClassLoader classLoader) {
        Set<Class> ifs = new LinkedHashSet<Class>();
        if (isVisible(c, classLoader)) {
            ifs.add(c);
        }
        Class<?>[] interfaces = c.getInterfaces();
        for (Class<?> i : interfaces) {
            ifs.addAll(getInterfaces0(i, classLoader));
        }
        return ifs;
    }

    @SuppressWarnings("rawtypes")
    public static Set<Class> getAllInterfaces(Class<?> c) {
        return getAllInterfaces(c, null);
    }

    /**
     * get all interfaces of the class. include all ancestors of all the interfaces of the class, but not include interface from the parent or
     * ancestors of the class.
     * 
     * @see {@link #isVisible(Class, ClassLoader)}
     * 
     * @param c
     * @param classLoader
     *            ClassLoader for check isVisible.
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static Set<Class> getAllInterfaces(Class<?> c, ClassLoader classLoader) {
        Set<Class> names = getInterfaces0(c, classLoader);
        names.remove(c);
        return names;
    }

    
    private static List<Class<? extends Number>> numRanges;
    static {
        numRanges = new LinkedList<Class<? extends Number>>();
        numRanges.add(Double.class);
        numRanges.add(double.class);
        numRanges.add(Double.class);
        
        numRanges.add(Float.class);
        numRanges.add(float.class);
        numRanges.add(Float.class);
        
        numRanges.add(Long.class);
        numRanges.add(long.class);
        numRanges.add(Long.class);
        
        numRanges.add(Integer.class);
        numRanges.add(int.class);
        numRanges.add(Integer.class);
        
        numRanges.add(Short.class);
        numRanges.add(short.class);
        numRanges.add(Short.class);
        
        numRanges.add(Byte.class);
        numRanges.add(byte.class);
        numRanges.add(Byte.class);
    }
    
    /**
     * 判断 数值类型source 是否能赋值给 数值类型target， 赋值顺序： byte -> short -> int -> long -> float -> double
     */
    public static boolean isNumAssignableFrom(Class<?> target, Class<?> source) {
        int tgt = numRanges.indexOf(target);
        int src = numRanges.indexOf(source);
        if (tgt == -1 || src == -1) {
            return false;
        }
        return Math.abs(numRanges.indexOf(target) - numRanges.indexOf(source)) <= 1
                || numRanges.indexOf(source) - numRanges.indexOf(target) > 1;
    }
    
    public static boolean isAssignableFromWithNum(Class<?> target, Class<?> source) {
        if (Number.class.isAssignableFrom(target) || target.isPrimitive()) {
            return ClassUtils.isNumAssignableFrom(target, source);
        }
        return target.isAssignableFrom(source);
    }
    
}
