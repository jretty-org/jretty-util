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
package org.zollty.tool;

import java.security.NoSuchAlgorithmException;

import org.zollty.util.AlgorithmUtils;

/**
 * A Powerful Algorithmic Toolkit.
 * 
 * @author zollty 
 * @since 2013-10-27
 * @deprecated use AlgorithmUtils instead
 */
public class AlgorithmTools {
	
	
    /**
     * Use MD5 algorithm to encode a string 
     * 
     * @param str String to encode
     * @return Encoded String
     * @throws NoSuchAlgorithmException
     */
    public static String md5Crypt(String str) {
        return AlgorithmUtils.md5Crypt(str);
    }
    
    public static String toHexStr(byte[] data) {
        return AlgorithmUtils.toHexStr(data);
    }

	/**
	 * Short URL Algorithm, 
	 *     e.g. http://url.cn/WgGTqt, 
	 *     short string "WgGTqt" can use this method to generate.
	 */
    public static String shortMsg(String msg) {
        return AlgorithmUtils.shortMsg(msg);
    }

}
