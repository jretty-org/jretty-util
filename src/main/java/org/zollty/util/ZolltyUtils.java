/*
 * @(#)ZolltyUtils.java
 * Copyright (C) 2013-2014 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License").
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * Create by zollty on 2013-6-7 [http://blog.csdn.net/zollty (or GitHub)]
 */
package org.zollty.util;


/**
 * @author zollty
 * @since 2013-6-7
 */
public class ZolltyUtils {
    
    public static class ArrayUtil {
        /**
         * 模拟list.add()方法，String数组也可以用 addStringToArray(array, "ABCD"); Append the given String to the given String
         * array, returning a new array consisting of the input array contents plus the given String.
         * @param array  the array to append to (can be <code>null</code>)
         * @param str the String to append
         * @return the new array (never <code>null</code>)
         */
        public static String[] addStr(String[] array, String str) {
            if (null == array || array.length == 0) {
                return new String[] { str };
            }
            String[] newArr = new String[array.length + 1];
            System.arraycopy(array, 0, newArr, 0, array.length);
            newArr[array.length] = str;
            return newArr;
        }

        public static int[] addInt(int[] array, int integer) {
            if (null == array || array.length == 0) {
                return new int[] { integer };
            }
            int[] newArr = new int[array.length + 1];
            System.arraycopy(array, 0, newArr, 0, array.length);
            newArr[array.length] = integer;
            return newArr;
        }
        
        public static char[] addChar(char[] array, char cchar) {
            if (null == array || array.length == 0) {
                return new char[] { cchar };
            }
            char[] newArr = new char[array.length + 1];
            System.arraycopy(array, 0, newArr, 0, array.length);
            newArr[array.length] = cchar;
            return newArr;
        }
        
//        public static String listToStr(List<?> list) {
//            return listToStr(list, ", ");
//        }
//        /**
//         * 将list转换成字符串，例如 listToStr(list, ", ")
//         * @param list 待转换的list
//         * @param split 分隔符
//         * @return 以split分割的字符串
//         */
//        public static String listToStr(List<?> list, String split) {
//            if (list == null){
//                return "";
//            }
//            StringBuilder sb = new StringBuilder();
//            int size = list.size();
//            for (int i = 0; i < size; i++) {
//                if (i != 0) {
//                    sb.append(split).append(list.get(i));
//                }
//                else {
//                    sb.append(list.get(i));
//                }
//            }
//            return sb.toString();
//        }
        
    }

}
