package org.zollty.util;

import static org.zollty.util.TestTools.CONTROLLER_ALERT;
import static org.zollty.util.TestTools.MSG_SPLIT;
import static org.zollty.util.TestTools.SERVICE_ALERT;
import static org.zollty.util.TestTools.UNDER_UNKNOWN_EXCEPTION;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

public class BasicIOExceptionTest {
    
   
    @Test
    public void test1() {

        try {
            doController1();
        }
        catch (Exception ne) {
            DebugTool.printStack(ne);
//            DebugTool.error(ne.toString());

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
            DebugTool.printStack(ne);
//            DebugTool.error(ne.toString());

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
