package org.zollty.util;

import static org.zollty.util.TestTools.CONTROLLER_ALERT;
import static org.zollty.util.TestTools.SERVICE_ALERT;
import static org.zollty.util.TestTools.UNDER_UNKNOWN_EXCEPTION;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

public class NestedCheckedExceptionTest {
    

    @Test
    public void testGetCause() {
        try {
            throw new NestedCheckedException(new NestedIOException(new IOException(CONTROLLER_ALERT)));
        }
        catch (Exception ne) {
            Assert.assertEquals(IOException.class, ne.getCause().getClass());
        }
    }
    
    @Test
    public void testGetMessage() {
        try {
            throw new NestedCheckedException(new NestedIOException(new IOException(CONTROLLER_ALERT)));
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
