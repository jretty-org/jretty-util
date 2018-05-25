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

/**
 * 可定义名字的ThreadFactory
 * 
 * @author zollty
 * @since 2016-9-03
 */
public class NamedThreadFactory implements ThreadFactory {

    private String baseName;
    private boolean addTimestamps;

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
            name = addTimestamps
                    ? ((NamedRunnable) r).getName() + "-" + DateFormatUtils.getUniqueDatePattern_TimeMillis_noSplit()
                    : ((NamedRunnable) r).getName();
        } else {
            name = baseName + "$" + DateFormatUtils.getUniqueDatePattern_TimeMillis_noSplit();
        }

        return new Thread(r, name);
    }

    public String getBaseName() {
        return baseName;
    }

    public void setBaseName(String baseName) {
        this.baseName = baseName;
    }

}