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
 * Create by ZollTy on 2013-10-27 (http://blog.zollty.com/, zollty@163.com)
 */
package org.jretty.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * A Powerful Algorithmic Toolkit.
 * 
 * @author zollty 
 * @since 2013-10-27
 */
public class AlgorithmUtils {
    
    /**
     * Use MD5 algorithm to encode a string 
     * 
     * @param str String to encode
     * @return Encoded String
     */
    public static String md5Crypt(String str) {
        if (StringUtils.isNullOrEmpty(str)) {
            throw new IllegalArgumentException("String to encrypt cannot be null or zero length");
        }
        try {
            MessageDigest msgdig = MessageDigest.getInstance("MD5");
            msgdig.update(str.getBytes("UTF-8"));
            byte[] hash = msgdig.digest();
            
            return toHexStr(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new BasicRuntimeException(e.toString());
        }
        catch (UnsupportedEncodingException e) {
            throw new BasicRuntimeException(e.toString());
        }
    }
    
    public static String toHexStr(byte[] data) {
        if (data == null) {
            return null;
        }
        if (data.length == 0) {
            return "";
        }
        StringBuilder hexString = new StringBuilder(33);
        String stmp;
        for (int i = 0; i < data.length; i++) {
            stmp = Integer.toHexString(0xFF & data[i]);
            if (stmp.length() == 1)
                hexString.append('0').append(stmp);
            else {
                hexString.append(stmp);
            }
        }
        return hexString.toString();
    }

    private static final String[] CHARS = new String[] { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l",
            "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5", "6",
            "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R",
            "S", "T", "U", "V", "W", "X", "Y", "Z" };
    
    /**
     * Short URL Algorithm, 
     *     e.g. http://url.cn/WgGTqt, 
     *     short string "WgGTqt" can use this method to generate.
     */
    public static String shortMsg(String msg) {
        // MD5 加密
        String hex = AlgorithmUtils.md5Crypt(msg);

        // 把加密字符按照 8 位一组 16 进制与 0x3FFFFFFF 进行位与运算
        String tempStr = hex.substring(0, 8);
        // 这里需要使用 long 型来转换，因为 Inteper .parseInt() 只能处理 31 位 , 首位为符号位 , 如果不用
        // long ，则会越界
        // 与0x3FFFFFFF位与运算的目的是获得
        long lHexLong = 0x0FFFFFFF & Long.parseLong(tempStr, 16);
        StringBuilder sbu = new StringBuilder(7);
        for (int j = 0; j < 6; j++) {
            // 把得到的值与 0x0000003D (61)进行位与运算，取得字符数组 chars 索引
            long index = 0x0000003D & lHexLong;
            // 把取得的字符相加
            sbu.append(CHARS[(int) index]);
            // 每次循环按位右移 5 位
            lHexLong = lHexLong >> 5;
        }
        // 把字符串存入对应索引的输出数组
        return sbu.toString();
    }

}
