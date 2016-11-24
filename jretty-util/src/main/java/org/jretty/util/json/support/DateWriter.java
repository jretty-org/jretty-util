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
 * Create by ZollTy on 2014-5-17 (http://blog.zollty.com/, zollty@163.com)
 */
package org.jretty.util.json.support;

import java.util.Date;

import org.jretty.util.DateFormatUtils;
import org.jretty.util.json.DefaultWriter;

/**
 * 默认为 yyyy-MM-dd HH:mm:ss 格式
 * @author zollty
 * @since 2014-5-17
 */
public class DateWriter implements ObjectWriter<Date> {
    
    private boolean useTimeValue;
    
    private DateFormatUtils dateFormat;

    public DateWriter() {
    }

    public DateWriter(String dateFormatStyle) {
        dateFormat = new DateFormatUtils(dateFormatStyle);
    }

    public DateWriter(boolean useTimeValue) {
        this.useTimeValue = useTimeValue;
    }

    @Override
    public void write(DefaultWriter out, Date date) {
        if (useTimeValue) {
            out.write(date.getTime());
            return;
        }
        if (dateFormat == null) {
            out.writeObject(DateFormatUtils.format_yyyy_MM_dd_HH_mm_ss(date));
        }
        else {
            out.writeObject(dateFormat.format(date));
        }
    }

    public boolean getUseTimeValue() {
        return useTimeValue;
    }

    public void setUseTimeValue(boolean useTimeValue) {
        this.useTimeValue = useTimeValue;
    }
}
