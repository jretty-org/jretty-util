package org.zollty.util.support;

import static org.zollty.util.TestTools.CONTROLLER_ALERT;
import static org.zollty.util.TestTools.MSG_SPLIT;
import static org.zollty.util.TestTools.SERVICE_ALERT;
import static org.zollty.util.TestTools.UNDER_UNKNOWN_EXCEPTION;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;
import org.zollty.log.LogFactory;
import org.zollty.log.Logger;
import org.zollty.util.NestedCheckedException;
import org.zollty.util.NestedException;
import org.zollty.util.NestedIOException;

public class NestedExceptionDelegateTest {
    
    private static final Logger LOG = LogFactory.getLogger();
    
    @Test
    public void testAll() {

        try {
            throw new NestedCheckedException(new NestedCheckedException(new NestedCheckedException(CONTROLLER_ALERT)));
        }
        catch (Exception ne) {
            
//            DebugTool.println(((ExceptionDelegateSupport) ne).getDelegate().getExceptionName() + CONTROLLER_ALERT);
//            DebugTool.error(((NestedCheckedException) ne).getStackTraceStr());

            Assert.assertEquals(((ExceptionDelegateSupport) ne).getDelegate().getExceptionName() + CONTROLLER_ALERT,
                    ne.toString());

            Assert.assertEquals(((ExceptionDelegateSupport) ne).getDelegate().getExceptionName() + CONTROLLER_ALERT,
                    ((NestedException) ne).getStackTraceStr());
        }

        try {
            doController();
        }
        catch (Exception ne) {
            
//            DebugTool.printStack(ne);

            Assert.assertTrue(ne.getStackTrace()[0].toString().startsWith(this.getClass().getName() + ".underService"));

            Assert.assertEquals(IOException.class.getName()+": " + CONTROLLER_ALERT + MSG_SPLIT + SERVICE_ALERT + MSG_SPLIT
                    + UNDER_UNKNOWN_EXCEPTION, ne.toString());

        }
    }

    private void doController() throws NestedCheckedException {
        try {
            doService();
        }
        catch (Exception ioe) {
            throw new NestedCheckedException(ioe, CONTROLLER_ALERT);
        }
    }

    private void doService() throws NestedCheckedException {
        try {
            underService();
        }
        catch (IOException ioe) {
            throw new NestedCheckedException(ioe, SERVICE_ALERT);
        }
    }

    private void underService() throws IOException {
        throw new IOException(UNDER_UNKNOWN_EXCEPTION);
    }

    
    @Test
    public void testAll1() {

        try {
            throw new NestedCheckedException(new NestedCheckedException(new NestedIOException(CONTROLLER_ALERT)));
        }
        catch (Exception ne) {
            
//            DebugTool.println(((ExceptionDelegateSupport) ne).getDelegate().getExceptionName() + CONTROLLER_ALERT);
//            DebugTool.error(((NestedCheckedException) ne).getStackTraceStr());

            // 以下两句代码无需更改
            Assert.assertEquals(((ExceptionDelegateSupport) ne).getDelegate().getExceptionName() + CONTROLLER_ALERT,
                    ne.toString());
            Assert.assertEquals(((ExceptionDelegateSupport) ne).getDelegate().getExceptionName() + CONTROLLER_ALERT,
                    ((NestedCheckedException) ne).getStackTraceStr());

        }

        try {
            doController1();
        }
        catch (Exception ne) {
            
//            DebugTool.printStack(ne);
            
            LOG.info(ne.toString());

            Assert.assertTrue(ne.getStackTrace()[0].toString().startsWith(this.getClass().getName() + ".underService"));

            Assert.assertEquals(IOException.class.getName() + ": " + CONTROLLER_ALERT + MSG_SPLIT + SERVICE_ALERT
                    + MSG_SPLIT + UNDER_UNKNOWN_EXCEPTION, ne.toString());

        }
    }

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
}
