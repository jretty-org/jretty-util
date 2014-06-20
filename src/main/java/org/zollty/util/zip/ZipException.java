/* @(#)ZipException.java 
 * Copyright (C) 2013-2014 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License").
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * Create by zollty on 2013-7-07 [http://blog.csdn.net/zollty (or GitHub)]
 */
package org.zollty.util.zip;

import org.zollty.util.NestedCheckedException;

/**
 * @author zollty 
 * @since 2013-7-07
 */
public class ZipException extends NestedCheckedException {

    private static final long serialVersionUID = -950595486850209684L;

    public ZipException(String message) {
        super(message);
    }

    public ZipException(Throwable e) {
        super(e);
    }

    public ZipException(Throwable e, String message) {
        super(e, message);
    }

    private static final String EXCEPTION_PRIFIX = "org.zollty.ZipException: ";

    @Override
    protected String getExceptionPrifix() {
        return EXCEPTION_PRIFIX;
    }
	
}
