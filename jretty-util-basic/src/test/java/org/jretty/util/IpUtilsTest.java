/* 
 * Copyright (C) 2018-2019 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License").
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * Create by ZollTy on 2018-2-23 (http://blog.zollty.com/, zollty@163.com)
 */
package org.jretty.util;

import org.jretty.tesper.LoopExecute;
import org.jretty.tesper.TestTools;

/**
 * @author zollty
 * @since 2018-2-23
 */
public class IpUtilsTest {
    
    public static void main(String[] args) throws Exception {
        loopExecute(100); // 预热CPU

        loopExecute(10000); // 正式测试
    }
    
    // 循环执行
    static void loopExecute(final int times) throws Exception {
        TestTools.loopExecute(new LoopExecute() {

            @Override
            public int getLoopTimes() {

                return times;
            }

            @Override
            public void execute() throws Exception {

                IpUtils.getLocalIP0();
            }
        });
        System.out.println("-------------------");
        TestTools.loopExecute(new LoopExecute() {

            @Override
            public int getLoopTimes() {

                return times;
            }

            @Override
            public void execute() throws Exception {

                IpUtils.getLocalIP();
            }
        });
    }

}
