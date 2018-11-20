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
 * Create by ZollTy on 2013-6-07 (http://blog.zollty.com/, zollty@163.com)
 */
package org.jretty.util;

import java.util.Random;

/**
 * 线程安全的随机数生成工具
 * 
 * @author zollty 高效的算法保证
 * @since 2013-6-07
 */
public class RandomUtils {

    /**
     * 获取 (0,1) 范围的随机实数，例如 0.019049038474404645, 0.9528710628981955
     */
    public static double nextDouble() {
        if (randomNumberGenerator == null) {
            initRNG();
        }
        return randomNumberGenerator.nextDouble();
    }

    /**
     * 获取 [0,n-1] 范围的随机数
     */
    public static int nextInt(int n) {
        if (randomNumberGenerator == null) {
            initRNG();
        }
        return randomNumberGenerator.nextInt(n);
    }

    /**
     * Generate radom number string 生成随机数字符串 <br>
     * the param (start, end) must in region [0,9] or ['A','Z'] or ['a', 'z'] <br>
     * e.g. (2,8) or ('A','K')
     * 
     * @param len
     *            [the target string's length]
     */
    public static final String getRadomStr(int start, int end, int len) {
        if (start > -1 && end < 10 && start < end) {
            StringBuilder bstr = new StringBuilder(len);
            int num = end + 1 - start;
            for (int i = 0; i < len; i++) {
                bstr.append(nextInt(num) + start);
            }
            return bstr.toString();
        }

        if (start > 64 && end < 91 && start < end) {
            StringBuilder bstr = new StringBuilder(len);
            int num = end + 1 - start;
            int n;
            for (int i = 0; i < len; i++) {
                n = nextInt(num) + start;
                bstr.append((char) n);
            }
            return bstr.toString();
        }

        if (start > 96 && end < 123 && start < end) {
            StringBuilder bstr = new StringBuilder(len);
            int num = end + 1 - start;
            int n;
            for (int i = 0; i < len; i++) {
                n = nextInt(num) + start;
                bstr.append((char) n);
            }
            return bstr.toString();
        }

        throw new IllegalArgumentException("start=" + start + ", end=" + end
                + ". Arguments not correct, should in region [0,9] or ['A','Z'] or ['a', 'z']");
    }

    /**
     * 获取长度为len的，范围为 'A'到'Z'的随机字母字符串
     */
    public static final String getRadomStrAZ(int len) {
        return getRadomStr('A', 'Z', len);
    }

    public static final String getRadomStr09(int len) {
        StringBuilder bstr = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            bstr.append(nextInt(10));
        }
        return bstr.toString();
    }

    public static final String getRadomStr09AZ(int len) {
        StringBuilder bstr = new StringBuilder(len);
        int n;
        for (int i = 0; i < len; i++) {
            n = 48 + nextInt(36);
            if (n >= 58 && n <= 64) {
                n += 26;
            }
            bstr.append((char) n);
        }
        return bstr.toString();
    }

    public static final String getRadomStr09az(int len) {
        StringBuilder bstr = new StringBuilder(len);
        int n;
        for (int i = 0; i < len; i++) {
            n = 48 + nextInt(36);
            if (n <= 57) {
                bstr.append((char) n);
            } else {
                if (n >= 58 && n <= 64) {
                    n += 26;
                }
                n += 32;
                bstr.append((char) n);
            }
        }
        return bstr.toString();
    }

    /**
     * 获取长度为{len}的随机字符串（A_Za_z组成）
     * 
     * @param len
     *            随机字符串的长度
     */
    public static final String getRadomStrAZaz(int len) {
        StringBuilder bstr = new StringBuilder(len);
        int n;
        for (int i = 0; i < len; i++) {
            n = 65 + nextInt(52); // 65-116（122-5）
            if (n >= 91 && n <= 96) { // 将91-96映射成117-122
                n += 26;
            }
            bstr.append((char) n);
        }
        return bstr.toString();
    }

    /**
     * 获取长度为{len}的随机字符串（0_9A_Za_z组成）
     * 
     * @param len
     *            随机字符串的长度
     */
    public static final String getRadomStr09AZaz(int len) {
        StringBuilder bstr = new StringBuilder(len);
        int n;
        for (int i = 0; i < len; i++) {
            n = 48 + nextInt(62);
            if (n >= 58 && n <= 64) {
                n += 58;
            } else if (n >= 91 && n <= 96) {
                n += 19;
            }
            bstr.append((char) n);
        }
        return bstr.toString();
    }

    private static Random randomNumberGenerator;

    private static synchronized void initRNG() {
        if (randomNumberGenerator == null) {
            randomNumberGenerator = new Random();
        }
    }

}
