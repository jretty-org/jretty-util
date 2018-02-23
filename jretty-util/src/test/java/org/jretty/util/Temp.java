package org.jretty.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.security.AccessControlException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.jretty.util.Assert;
import org.jretty.util.BasicRuntimeException;
import org.jretty.util.ClassUtils;
import org.jretty.util.ReflectionUtils;

class Temp {

    
    static class ReflectionUtil {
        
        public static void fieldCloneByName2(Object source, Object target) {
            Map<String, Field> map = ReflectionUtils.getAllNonStaticFields(target.getClass());
            Iterator<Entry<String, Field>> it = map.entrySet().iterator();
            Map<String, Field> sourceMap = ReflectionUtils.getAllNonStaticFields(source.getClass());
            while (it.hasNext()) {
                Entry<String, Field> entry = it.next();
                Field targetField = entry.getValue();
                Field sourceField = sourceMap.get(entry.getKey());
                if (sourceField != null && 
                        ClassUtils.isAssignableWithNum(targetField.getType(), sourceField.getType())) {
                    ReflectionUtils.makeAccessible(sourceField);
                    ReflectionUtils.makeAccessible(targetField);
                    Object value = ReflectionUtils.getField(sourceField, source);
                    if(Number.class.isAssignableFrom(targetField.getType())) {
                        value = ReflectionUtils.convertNumber((Number) value, targetField.getType());
                    }
                    ReflectionUtils.setField(targetField, target, value);
                }
            }
        }
        
        /**
         * Return the number of methods with a given name (with any argument types),
         * for the given class and/or its superclasses. Includes non-public methods.
         * @param clazz the clazz to check
         * @param methodName the name of the method
         * @return the number of methods with the given name
         */
        public static int getMethodCountForName(Class<?> clazz, String methodName) {
            Assert.notNull(clazz, "Class must not be null");
            Assert.notNull(methodName, "Method name must not be null");
            int count = 0;
            Method[] declaredMethods = clazz.getDeclaredMethods();
            for (Method method : declaredMethods) {
                if (methodName.equals(method.getName())) {
                    count++;
                }
            }
            Class<?>[] ifcs = clazz.getInterfaces();
            for (Class<?> ifc : ifcs) {
                count += getMethodCountForName(ifc, methodName);
            }
            if (clazz.getSuperclass() != null) {
                count += getMethodCountForName(clazz.getSuperclass(), methodName);
            }
            return count;
        }

        /**
         * Does the given class or one of its superclasses at least have one or more
         * methods with the supplied name (with any argument types)?
         * Includes non-public methods.
         * @param clazz the clazz to check
         * @param methodName the name of the method
         * @return whether there is at least one method with the given name
         */
        public static boolean hasAtLeastOneMethodWithName(Class<?> clazz, String methodName) {
            Assert.notNull(clazz, "Class must not be null");
            Assert.notNull(methodName, "Method name must not be null");
            Method[] declaredMethods = clazz.getDeclaredMethods();
            for (Method method : declaredMethods) {
                if (method.getName().equals(methodName)) {
                    return true;
                }
            }
            Class<?>[] ifcs = clazz.getInterfaces();
            for (Class<?> ifc : ifcs) {
                if (hasAtLeastOneMethodWithName(ifc, methodName)) {
                    return true;
                }
            }
            return (clazz.getSuperclass() != null && hasAtLeastOneMethodWithName(clazz.getSuperclass(), methodName));
        }
        
        /**
         * Given a method, which may come from an interface, and a target class used
         * in the current reflective invocation, find the corresponding target method
         * if there is one. E.g. the method may be <code>IFoo.bar()</code> and the
         * target class may be <code>DefaultFoo</code>. In this case, the method may be
         * <code>DefaultFoo.bar()</code>. This enables attributes on that method to be found.
         * <p><b>NOTE:</b> In contrast to {@link org.springframework.aop.support.AopUtils#getMostSpecificMethod},
         * this method does <i>not</i> resolve Java 5 bridge methods automatically.
         * Call {@link org.springframework.core.BridgeMethodResolver#findBridgedMethod}
         * if bridge method resolution is desirable (e.g. for obtaining metadata from
         * the original method definition).
         * <p><b>NOTE:</b>Since Spring 3.1.1, if java security settings disallow reflective
         * access (e.g. calls to {@code Class#getDeclaredMethods} etc, this implementation
         * will fall back to returning the originally provided method.
         * @param method the method to be invoked, which may come from an interface
         * @param targetClass the target class for the current invocation.
         * May be <code>null</code> or may not even implement the method.
         * @return the specific target method, or the original method if the
         * <code>targetClass</code> doesn't implement it or is <code>null</code>
         */
        public static Method getMostSpecificMethod(Method method, Class<?> targetClass) {
            Method specificMethod = null;
            if (method != null && isOverridable(method, targetClass) &&
                    targetClass != null && !targetClass.equals(method.getDeclaringClass())) {
                try {
                    specificMethod = ReflectionUtils.findMethod(targetClass, method.getName(), method.getParameterTypes());
                } catch (AccessControlException ex) {
                    // security settings are disallowing reflective access; leave
                    // 'specificMethod' null and fall back to 'method' below
                }
            }
            return (specificMethod != null ? specificMethod : method);
        }

        /**
         * Determine whether the given method is overridable in the given target class.
         * @param method the method to check
         * @param targetClass the target class to check against
         */
        @SuppressWarnings("rawtypes")
        private static boolean isOverridable(Method method, Class targetClass) {
            if (Modifier.isPrivate(method.getModifiers())) {
                return false;
            }
            if (Modifier.isPublic(method.getModifiers()) || Modifier.isProtected(method.getModifiers())) {
                return true;
            }
            return ClassUtils.getPackageName(method.getDeclaringClass())
                    .equals(ClassUtils.getPackageName(targetClass));
        }
        
        public static Object getAttributeValue(Object instance, String attrName) throws Exception {
            // step1
            String name = attrName.replaceFirst(attrName.substring(0, 1), attrName.substring(0, 1).toUpperCase());
            Method plmethod = ReflectionUtils.findMethod(instance.getClass(), "get" + name);
            if (null == plmethod) {
                plmethod = ReflectionUtils.findMethod(instance.getClass(), "is" + name);
            }
            if (null != plmethod) {
                return plmethod.invoke(instance);
            }
            // step2
            Field field = ReflectionUtils.findField(instance.getClass(), attrName);
            try {
                return ReflectionUtils.getField(field, instance);
            } catch (Exception e) {
                // ignore
                ReflectionUtils.makeAccessible(field);
                try {
                    return ReflectionUtils.getField(field, instance);
                } catch (Exception ex) {
                    // ignore
                }
            }
            throw new BasicRuntimeException("can't get this filed's value {}", attrName);
        }

        public static void setAttributeValue(Object instance, String attrName, Object attrValue) throws Exception {
            // step1
            String name = attrName.replaceFirst(attrName.substring(0, 1), attrName.substring(0, 1).toUpperCase());
            Method plmethod = null;
            plmethod = findSetterMethod(instance.getClass(), "set" + name);

            if (plmethod != null) {
                plmethod.invoke(instance, attrValue);
                return;
            }
            // step2
            Field field = ReflectionUtils.findField(instance.getClass(), attrName);
            try {
                ReflectionUtils.setField(field, instance, attrValue);
            } catch (Exception e) {
                // ignore
                ReflectionUtils.makeAccessible(field);
                try {
                    ReflectionUtils.setField(field, instance, attrValue);
                } catch (Exception exp) {
                    throw new BasicRuntimeException(exp, "can't set this filed({})'s value to {}", attrName,
                            instance.getClass().getName());
                }
            }
        }

        /**
         * only used by this class
         * 
         * @see #setAttributeValue(Object, String, Object)
         */
        private static Method findSetterMethod(Class<?> clazz, String name) {
            Assert.notNull(clazz, "Class must not be null");
            Assert.notNull(name, "Method name must not be null");
            Class<?> searchType = clazz;
            while (searchType != null) {
                Method[] methods = (searchType.isInterface() ? searchType.getMethods()
                        : searchType.getDeclaredMethods());
                for (Method method : methods) {
                    if (name.equals(method.getName()) && method.getParameterTypes().length == 1) {
                        return method;
                    }
                }
                searchType = searchType.getSuperclass();
            }
            return null;
        }
    }
    
    
    static class UrlUtil {
//      /**
//      * Return whether the given resource location is a URL:
//      * either a special "classpath" pseudo URL or a standard URL.
//      * @param resourceLocation the location String to check
//      * @return whether the location qualifies as a URL
//      * @see #CLASSPATH_URL_PREFIX
//      * @see java.net.URL
//      */
//     public static boolean isUrl(String resourceLocation) {
//         if (resourceLocation == null) {
//             return false;
//         }
//         if (resourceLocation.startsWith(CLASSPATH_URL_PREFIX)) {
//             return true;
//         }
//         try {
//             new URL(resourceLocation);
//             return true;
//         }
//         catch (MalformedURLException ex) {
//             return false;
//         }
//     }
 //
//     /**
//      * Resolve the given resource location to a <code>java.net.URL</code>.
//      * <p>Does not check whether the URL actually exists; simply returns
//      * the URL that the given location would correspond to.
//      * @param resourceLocation the resource location to resolve: either a
//      * "classpath:" pseudo URL, a "file:" URL, or a plain file path
//      * @return a corresponding URL object
//      * @throws FileNotFoundException if the resource cannot be resolved to a URL
//      */
//     public static URL getURL(String resourceLocation) throws FileNotFoundException {
//         Assert.notNull(resourceLocation, "Resource location must not be null");
//         if (resourceLocation.startsWith(CLASSPATH_URL_PREFIX)) {
//             String path = resourceLocation.substring(CLASSPATH_URL_PREFIX.length());
//             URL url = ClassUtils.getDefaultClassLoader().getResource(path);
//             if (url == null) {
//                 String description = "class path resource [" + path + "]";
//                 throw new FileNotFoundException(
//                         description + " cannot be resolved to URL because it does not exist");
//             }
//             return url;
//         }
//         try {
//             // try URL
//             return new URL(resourceLocation);
//         }
//         catch (MalformedURLException ex) {
//             // no URL -> treat as file path
//             try {
//                 return new File(resourceLocation).toURI().toURL();
//             }
//             catch (MalformedURLException ex2) {
//                 throw new FileNotFoundException("Resource location [" + resourceLocation +
//                         "] is neither a URL not a well-formed file path");
//             }
//         }
//     }
 //
//     /**
//      * Resolve the given resource location to a <code>java.io.File</code>,
//      * i.e. to a file in the file system.
//      * <p>Does not check whether the fil actually exists; simply returns
//      * the File that the given location would correspond to.
//      * @param resourceLocation the resource location to resolve: either a
//      * "classpath:" pseudo URL, a "file:" URL, or a plain file path
//      * @return a corresponding File object
//      * @throws FileNotFoundException if the resource cannot be resolved to
//      * a file in the file system
//      */
//     public static File getFile(String resourceLocation) throws FileNotFoundException {
//         Assert.notNull(resourceLocation, "Resource location must not be null");
//         if (resourceLocation.startsWith(CLASSPATH_URL_PREFIX)) {
//             String path = resourceLocation.substring(CLASSPATH_URL_PREFIX.length());
//             String description = "class path resource [" + path + "]";
//             URL url = ClassUtils.getDefaultClassLoader().getResource(path);
//             if (url == null) {
//                 throw new FileNotFoundException(
//                         description + " cannot be resolved to absolute file path " +
//                         "because it does not reside in the file system");
//             }
//             return getFile(url, description);
//         }
//         try {
//             // try URL
//             return getFile(new URL(resourceLocation));
//         }
//         catch (MalformedURLException ex) {
//             // no URL -> treat as file path
//             return new File(resourceLocation);
//         }
//     }

    }

}