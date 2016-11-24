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
 * Create by ZollTy on 2013-6-27 (http://blog.zollty.com/, zollty@163.com)
 */
package org.jretty.util;

import java.util.Collection;
import java.util.Map;

/**
 * Assertion utility class that assists in validating arguments.
 * Useful for identifying programmer errors early and clearly at runtime.
 *
 * <p>For example, if the contract of a public method states it does not
 * allow <code>null</code> arguments, Assert can be used to validate that
 * contract. Doing this clearly indicates a contract violation when it
 * occurs and protects the class's invariants.
 *
 * <p>Typically used to validate method arguments rather than configuration
 * properties, to check for cases that are usually programmer errors rather than
 * configuration errors. In contrast to config initialization code, there is
 * usally no point in falling back to defaults in such methods.
 *
 * <p>This class is similar to JUnit's assertion library. If an argument value is
 * deemed invalid, an {@link IllegalArgumentException} is thrown (typically).
 * For example:
 *
 * <pre class="code">
 * Assert.notNull(clazz, "The class must not be null");
 * Assert.isTrue(i > 0, "The value must be greater than zero");</pre>
 *
 * Mainly for internal use within the framework; consider Jakarta's Commons Lang
 * >= 2.0 for a more comprehensive suite of assertion utilities.
 *
 * @author Keith Donald
 * @author Juergen Hoeller
 * @author Colin Sampaleanu
 * @author Rob Harrop
 * @auther Zollty Tsou
 * 
 * @since 2013-6-27
 */
public abstract class Assert {
    
    /**
     * 改造堆栈信息，remove第一个堆栈，即Assert内部的堆栈，便于外部调用程序直接定位到自己的调用出处。
     * 例如，改造后堆栈信息为：
     * <pre>
     * java.lang.IllegalArgumentException: [Assertion failed] - this argument is required; it must not be null
        at org.zollty.util.AssertTest.doService(AssertTest.java:25)
        
     * </pre>
     * 改造前堆栈信息为：
     * <pre>
     * java.lang.IllegalArgumentException: [Assertion failed] - this argument is required; it must not be null
        at org.zollty.util.AssertTest.notNull(AssertTest.java:123)
        at org.zollty.util.AssertTest.notNull(AssertTest.java:110)
        at org.zollty.util.AssertTest.hasText(AssertTest.java:91)
        at org.zollty.util.AssertTest.hasLength(AssertTest.java:51)
        at org.zollty.util.AssertTest.doService(AssertTest.java:25)
        
     * </pre>
     */
    private static RuntimeException changeIAE(RuntimeException e){
        StackTraceElement[] st = e.getStackTrace();
        st = ArrayUtils.remove(st, 0);
        e.setStackTrace(st);
        return e;
    }

	/**
	 * Assert a boolean expression, throwing <code>IllegalArgumentException</code>
	 * if the test result is <code>false</code>.
	 * <pre class="code">Assert.isTrue(i &gt; 0, "The value must be greater than zero");</pre>
	 * @param expression a boolean expression
	 * @param message the exception message to use if the assertion fails
	 * @throws IllegalArgumentException if expression is <code>false</code>
	 */
	public static void isTrue(boolean expression, String message) {
		if (!expression) {
		    throw changeIAE(new IllegalArgumentException(message));
		}
	}

	/**
	 * Assert a boolean expression, throwing <code>IllegalArgumentException</code>
	 * if the test result is <code>false</code>.
	 * <pre class="code">Assert.isTrue(i &gt; 0);</pre>
	 * @param expression a boolean expression
	 * @throws IllegalArgumentException if expression is <code>false</code>
	 */
	public static void isTrue(boolean expression) {
		if (!expression) {
            throw changeIAE(new IllegalArgumentException("[Assertion failed] - this expression must be true"));
        }
	}

	/**
	 * Assert that an object is <code>null</code> .
	 * <pre class="code">Assert.isNull(value, "The value must be null");</pre>
	 * @param object the object to check
	 * @param message the exception message to use if the assertion fails
	 * @throws IllegalArgumentException if the object is not <code>null</code>
	 */
	public static void isNull(Object object, String message) {
		if (object != null) {
		    throw changeIAE(new IllegalArgumentException(message));
		}
	}

	/**
	 * Assert that an object is <code>null</code> .
	 * <pre class="code">Assert.isNull(value);</pre>
	 * @param object the object to check
	 * @throws IllegalArgumentException if the object is not <code>null</code>
	 */
	public static void isNull(Object object) {
		if (object != null) {
            throw changeIAE(new IllegalArgumentException("[Assertion failed] - the object argument must be null"));
        }
	}

	/**
	 * Assert that an object is not <code>null</code> .
	 * <pre class="code">Assert.notNull(clazz, "The class must not be null");</pre>
	 * @param object the object to check
	 * @param message the exception message to use if the assertion fails
	 * @throws IllegalArgumentException if the object is <code>null</code>
	 */
	public static void notNull(Object object, String message) {
		if (object == null) {
		    throw changeIAE(new IllegalArgumentException(message));
		}
	}

	/**
	 * Assert that an object is not <code>null</code> .
	 * <pre class="code">Assert.notNull(clazz);</pre>
	 * @param object the object to check
	 * @throws IllegalArgumentException if the object is <code>null</code>
	 */
	public static void notNull(Object object) {
		if (object == null) {
            throw changeIAE(new IllegalArgumentException("[Assertion failed] - this argument is required; it must not be null"));
        }
	}

	/**
	 * Assert that the given String is not empty; that is,
	 * it must not be <code>null</code> and not the empty String.
	 * <pre class="code">Assert.hasLength(name, "Name must not be empty");</pre>
	 * @param text the String to check
	 * @param message the exception message to use if the assertion fails
	 * @see StringUtils#hasLength
	 */
	public static void hasLength(String text, String message) {
		if (StringUtils.isNullOrEmpty(text)) {
			throw changeIAE(new IllegalArgumentException(message));
		}
	}

	/**
	 * Assert that the given String is not empty; that is,
	 * it must not be <code>null</code> and not the empty String.
	 * <pre class="code">Assert.hasLength(name);</pre>
	 * @param text the String to check
	 * @see StringUtils#hasLength
	 */
	public static void hasLength(String text) {
		if (StringUtils.isNullOrEmpty(text)) {
            throw changeIAE(new IllegalArgumentException(
                    "[Assertion failed] - this String argument must have length; it must not be null or empty"));
        }
	}

	/**
	 * Assert that the given String has valid text content; that is, it must not
	 * be <code>null</code> and must contain at least one non-whitespace character.
	 * <pre class="code">Assert.hasText(name, "'name' must not be empty");</pre>
	 * @param text the String to check
	 * @param message the exception message to use if the assertion fails
	 * @see StringUtils#hasText
	 */
	public static void hasText(String text, String message) {
		if (StringUtils.isBlank(text)) {
			throw changeIAE(new IllegalArgumentException(message));
		}
	}

	/**
	 * Assert that the given String has valid text content; that is, it must not
	 * be <code>null</code> and must contain at least one non-whitespace character.
	 * <pre class="code">Assert.hasText(name, "'name' must not be empty");</pre>
	 * @param text the String to check
	 * @see StringUtils#hasText
	 */
	public static void hasText(String text) {
	    if (StringUtils.isBlank(text)) {
            throw changeIAE(new IllegalArgumentException(
                    "[Assertion failed] - this String argument must have text; it must not be null, empty, or blank"));
        }
	}

	/**
	 * Assert that the given text does not contain the given substring.
	 * <pre class="code">Assert.doesNotContain(name, "rod", "Name must not contain 'rod'");</pre>
	 * @param textToSearch the text to search
	 * @param substring the substring to find within the text
	 * @param message the exception message to use if the assertion fails
	 */
	public static void doesNotContain(String textToSearch, String substring, String message) {
		if (StringUtils.isNotEmpty(textToSearch) && StringUtils.isNotEmpty(substring) &&
				textToSearch.indexOf(substring) != -1) {
			throw changeIAE(new IllegalArgumentException(message));
		}
	}

	/**
	 * Assert that the given text does not contain the given substring.
	 * <pre class="code">Assert.doesNotContain(name, "rod");</pre>
	 * @param textToSearch the text to search
	 * @param substring the substring to find within the text
	 */
	public static void doesNotContain(String textToSearch, String substring) {
	    if (StringUtils.isNotEmpty(textToSearch) && StringUtils.isNotEmpty(substring) &&
                textToSearch.indexOf(substring) != -1) {
            throw changeIAE(new IllegalArgumentException(
                    "[Assertion failed] - this String argument must not contain the substring [" + substring + "]"));
        }
	}


	/**
	 * Assert that an array has elements; that is, it must not be
	 * <code>null</code> and must have at least one element.
	 * <pre class="code">Assert.notEmpty(array, "The array must have elements");</pre>
	 * @param array the array to check
	 * @param message the exception message to use if the assertion fails
	 * @throws IllegalArgumentException if the object array is <code>null</code> or has no elements
	 */
	public static void notEmpty(Object[] array, String message) {
		if (array==null || array.length==0) {
			throw changeIAE(new IllegalArgumentException(message));
		}
	}

	/**
	 * Assert that an array has elements; that is, it must not be
	 * <code>null</code> and must have at least one element.
	 * <pre class="code">Assert.notEmpty(array);</pre>
	 * @param array the array to check
	 * @throws IllegalArgumentException if the object array is <code>null</code> or has no elements
	 */
	public static void notEmpty(Object[] array) {
	    if (array==null || array.length==0) {
            throw changeIAE(new IllegalArgumentException(
                    "[Assertion failed] - this array must not be empty: it must contain at least 1 element"));
        }
	}

	/**
	 * Assert that an array has no null elements.
	 * Note: Does not complain if the array is empty!
	 * <pre class="code">Assert.noNullElements(array, "The array must have non-null elements");</pre>
	 * @param array the array to check
	 * @param message the exception message to use if the assertion fails
	 * @throws IllegalArgumentException if the object array contains a <code>null</code> element
	 */
	public static void noNullElements(Object[] array, String message) {
		if (array != null) {
			for (int i = 0; i < array.length; i++) {
				if (array[i] == null) {
					throw changeIAE(new IllegalArgumentException(message));
				}
			}
		}
	}

	/**
	 * Assert that an array has no null elements.
	 * Note: Does not complain if the array is empty!
	 * <pre class="code">Assert.noNullElements(array);</pre>
	 * @param array the array to check
	 * @throws IllegalArgumentException if the object array contains a <code>null</code> element
	 */
	public static void noNullElements(Object[] array) {
	    if (array != null) {
            for (int i = 0; i < array.length; i++) {
                if (array[i] == null) {
                    throw changeIAE(new IllegalArgumentException(
                            "[Assertion failed] - this array must not contain any null elements"));
                }
            }
        }
	}

	/**
	 * Assert that a collection has elements; that is, it must not be
	 * <code>null</code> and must have at least one element.
	 * <pre class="code">Assert.notEmpty(collection, "Collection must have elements");</pre>
	 * @param collection the collection to check
	 * @param message the exception message to use if the assertion fails
	 * @throws IllegalArgumentException if the collection is <code>null</code> or has no elements
	 */
	@SuppressWarnings("rawtypes")
    public static void notEmpty(Collection collection, String message) {
		if (collection==null || collection.isEmpty()) {
			throw changeIAE(new IllegalArgumentException(message));
		}
	}

	/**
	 * Assert that a collection has elements; that is, it must not be
	 * <code>null</code> and must have at least one element.
	 * <pre class="code">Assert.notEmpty(collection, "Collection must have elements");</pre>
	 * @param collection the collection to check
	 * @throws IllegalArgumentException if the collection is <code>null</code> or has no elements
	 */
	@SuppressWarnings("rawtypes")
	public static void notEmpty(Collection collection) {
	    if (collection==null || collection.isEmpty()) {
            throw changeIAE(new IllegalArgumentException(
                    "[Assertion failed] - this collection must not be empty: it must contain at least 1 element"));
        }
	}

	/**
	 * Assert that a Map has entries; that is, it must not be <code>null</code>
	 * and must have at least one entry.
	 * <pre class="code">Assert.notEmpty(map, "Map must have entries");</pre>
	 * @param map the map to check
	 * @param message the exception message to use if the assertion fails
	 * @throws IllegalArgumentException if the map is <code>null</code> or has no entries
	 */
	@SuppressWarnings("rawtypes")
	public static void notEmpty(Map map, String message) {
		if (map==null || map.isEmpty()) {
			throw changeIAE(new IllegalArgumentException(message));
		}
	}

	/**
	 * Assert that a Map has entries; that is, it must not be <code>null</code>
	 * and must have at least one entry.
	 * <pre class="code">Assert.notEmpty(map);</pre>
	 * @param map the map to check
	 * @throws IllegalArgumentException if the map is <code>null</code> or has no entries
	 */
	@SuppressWarnings("rawtypes")
	public static void notEmpty(Map map) {
	    if (map==null || map.isEmpty()) {
            throw changeIAE(new IllegalArgumentException(
                    "[Assertion failed] - this map must not be empty; it must contain at least one entry"));
        }
	}


	/**
	 * Assert that the provided object is an instance of the provided class.
	 * <pre class="code">Assert.instanceOf(Foo.class, foo);</pre>
	 * @param clazz the required class
	 * @param obj the object to check
	 * @throws IllegalArgumentException if the object is not an instance of clazz
	 * @see Class#isInstance
	 */
	@SuppressWarnings("rawtypes")
	public static void isInstanceOf(Class type, Object obj) {
		if (type == null) {
            throw changeIAE(new IllegalArgumentException("Type to check against must not be null"));
        }
        if (!type.isInstance(obj)) {
            throw new IllegalArgumentException(
                    "Object of class [" + (obj != null ? obj.getClass().getName() : "null") +
                    "] must be an instance of " + type);
        }
	}

	/**
	 * Assert that the provided object is an instance of the provided class.
	 * <pre class="code">Assert.instanceOf(Foo.class, foo);</pre>
	 * @param type the type to check against
	 * @param obj the object to check
	 * @param message a message which will be prepended to the message produced by
	 * the function itself, and which may be used to provide context. It should
	 * normally end in a ": " or ". " so that the function generate message looks
	 * ok when prepended to it.
	 * @throws IllegalArgumentException if the object is not an instance of clazz
	 * @see Class#isInstance
	 */
	@SuppressWarnings("rawtypes")
	public static void isInstanceOf(Class type, Object obj, String message) {
		if (type == null) {
            throw changeIAE(new IllegalArgumentException("Type to check against must not be null"));
        }
		if (!type.isInstance(obj)) {
			throw new IllegalArgumentException(message +
					"Object of class [" + (obj != null ? obj.getClass().getName() : "null") +
					"] must be an instance of " + type);
		}
	}

	/**
	 * Assert that <code>superType.isAssignableFrom(subType)</code> is <code>true</code>.
	 * <pre class="code">Assert.isAssignable(Number.class, myClass);</pre>
	 * @param superType the super type to check
	 * @param subType the sub type to check
	 * @throws IllegalArgumentException if the classes are not assignable
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void isAssignable(Class superType, Class subType) {
		if (superType == null) {
            throw changeIAE(new IllegalArgumentException("Type to check against must not be null"));
        }
        if (subType == null || !superType.isAssignableFrom(subType)) {
            throw new IllegalArgumentException(subType + " is not assignable to " + superType);
        }
	}

	/**
	 * Assert that <code>superType.isAssignableFrom(subType)</code> is <code>true</code>.
	 * <pre class="code">Assert.isAssignable(Number.class, myClass);</pre>
	 * @param superType the super type to check against
	 * @param subType the sub type to check
	 * @param message a message which will be prepended to the message produced by
	 * the function itself, and which may be used to provide context. It should
	 * normally end in a ": " or ". " so that the function generate message looks
	 * ok when prepended to it.
	 * @throws IllegalArgumentException if the classes are not assignable
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void isAssignable(Class superType, Class subType, String message) {
		if (superType == null) {
            throw changeIAE(new IllegalArgumentException("Type to check against must not be null"));
        }
		if (subType == null || !superType.isAssignableFrom(subType)) {
			throw new IllegalArgumentException(message + subType + " is not assignable to " + superType);
		}
	}


	/**
	 * Assert a boolean expression, throwing <code>IllegalStateException</code>
	 * if the test result is <code>false</code>. Call isTrue if you wish to
	 * throw IllegalArgumentException on an assertion failure.
	 * <pre class="code">Assert.state(id == null, "The id property must not already be initialized");</pre>
	 * @param expression a boolean expression
	 * @param message the exception message to use if the assertion fails
	 * @throws IllegalStateException if expression is <code>false</code>
	 */
	public static void state(boolean expression, String message) {
		if (!expression) {
		    throw changeIAE(new IllegalStateException(message));
		}
	}

	/**
	 * Assert a boolean expression, throwing {@link IllegalStateException}
	 * if the test result is <code>false</code>.
	 * <p>Call {@link #isTrue(boolean)} if you wish to
	 * throw {@link IllegalArgumentException} on an assertion failure.
	 * <pre class="code">Assert.state(id == null);</pre>
	 * @param expression a boolean expression
	 * @throws IllegalStateException if the supplied expression is <code>false</code>
	 */
	public static void state(boolean expression) {
		if (!expression) {
            throw changeIAE(new IllegalStateException(
                    "[Assertion failed] - this state invariant must be true"));
        }
	}

}
