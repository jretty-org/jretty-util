package org.jretty.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


class ReflectionMethodUtils {
    
    /**
     * Return the qualified name of the given method, consisting of
     * fully qualified interface/class name + "." + method name.
     * @param method the method
     * @return the qualified name of the method
     */
    public static String getQualifiedMethodName(Method method) {
        Assert.notNull(method, "Method must not be null");
        return method.getDeclaringClass().getName() + "." + method.getName();
    }
    
    /**
     * Determine whether the given method is an "equals" method.
     * @see java.lang.Object#equals(Object)
     */
    public static boolean isEqualsMethod(Method method) {
        if (method == null || !method.getName().equals("equals")) {
            return false;
        }
        Class<?>[] paramTypes = method.getParameterTypes();
        return (paramTypes.length == 1 && paramTypes[0] == Object.class);
    }

    /**
     * Determine whether the given method is a "hashCode" method.
     * @see java.lang.Object#hashCode()
     */
    public static boolean isHashCodeMethod(Method method) {
        return (method != null && method.getName().equals("hashCode") && method.getParameterTypes().length == 0);
    }

    /**
     * Determine whether the given method is a "toString" method.
     * @see java.lang.Object#toString()
     */
    public static boolean isToStringMethod(Method method) {
        return (method != null && method.getName().equals("toString") && method.getParameterTypes().length == 0);
    }

    /**
     * Determine whether the given method is originally declared by {@link java.lang.Object}.
     */
    public static boolean isObjectMethod(Method method) {
        try {
            Object.class.getDeclaredMethod(method.getName(), method.getParameterTypes());
            return true;
        } catch (SecurityException ex) {
            return false;
        } catch (NoSuchMethodException ex) {
            return false;
        }
    }
    
    /**
     * Invoke the specified {@link Method} against the supplied target object with the
     * supplied arguments. The target object can be <code>null</code> when invoking a
     * static {@link Method}.
     * <p>Thrown exceptions are handled via a call to {@link #handleReflectionException}.
     * @param method the method to invoke
     * @param target the target object to invoke the method on
     * @param args the invocation arguments (may be <code>null</code>)
     * @return the invocation result, if any
     */
    public static Object invokeMethod(Method method, Object target, Object... args) {
        try {
            return method.invoke(target, args);
        } catch (InvocationTargetException e) {
            throw new NestedRuntimeException(e.getCause());
        } catch (Exception e) {
            throw new NestedRuntimeException(e);
        } catch (ExceptionInInitializerError e) {
            throw new NestedRuntimeException(e.getCause());
        }
    }
    
    public static Object invokeMethod(String methodName, Object target, Object... args) {
        return invokeMethod(methodName, new Class<?>[0], target, args);
    }

    public static Object invokeMethod(String methodName, Class<?>[] parameterTypes,
            Object target, Object... args) {
        Method method = getDeclaredMethod(target.getClass(), methodName, parameterTypes);
        ReflectionUtils.makeAccessible(method);
        return invokeMethod(method, target, args);
    }
    
    public static Object invokeStaticMethod(String methodName, Class<?> clazz, Object... args) {
        return invokeStaticMethod(methodName, new Class<?>[0], clazz, args);
    }
    
    public static Object invokeStaticMethod(String methodName, Class<?>[] parameterTypes,
            Class<?> clazz, Object... args) {
        Method method = getDeclaredMethod(clazz, methodName, parameterTypes);
        ReflectionUtils.makeAccessible(method);
        return invokeMethod(method, null, args);
    }

    public static Object invokeConstructor(Constructor<?> constructor, Object... args) {
        try {
            return constructor.newInstance(args);
        } catch (InvocationTargetException e) {
            throw new NestedRuntimeException(e.getCause());
        } catch (Exception e) {
            throw new NestedRuntimeException(e);
        } catch (ExceptionInInitializerError e) {
            throw new NestedRuntimeException(e.getCause());
        }
    }

    public static Object invokeConstructor(Class<?> clazz, Class<?>[] parameterTypes, Object... args) {
        Constructor<?> constructor = getDeclaredConstructor(clazz, parameterTypes);
        ReflectionUtils.makeAccessible(constructor);
        return invokeConstructor(constructor, args);
    }
    

    /**
     * Return a public static method of a class.
     * @param methodName the static method name
     * @param clazz the class which defines the method
     * @param args the parameter types to the method
     * @return the static method, or <code>null</code> if no static method was found
     * @throws IllegalArgumentException if the method name is blank or the clazz is null
     */
    public static Method getStaticMethod(Class<?> clazz, String methodName, Class<?>... args) {
        Assert.notNull(clazz, "Class must not be null");
        Assert.notNull(methodName, "Method name must not be null");
        try {
            Method method = clazz.getMethod(methodName, args);
            return Modifier.isStatic(method.getModifiers()) ? method : null;
        } catch (NoSuchMethodException ex) {
            return null;
        }
    }
    
    
    /**
     * Determine whether the given class has a method with the given signature,
     * and return it if available (else return <code>null</code>).
     * <p>Essentially translates <code>NoSuchMethodException</code> to <code>null</code>.
     * @param clazz the clazz to analyze
     * @param methodName the name of the method
     * @param paramTypes the parameter types of the method
     * @return the method, or <code>null</code> if not found
     * @see java.lang.Class#getMethod
     */
    public static Method getMethod(Class<?> clazz, String methodName, Class<?>... paramTypes) {
        Assert.notNull(clazz, "Class must not be null");
        Assert.notNull(methodName, "Method name must not be null");
        try {
            return clazz.getMethod(methodName, paramTypes);
        }
        catch (NoSuchMethodException ex) {
            return null;
        }
    }
    
    public static Method getDeclaredMethod(Class<?> clazz, String methodName, Class<?>... paramTypes) {
        Assert.notNull(clazz, "Class must not be null");
        Assert.notNull(methodName, "Method name must not be null");
        try {
            return clazz.getDeclaredMethod(methodName, paramTypes);
        }
        catch (NoSuchMethodException ex) {
            return null;
        }
    }
    
    /**
     * Attempt to find a {@link Method} on the supplied class with the supplied name
     * and parameter types. Searches all superclasses up to <code>Object</code>.
     * <p>Returns <code>null</code> if no {@link Method} can be found.
     * @param clazz the class to introspect
     * @param name the name of the method
     * @param paramTypes the parameter types of the method
     * (may be <code>null</code> to indicate any signature)
     * @return the Method object, or <code>null</code> if none found
     */
    public static Method findMethod(Class<?> clazz, String name, Class<?>... paramTypes) {
        Assert.notNull(clazz, "Class must not be null");
        Assert.notNull(name, "Method name must not be null");
        Class<?> searchType = clazz;
        while (searchType != null) {
            Method[] methods = (searchType.isInterface() ? searchType.getMethods() : searchType.getDeclaredMethods());
            for (Method method : methods) {
                if (name.equals(method.getName())
                        && (paramTypes == null || Arrays.equals(paramTypes, method.getParameterTypes()))) {
                    return method;
                }
            }
            searchType = searchType.getSuperclass();
        }
        return null;
    }

    /**
     * Determine whether the given class has a public constructor with the given signature,
     * and return it if available (else return <code>null</code>).
     * <p>Essentially translates <code>NoSuchMethodException</code> to <code>null</code>.
     * @param clazz the clazz to analyze
     * @param paramTypes the parameter types of the method
     * @return the constructor, or <code>null</code> if not found
     * @see java.lang.Class#getConstructor
     */
    public static <T> Constructor<T> getConstructor(Class<T> clazz, Class<?>... paramTypes) {
        Assert.notNull(clazz, "Class must not be null");
        try {
            return clazz.getConstructor(paramTypes);
        }
        catch (NoSuchMethodException ex) {
            return null;
        }
    }
    
    public static <T> Constructor<T> getDeclaredConstructor(Class<T> clazz, Class<?>... paramTypes) {
        Assert.notNull(clazz, "Class must not be null");
        try {
            return clazz.getDeclaredConstructor(paramTypes);
        }
        catch (NoSuchMethodException ex) {
            return null;
        }
    }
    
    
    /**
     * Get all declared methods on the leaf class and all superclasses. Leaf
     * class methods are included first.
     */
    public static List<Method> getAllDeclaredMethods(Class<?> leafClass) {
        final List<Method> methods = new ArrayList<Method>(32);
        doWithMethods(leafClass, new MethodCallback() {
            public void doWith(Method method) {
                methods.add(method);
            }
        });
        return methods;
    }

    /**
     * Get the unique set of declared methods on the leaf class and all superclasses. Leaf
     * class methods are included first and while traversing the superclass hierarchy any methods found
     * with signatures matching a method already included are filtered out.
     */
    public static List<Method> getUniqueDeclaredMethods(Class<?> leafClass) {
        final List<Method> methods = new ArrayList<Method>(32);
        doWithMethods(leafClass, new MethodCallback() {
            public void doWith(Method method) {
                boolean knownSignature = false;
                Method methodBeingOverriddenWithCovariantReturnType = null;

                for (Method existingMethod : methods) {
                    if (method.getName().equals(existingMethod.getName()) &&
                            Arrays.equals(method.getParameterTypes(), existingMethod.getParameterTypes())) {
                        // is this a covariant return type situation?
                        if (existingMethod.getReturnType() != method.getReturnType() &&
                                existingMethod.getReturnType().isAssignableFrom(method.getReturnType())) {
                            methodBeingOverriddenWithCovariantReturnType = existingMethod;
                        } else {
                            knownSignature = true;
                        }
                        break;
                    }
                }
                if (methodBeingOverriddenWithCovariantReturnType != null) {
                    methods.remove(methodBeingOverriddenWithCovariantReturnType);
                }
                if (!knownSignature) {
                    methods.add(method);
                }
            }
        });
        return methods;
    }

    
    /**
     * Determine whether the given method explicitly declares the given
     * exception or one of its superclasses, which means that an exception of
     * that type can be propagated as-is within a reflective invocation.
     * @param method the declaring method
     * @param exceptionType the exception to throw
     * @return <code>true</code> if the exception can be thrown as-is;
     * <code>false</code> if it needs to be wrapped
     */
    public static boolean declaresException(Method method, Class<?> exceptionType) {
        Assert.notNull(method, "Method must not be null");
        Class<?>[] declaredExceptions = method.getExceptionTypes();
        for (Class<?> declaredException : declaredExceptions) {
            if (declaredException.isAssignableFrom(exceptionType)) {
                return true;
            }
        }
        return false;
    }
    
    
    /**
     * @see #doWithMethods(Class, MethodCallback, MethodFilter)
     */
    public static void doWithMethods(Class<?> clazz, MethodCallback mc) {
        doWithMethods(clazz, mc, null);
    }

    /**
     * Perform the given callback operation on all matching methods of the given
     * class and superclasses (or given interface and super-interfaces).
     * <p>The same named method occurring on subclass and superclass will appear
     * twice, unless excluded by the specified {@link MethodFilter}.
     * @param clazz class to start looking at
     * @param mc the callback to invoke for each method
     * @param mf the filter that determines the methods to apply the callback to
     */
    public static void doWithMethods(Class<?> clazz, MethodCallback mc, MethodFilter mf) {
        // Keep backing up the inheritance hierarchy.
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            if (mf != null && !mf.matches(method)) {
                continue;
            }
            mc.doWith(method);
        }
        if (clazz.getSuperclass() != null) {
            doWithMethods(clazz.getSuperclass(), mc, mf);
            
        } else if (clazz.isInterface()) {
            for (Class<?> superIfc : clazz.getInterfaces()) {
                doWithMethods(superIfc, mc, mf);
            }
        }
    }
    
    
    /**
     * Action to take on each method.
     */
    public interface MethodCallback {

        /**
         * Perform an operation using the given method.
         * @param method the method to operate on
         */
        void doWith(Method method);
    }


    /**
     * Callback optionally used to filter methods to be operated on by a method callback.
     */
    public interface MethodFilter {

        /**
         * Determine whether the given method matches.
         * @param method the method to check
         */
        boolean matches(Method method);
    }

    
    /**
     * Pre-built MethodFilter that matches all non-bridge methods.
     */
    public static final MethodFilter NON_BRIDGED_METHODS = new MethodFilter() {

        public boolean matches(Method method) {
            return !method.isBridge();
        }
    };


    /**
     * Pre-built MethodFilter that matches all non-bridge methods
     * which are not declared on <code>java.lang.Object</code>.
     */
    public static final MethodFilter USER_DECLARED_METHODS = new MethodFilter() {

        public boolean matches(Method method) {
            return (!method.isBridge() && method.getDeclaringClass() != Object.class);
        }
    };

}
