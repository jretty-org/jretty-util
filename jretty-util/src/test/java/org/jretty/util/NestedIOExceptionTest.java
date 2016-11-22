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
 * Create by ZollTy on 2015-3-17 (http://blog.zollty.com/, zollty@163.com)
 */
package org.zollty.util;

import static org.zollty.util.ExceptionTestTools.CONTROLLER_ALERT;
import static org.zollty.util.ExceptionTestTools.SERVICE_ALERT;
import static org.zollty.util.ExceptionTestTools.UNDER_UNKNOWN_EXCEPTION;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author zollty
 * @since 2015-3-17
 */
public class NestedIOExceptionTest {
    

    @Test
    public void testGetCause() {
        try {
            throw new NestedIOException(new NestedCheckedException(new IOException(CONTROLLER_ALERT)));
        }
        catch (Exception ne) {
            Assert.assertEquals(IOException.class, ne.getCause().getClass());
        }
    }
    
    @Test
    public void testGetMessage() {
        try {
            throw new NestedIOException(new NestedCheckedException(new IOException(CONTROLLER_ALERT)));
        }
        catch (Exception ne) {
            Assert.assertEquals(ne.getMessage(), CONTROLLER_ALERT);
        }
    }
    
    @Test
    public void testToString() {
        try {
            throw new NestedCheckedException(new NestedIOException(new IOException(CONTROLLER_ALERT)));
        }
        catch (Exception ne) {
            Assert.assertEquals(ne.toString(), IOException.class.getName() + ": " + CONTROLLER_ALERT);
        }
    }
    
    @Test
    public void testGetStackTrace() {
        try {
            doController();
        }
        catch (Exception ne) {
            // DebugTool.printStack(ne);
            Assert.assertTrue(ne.getStackTrace()[0].toString().startsWith(this.getClass().getName() + ".underService"));

        }
    }

    @Test
    public void testGetStackTraceStr() {
        try {
            throw new NestedCheckedException(new NestedIOException(new IOException(CONTROLLER_ALERT)));
        }
        catch (NestedCheckedException ne) {
            Assert.assertTrue(ne.getStackTraceStr().startsWith(IOException.class.getName() + ": " + CONTROLLER_ALERT));
        }
    }
    
    
    ////////////////////////

    private void doController() throws NestedCheckedException {
        try {
            doService();
        }
        catch (Exception ioe) {
            throw new NestedCheckedException(ioe, CONTROLLER_ALERT);
        }
    }

    private void doService() throws IOException {
        try {
            underService();
        }
        catch (IOException ioe) {
            throw new NestedIOException(ioe, SERVICE_ALERT);
        }
    }

    private void underService() throws IOException {
        throw new IOException(UNDER_UNKNOWN_EXCEPTION);
    }

}
