/* 
 * Copyright (C) 2014-2015 the original author or authors.
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

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * Simple utility class for working with 
 * the reflection API and handling reflection exceptions.<br>
 * {Java 反射API增强工具类}
 *
 * @auther Zollty Tsou
 * 
 * @since 2014-6-01
 */
public class ReflectionUtils extends ReflectionMethodUtils {
    
    //━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓
    // 静默调用反射方法
    //━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛
    
    /**
     * a slient way to invoke the class.newInstance() method.
     */
    public static <T> T newInstance(Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (Exception e) {
            throw new NestedRuntimeException(e);
        } catch (ExceptionInInitializerError e) {
            throw new NestedRuntimeException(e.getCause());
        }
    }
    
    /**
     * Set the field represented by the supplied {@link Field field object} on the
     * specified {@link Object target object} to the specified <code>value</code>.
     * In accordance with {@link Field#set(Object, Object)} semantics, the new value
     * is automatically unwrapped if the underlying field has a primitive type.
     * <p>Thrown exceptions are handled via a call to {@link #handleReflectionException(Exception)}.
     * @param field the field to set
     * @param target the target object on which to set the field
     * @param value the value to set; may be <code>null</code>
     */
    public static void setField(Field field, Object target, Object value) {
        try {
            field.set(target, value);
        } catch (Exception e) {
            throw new NestedRuntimeException(e);
        } catch (ExceptionInInitializerError e) {
            throw new NestedRuntimeException(e.getCause());
        }
    }

    /**
     * Get the field represented by the supplied {@link Field field object} on the
     * specified {@link Object target object}. In accordance with {@link Field#get(Object)}
     * semantics, the returned value is automatically wrapped if the underlying field
     * has a primitive type.
     * <p>Thrown exceptions are handled via a call to {@link #handleReflectionException(Exception)}.
     * @param field the field to get
     * @param target the target object from which to get the field
     * @return the field's current value
     */
    public static Object getField(Field field, Object target) {
        try {
            return field.get(target);
        } catch (Exception e) {
            throw new NestedRuntimeException(e);
        } catch (ExceptionInInitializerError e) {
            throw new NestedRuntimeException(e.getCause());
        }
    }

    /**
     * Determine whether the given field is a "public static final" constant.
     * @param field the field to check
     */
    public static boolean isPublicStaticFinal(Field field) {
        int modifiers = field.getModifiers();
        return (Modifier.isPublic(modifiers) && Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers));
    }

    /**
     * Make the given field accessible, explicitly setting it accessible if
     * necessary. The <code>setAccessible(true)</code> method is only called
     * when actually necessary, to avoid unnecessary conflicts with a JVM
     * SecurityManager (if active).
     * @param field the field to make accessible
     * @see java.lang.reflect.Field#setAccessible
     */
    public static void makeAccessible(Field field) {
        if ((!Modifier.isPublic(field.getModifiers()) || !Modifier.isPublic(field.getDeclaringClass().getModifiers()) ||
                Modifier.isFinal(field.getModifiers())) && !field.isAccessible()) {
            field.setAccessible(true);
        }
    }

    /**
     * Make the given method accessible, explicitly setting it accessible if
     * necessary. The <code>setAccessible(true)</code> method is only called
     * when actually necessary, to avoid unnecessary conflicts with a JVM
     * SecurityManager (if active).
     * @param method the method to make accessible
     * @see java.lang.reflect.Method#setAccessible
     */
    public static void makeAccessible(Method method) {
        if ((!Modifier.isPublic(method.getModifiers()) || !Modifier.isPublic(method.getDeclaringClass().getModifiers()))
                && !method.isAccessible()) {
            method.setAccessible(true);
        }
    }

    /**
     * Make the given constructor accessible, explicitly setting it accessible
     * if necessary. The <code>setAccessible(true)</code> method is only called
     * when actually necessary, to avoid unnecessary conflicts with a JVM
     * SecurityManager (if active).
     * @param ctor the constructor to make accessible
     * @see java.lang.reflect.Constructor#setAccessible
     */
    public static void makeAccessible(Constructor<?> ctor) {
        if ((!Modifier.isPublic(ctor.getModifiers()) || !Modifier.isPublic(ctor.getDeclaringClass().getModifiers()))
                && !ctor.isAccessible()) {
            ctor.setAccessible(true);
        }
    }

	/**
	 * Given the source object and the destination, which must be the same class
	 * or a subclass, copy all fields, including inherited fields. Designed to
	 * work on objects with public no-arg constructors.
	 * @throws IllegalArgumentException if the arguments are incompatible
	 */
	public static void fieldCloneByInherit(final Object parentSource, final Object dest) {
	    Assert.notNull(parentSource, "Source for field copy cannot be null");
        Assert.notNull(dest, "Destination for field copy cannot be null");
		if (!parentSource.getClass().isAssignableFrom(dest.getClass())) {
			throw new IllegalArgumentException("Destination class [" + dest.getClass().getName()
					+ "] must be same or subclass as source class [" + parentSource.getClass().getName() + "]");
		}
		doWithFields(parentSource.getClass(), new FieldCallback() {
		    
			public void doWith(Field field) {
				makeAccessible(field);
				Object srcValue = getField(field, parentSource);
				setField(field, dest, srcValue);
			}
		}, COPYABLE_FIELDS);
	}
    
	/**
     * 两个对象属性的拷贝：将source对象的属性值，赋值给target对象对应的名字和类型相同的属性。<br>
     * 比如可以实现：父类转子类——将父类的属性值拷贝给子类。（当然，只需要属性名称和类型一致就行了，不需要继承关系）
     * <p>支持数值类型 把小范围的值 赋值给 大范围的值，比如Float赋值给Double 
     * @param source 原始对象[orginal obj]
     * @param target 目标对象[target obj]
     */
	public static void fieldCloneByName(final Object source, final Object target) {
	    Assert.notNull(source, "Source for field copy cannot be null");
	    Assert.notNull(target, "Destination for field copy cannot be null");
	    final Map<String, Field> sourceMap = getAllNonStaticFields(source.getClass());
        doWithFields(target.getClass(), new FieldCallback() {
            
            public void doWith(Field targetField) {
                Field sourceField = sourceMap.get(targetField.getName());
                if (sourceField != null && 
                        ClassUtils.isAssignableFromWithNum(targetField.getType(), sourceField.getType())) {
                    makeAccessible(sourceField);
                    makeAccessible(targetField);
                    Object value = getField(sourceField, source);
                    if(Number.class.isAssignableFrom(targetField.getType())) {
                        value = convertNumber((Number) value, targetField.getType());
                    }
                    setField(targetField, target, value);
                }
            }
        }, COPYABLE_FIELDS);
	}
	
    public static Map<String, Field> getAllNonStaticFields(Class<?> clazz) {
        Assert.notNull(clazz, "Class must not be null");
        Class<?> searchType = clazz;
        Map<String, Field> map = new HashMap<String, Field>();
        String name = null;
        while (!Object.class.equals(searchType) && searchType != null) {
            Field[] fields = searchType.getDeclaredFields();
            for (Field field : fields) {
                if (!Modifier.isStatic(field.getModifiers())) {
                    name = field.getName();
                    if (map.get(name) == null) {
                        map.put(name, field);
                    }  
                }
            }
            searchType = searchType.getSuperclass();
        }
        return map;
    }
	
	/**
     * Get all fields. Searches all superclasses up to {@link Object}.
     * @param clazz the class to introspect
     * @return the corresponding Field object's Map &lt;Field name, Field object&gt;
     */
    public static Map<String, Field> getAllFields(Class<?> clazz) {
        Assert.notNull(clazz, "Class must not be null");
        Class<?> searchType = clazz;
        Map<String, Field> map = new HashMap<String, Field>();
        String name = null;
        while (!Object.class.equals(searchType) && searchType != null) {
            Field[] fields = searchType.getDeclaredFields();
            for (Field field : fields) {
                name = field.getName();
                if (map.get(name) == null) {
                    map.put(name, field);
                }
            }
            searchType = searchType.getSuperclass();
        }
        return map;
    }
	
    /**
     * Attempt to find a {@link Field field} on the supplied {@link Class} with the
     * supplied <code>name</code>. Searches all superclasses up to {@link Object}.
     * @param clazz the class to introspect
     * @param name the name of the field
     * @return the corresponding Field object, or <code>null</code> if not found
     */
    public static Field findField(Class<?> clazz, String name) {
        return findField(clazz, name, null);
    }

    /**
     * Attempt to find a {@link Field field} on the supplied {@link Class} with the
     * supplied <code>name</code> and/or {@link Class type}. Searches all superclasses
     * up to {@link Object}.
     * @param clazz the class to introspect
     * @param name the name of the field (may be <code>null</code> if type is specified)
     * @param type the type of the field (may be <code>null</code> if name is specified)
     * @return the corresponding Field object, or <code>null</code> if not found
     */
    public static Field findField(Class<?> clazz, String name, Class<?> type) {
        Assert.notNull(clazz, "Class must not be null");
        Assert.isTrue(name != null || type != null, "Either name or type of the field must be specified");
        Class<?> searchType = clazz;
        while (!Object.class.equals(searchType) && searchType != null) {
            Field[] fields = searchType.getDeclaredFields();
            for (Field field : fields) {
                if ((name == null || name.equals(field.getName())) && 
                        (type == null || type.equals(field.getType()))) {
                    return field;
                }
            }
            searchType = searchType.getSuperclass();
        }
        return null;
    }
    
    /**
     * Invoke the given callback on all fields in the target class, going up the
     * class hierarchy to get all declared fields.
     * @param clazz the target class to analyze
     * @param fc the callback to invoke for each field
     */
    public static void doWithFields(Class<?> clazz, FieldCallback fc) {
        doWithFields(clazz, fc, null);
    }
    
    /**
     * Invoke the given callback on all fields in the target class, going up the
     * class hierarchy to get all declared fields.
     * @param clazz the target class to analyze
     * @param fc the callback to invoke for each field
     * @param ff the filter that determines the fields to apply the callback to
     */
    public static void doWithFields(Class<?> clazz, FieldCallback fc, FieldFilter ff) {
        // Keep backing up the inheritance hierarchy.
        Class<?> targetClass = clazz;
        do {
            Field[] fields = targetClass.getDeclaredFields();
            for (Field field : fields) {
                // Skip static and final fields.
                if (ff != null && !ff.matches(field)) {
                    continue;
                }
                fc.doWith(field);
            }
            targetClass = targetClass.getSuperclass();
        }
        while (targetClass != null && targetClass != Object.class);
    }
    
	/**
	 * Callback interface invoked on each field in the hierarchy.
	 */
	public interface FieldCallback {

		/**
		 * Perform an operation using the given field.
		 * @param field the field to operate on
		 */
		void doWith(Field field);
	}


	/**
	 * Callback optionally used to filter fields to be operated on by a field callback.
	 */
	public interface FieldFilter {

		/**
		 * Determine whether the given field matches.
		 * @param field the field to check
		 */
		boolean matches(Field field);
	}


	/**
	 * Pre-built FieldFilter that matches all non-static, non-final fields.
	 */
	public static final FieldFilter COPYABLE_FIELDS = new FieldFilter() {

		public boolean matches(Field field) {
			return !(Modifier.isStatic(field.getModifiers()) || Modifier.isFinal(field.getModifiers()));
		}
	};

	/**
     * 低范围的值 转换成 高范围的类型，比如 Integer的值 转换成 long类型
     * @param value
     * @param classType 可以为Byte，Double，Float，Integer，Long，Short
     */
    static Object convertNumber(Number value, Class<?> classType) {
        if(Double.class.equals(classType)) {
            return value.doubleValue();
        }
        if(Float.class.equals(classType)) {
            return value.floatValue();
        }
        if(Long.class.equals(classType)) {
            return value.longValue();
        }
        if(Integer.class.equals(classType)) {
            return value.intValue();
        }
        if(Short.class.equals(classType)) {
            return value.shortValue();
        }
        if(Byte.class.equals(classType)) {
            return value.byteValue();
        }
        return value;
    }
    
}
