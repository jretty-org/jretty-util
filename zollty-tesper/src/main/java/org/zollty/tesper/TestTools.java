/*
 * Copyright 2012-2013 the original author or authors.
 * 
 * Create by Zollty Tsou (http://blog.zollty.com) on 2013-12-2 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * 
 */
package org.zollty.tesper;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author zollty
 * @since 2013-12-2
 */
public class TestTools {

    public static LogPrint out = new LogPrint() {
        @Override
        public void println(String str) {
            System.out.println(str);
        }

        @Override
        public void print(Throwable e) {
            e.printStackTrace();
        }
    };

    /**
     * 模拟一个很耗时的CPU操作，可以和Thread.slop()相比，但是作用不一样。
     * 
     * @param level (10,100)区间，推荐参考的level： <br>
     *            当level = 26, 53时，对应的耗时大概为：8秒、60秒
     */
    public static void wasteCPUTime(int level) {
        Date begin = new Date();
        String str = "";
        for (int o = 0; o < level; o++) {
            if (o == 26)
                str += o;
            for (int p = 0; p < 36; p++) {
                if (p == 25)
                    str += p;
                for (int q = 0; q < 68; q++) {
                    if (q == 24)
                        str += q;
                    for (int r = 0; r < 100; r++) {
                        if (r == 23)
                            str += r;
                    }
                }
            }
        }
        if ("".equals(str)) {
        }
        Date end = new Date();
        long cost = end.getTime() - begin.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd HH:mm:ss.S");
        out.println("耗时：" + cost + "毫秒(ms) - [开始时间：" + sdf.format(begin) + "，结束时间：" + sdf.format(end) + "]");
    }

    /**
     * 线程休眠
     * 
     * @param sleepTime
     */
    public static void sleepThread(long sleepTime) {
        try {
            out.println(Thread.currentThread().getName() + " Start to sleep " + sleepTime + " MS");
            Thread.sleep(sleepTime);
        }
        catch (InterruptedException e) {
            out.print(e);
        }
    }

    public static void printCNTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
        out.println("[ " + df.format(new Date()) + "]");
    }

    public static void printCNTime(long time) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
        out.println("[ " + df.format(new Date(time)) + "]");
    }

    public static long loopExecute(LoopExecute exe) throws Exception {
        int n = exe.getLoopTimes();
        long t1 = System.currentTimeMillis();
        for (int i = 0; i < n; i++) {
            exe.execute();
        }
        long t2 = System.currentTimeMillis() - t1;
        out.println("[" + exe.getClass().getName() + "] executed cost " + t2 + " ms. ( " + (double) t2 / n + " ms/n)");
        return t2;
    }

}