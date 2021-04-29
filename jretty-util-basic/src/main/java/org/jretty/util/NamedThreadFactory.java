/* 
 * Copyright (C) 2016-2018 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License").
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * Create by ZollTy on 2016-9-03 (http://blog.zollty.com/, zollty@163.com)
 */
package org.jretty.util;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 可定义名字的ThreadFactory
 * 
 * @author zollty
 * @since 2016-9-03
 */
public class NamedThreadFactory implements ThreadFactory {

    private String baseName;
    private boolean addTimestamps;
    private final AtomicInteger poolNumber = new AtomicInteger(1);
    
    /** Stores the uncaught exception handler. */
    private Thread.UncaughtExceptionHandler uncaughtExceptionHandler;

    /** Stores the priority. */
    private Integer priority;

    /** Stores the daemon status flag. */
    private Boolean daemonFlag;

    public NamedThreadFactory(String baseName) {
        super();
        this.baseName = baseName;
        this.addTimestamps = true;
    }

    public NamedThreadFactory(String baseName, boolean addTimestamps) {
        super();
        this.baseName = baseName;
        this.addTimestamps = addTimestamps;
    }

    @Override
    public Thread newThread(Runnable r) {
        String name;
        if (r instanceof NamedRunnable) {
            name = ((NamedRunnable) r).getName();
        } else {
            name = baseName;
        }
        name = name + "-" + poolNumber.getAndIncrement();
        if(addTimestamps) {
            name = name + "$" + DateFormatUtils.getUniqueDatePatternTimeMillisNoSplit();
        }
        
        Thread t = new Thread(r, name);
        
        if (uncaughtExceptionHandler != null) {
            t.setUncaughtExceptionHandler(uncaughtExceptionHandler);
        }

        if (priority != null) {
            t.setPriority(priority.intValue());
        }

        if (daemonFlag != null) {
            t.setDaemon(daemonFlag.booleanValue());
        }

        return t;
    }

    public String getBaseName() {
        return baseName;
    }

    public void setBaseName(String baseName) {
        this.baseName = baseName;
    }

    /**
     * @param addTimestamps the addTimestamps to set
     */
    public void setAddTimestamps(boolean addTimestamps) {
        this.addTimestamps = addTimestamps;
    }

    /**
     * @param uncaughtExceptionHandler the uncaughtExceptionHandler to set
     */
    public void setUncaughtExceptionHandler(
            Thread.UncaughtExceptionHandler uncaughtExceptionHandler) {
        this.uncaughtExceptionHandler = uncaughtExceptionHandler;
    }

    /**
     * @param priority the priority to set
     */
    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    /**
     * @param daemonFlag the daemonFlag to set
     */
    public void setDaemonFlag(Boolean daemonFlag) {
        this.daemonFlag = daemonFlag;
    }

}