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
package org.jretty.util;

import static org.jretty.util.ExceptionTestTools.CONTROLLER_ALERT;
import static org.jretty.util.ExceptionTestTools.MSG_SPLIT;
import static org.jretty.util.ExceptionTestTools.SERVICE_ALERT;
import static org.jretty.util.ExceptionTestTools.UNDER_UNKNOWN_EXCEPTION;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;
import org.jretty.log.LogFactory;
import org.jretty.log.Logger;

/**
 * @author zollty
 * @since 2015-3-17
 */
public class BasicIOExceptionTest {
    
    private static final Logger LOG = LogFactory.getLogger();
   
    @Test
    public void test1() {

        try {
            doController1();
        }
        catch (Exception ne) {
            LOG.info(ne);

            Assert.assertTrue(ne.getStackTrace()[0].toString().startsWith(this.getClass().getName() + ".underService"));

            Assert.assertEquals(IOException.class.getName() + ": " + CONTROLLER_ALERT + MSG_SPLIT + SERVICE_ALERT + MSG_SPLIT
                    + UNDER_UNKNOWN_EXCEPTION, ne.toString());
        }
    }
    
    @Test
    public void test2() {

        try {
            doController2();
        }
        catch (Exception ne) {
            LOG.info(ne);

            Assert.assertTrue(ne.getStackTrace()[0].toString().startsWith(this.getClass().getName() + ".underService"));

            Assert.assertEquals(NestedIOException.EXCEPTION_PRIFIX + CONTROLLER_ALERT + MSG_SPLIT + SERVICE_ALERT + MSG_SPLIT
                    + UNDER_UNKNOWN_EXCEPTION, ne.toString());
        }
    }
    
    
    
    ////////////////////////////////////

    private void doController1() throws NestedCheckedException {
        try {
            doService1();
        }
        catch (Exception ioe) {
            throw new NestedCheckedException(ioe, CONTROLLER_ALERT);
        }
    }

    private void doService1() throws NestedCheckedException {
        try {
            underService1();
        }
        catch (IOException ioe) {
            throw new NestedCheckedException(ioe, SERVICE_ALERT);
        }
    }
    
    private void underService1() throws IOException {
        throw new IOException(UNDER_UNKNOWN_EXCEPTION);
    }
    
    
    
    
    private void doController2() throws NestedCheckedException {
        try {
            doService2();
        }
        catch (Exception ioe) {
            throw new NestedCheckedException(ioe, CONTROLLER_ALERT);
        }
    }

    private void doService2() throws NestedCheckedException {
        try {
            underService2();
        }
        catch (IOException ioe) {
            throw new NestedCheckedException(ioe, SERVICE_ALERT);
        }
    }

    
    private void underService2() throws IOException {
        throw new NestedIOException(UNDER_UNKNOWN_EXCEPTION);
    }

}
