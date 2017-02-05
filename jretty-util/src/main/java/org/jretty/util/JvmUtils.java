/* 
 * Copyright (C) 2014-2015 the original author or authors.
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

import org.jretty.log.LogFactory;
import org.jretty.log.Logger;

/**
 * JVM 工具类
 * 
 * @author zollty
 * @since 2014-2-15
 */
public class JvmUtils {

    private static final Logger LOG = LogFactory.getLogger(JvmUtils.class);
    
    public static final String JDK_VENDER_IBM = "IBM";
    
    public static final String JDK_VENDER_SUN = "SUN";
    
    public static String getJDKVernder() {
        String verderURL = System.getProperty("java.vendor.url");
        if (verderURL.indexOf("sun") != -1) {
            return JDK_VENDER_SUN;
        }
        else if (verderURL.indexOf("ibm") != -1) {
            return JDK_VENDER_IBM;
        }
        return JDK_VENDER_SUN;
    }

    public static String getOSFileSeparator() {
        return System.getProperty("file.separator");
    }

    public static String getOSPathSeparator() {
        return System.getProperty("path.separator");
    }

    public static void runGC() {
        long begin = System.currentTimeMillis();
        System.gc();
        LOG.warn("gc success! cost(ms) " + (System.currentTimeMillis() - begin));
    }

    public static long getJVMFreeMemory() {
        return Runtime.getRuntime().freeMemory();
    }

    public static long getJVMTotalMemory() {
        return Runtime.getRuntime().totalMemory();
    }

    public static long getJVMMaxMemory() {
        return Runtime.getRuntime().maxMemory();
    }

    public static String getJavaVersion() {
        return System.getProperty("java.version");
    }

    public static String getJavaRuntime() {
        return System.getProperty("java.runtime.version");
    }

    public static interface ShutdownHook {
        void shutdown() throws Throwable;
    }

    public static void registShutdownHook(final String name, final ShutdownHook shutdownHook) {
        LOG.info("regist shutdown hook {}", name);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                try {
                    shutdownHook.shutdown();
                    LOG.info("{} shutdown successed.", name);
                } catch (Throwable t) {
                    LOG.warn(t, "{} shutdown failed, ignore it.", name);
                }
            }
        });
    }

}