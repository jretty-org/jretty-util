/* 
 * Copyright (C) 2013-2014 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License").
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * Create by Zollty Tsou (Contact: zollty@163.com, http://blog.zollty.com)
 */
package org.zollty.util;

/**
 * 集合工具类
 * 
 * @author zollty
 * @since 2014-06-22
 */
public class CollectionUtils {
    
    /**
     * 模拟list.add()方法，Append the given String to the given String
     * array, returning a new array consisting of the input array contents plus the given String.
     * 
     * @param array  the array to append to (can be <code>null</code>)
     * @param str the String to append
     * @return the new array (never <code>null</code>)
     */
    public static String[] arrayAdd(String[] array, String str) {
        if (null == array || array.length == 0) {
            return new String[] { str };
        }
        String[] newArr = new String[array.length + 1];
        System.arraycopy(array, 0, newArr, 0, array.length);
        newArr[array.length] = str;
        return newArr;
    }

    public static int[] arrayAdd(int[] array, int integer) {
        if (null == array || array.length == 0) {
            return new int[] { integer };
        }
        int[] newArr = new int[array.length + 1];
        System.arraycopy(array, 0, newArr, 0, array.length);
        newArr[array.length] = integer;
        return newArr;
    }
    
    public static char[] arrayAdd(char[] array, char cchar) {
        if (null == array || array.length == 0) {
            return new char[] { cchar };
        }
        char[] newArr = new char[array.length + 1];
        System.arraycopy(array, 0, newArr, 0, array.length);
        newArr[array.length] = cchar;
        return newArr;
    }

}
