/* 
 * Copyright (C) 2014-2019 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License").
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * Create by ZollTy on 2014-2-15 (http://blog.zollty.com/, zollty@163.com)
 */
package org.jretty.util;

/**
 * 
 * @author zollty
 * @since 2014-2-15
 */
public class JvmUtilsTest {

    public static void main(String[] args) {
        System.out.println(JvmUtils.getJDKVernder());
        System.out.println(JvmUtils.getJavaVersion());
        System.out.println(JvmUtils.getJavaRuntime());
        System.out.println(JvmUtils.getJVMTotalMemory()/1024/1024 + "Mb");
        System.out.println(JvmUtils.getJVMMaxMemory()/1024/1024 + "Mb");
        System.out.println(JvmUtils.getJVMFreeMemory()/1024/1024 + "Mb");
    }

}
