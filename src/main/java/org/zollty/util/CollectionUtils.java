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

import java.util.Arrays;
import java.util.List;

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
    
    
    //==============================================================
    //===============Change Array Element Type
    
    public static Integer[] changeType(int[] array) {
        if (array == null) {
            return null;
        }
        Integer[] arr = new Integer[array.length];
        for (int i = 0; i < array.length; i++) {
            arr[i] = array[i];
        }
        return arr;
    }
    
    public static int[] changeType(Integer[] array) {
        if (array == null) {
            return null;
        }
        int[] arr = new int[array.length];
        for (int i = 0; i < array.length; i++) {
            arr[i] = array[i];
        }
        return arr;
    }
    
    public static Long[] changeType(long[] array) {
        if (array == null) {
            return null;
        }
        Long[] arr = new Long[array.length];
        for (int i = 0; i < array.length; i++) {
            arr[i] = array[i];
        }
        return arr;
    }
    
    public static long[] changeType(Long[] array) {
        if (array == null) {
            return null;
        }
        long[] arr = new long[array.length];
        for (int i = 0; i < array.length; i++) {
            arr[i] = array[i];
        }
        return arr;
    }
    
    public static Double[] changeType(double[] array) {
        if (array == null) {
            return null;
        }
        Double[] arr = new Double[array.length];
        for (int i = 0; i < array.length; i++) {
            arr[i] = array[i];
        }
        return arr;
    }
    
    public static double[] changeType(Double[] array) {
        if (array == null) {
            return null;
        }
        double[] arr = new double[array.length];
        for (int i = 0; i < array.length; i++) {
            arr[i] = array[i];
        }
        return arr;
    }
    
    public static Byte[] changeType(byte[] array) {
        if (array == null) {
            return null;
        }
        Byte[] arr = new Byte[array.length];
        for (int i = 0; i < array.length; i++) {
            arr[i] = array[i];
        }
        return arr;
    }
    
    public static byte[] changeType(Byte[] array) {
        if (array == null) {
            return null;
        }
        byte[] arr = new byte[array.length];
        for (int i = 0; i < array.length; i++) {
            arr[i] = array[i];
        }
        return arr;
    }
    
    public static Boolean[] changeType(boolean[] array) {
        if (array == null) {
            return null;
        }
        Boolean[] arr = new Boolean[array.length];
        for (int i = 0; i < array.length; i++) {
            arr[i] = array[i];
        }
        return arr;
    }
    
    public static boolean[] changeType(Boolean[] array) {
        if (array == null) {
            return null;
        }
        boolean[] arr = new boolean[array.length];
        for (int i = 0; i < array.length; i++) {
            arr[i] = array[i];
        }
        return arr;
    }
    
    
    ///////////////////////////////////////////////////////////////////////////
    ///////////////Check Array Element Duplication
    
    /**
     * Check Array Element Duplication. <p>
     * If it's main type element array, use like this <p>
     * <code> checkDuplication( changeType(array) ) </code>
     * @return return -1 if no duplication
     */
    public static int checkDuplication(Object[] array) {
        return checkDuplication(Arrays.asList(array));
    }
    
    /**
     * Check List Element Duplication
     * @return return -1 if no duplication
     */
    public static int checkDuplication(List<?> array) {
        int size = array.size();
        if(array!=null && size>0)
        {
            for(int i=0;i<size;i++){
                for(int j=i+1;j<size;j++){
                    if( ObjectUtils.safeEqual(array.get(j), array.get(i)) ) {
                        return i;
                    }
                }
            }
        }
        return -1;
    }

}
