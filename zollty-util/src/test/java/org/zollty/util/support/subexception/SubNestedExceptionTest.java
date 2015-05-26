package org.zollty.util.support.subexception;

import static org.zollty.util.TestTools.CONTROLLER_ALERT;
import static org.zollty.util.TestTools.MSG_SPLIT;
import static org.zollty.util.TestTools.SERVICE_ALERT;
import static org.zollty.util.TestTools.UNDER_UNKNOWN_EXCEPTION;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;
import org.zollty.util.NestedCheckedException;
import org.zollty.util.support.ExceptionDelegateSupport;

public class SubNestedExceptionTest {

    /**
     * 测试结论：
     * 
     * NestedException的子类，可以混用，不会影响输出结果（输出结果均为最内层的那个Exception）。
     * 
     */

    @Test
    public void testAll() {

        try {
            throw new SubNestedException(new SubNestedException2(new SubNestedException(CONTROLLER_ALERT)));
        }
        catch (Exception ne) {

            // 以下两句代码无需更改
            Assert.assertEquals(((ExceptionDelegateSupport) ne).getDelegate().getExceptionName() + CONTROLLER_ALERT,
                    ne.toString());
            Assert.assertEquals(((ExceptionDelegateSupport) ne).getDelegate().getExceptionName() + CONTROLLER_ALERT,
                    ((NestedCheckedException) ne).getStackTraceStr());

        }

        try {
            doController();
        }
        catch (Exception ne) {

            Assert.assertTrue(ne.getStackTrace()[0].toString().startsWith(this.getClass().getName() + ".underService"));

            Assert.assertEquals(IOException.class.getName()+": " + CONTROLLER_ALERT + MSG_SPLIT + SERVICE_ALERT + MSG_SPLIT
                    + UNDER_UNKNOWN_EXCEPTION, ne.toString());

        }
    }

    private void doController() throws SubNestedException2 {
        try {
            doService();
        }
        catch (Exception ioe) {
            throw new SubNestedException2(ioe, CONTROLLER_ALERT);
        }
    }

    private void doService() throws SubNestedException {
        try {
            underService();
        }
        catch (IOException ioe) {
            throw new SubNestedException(ioe, SERVICE_ALERT);
        }
    }

    private void underService() throws IOException {
        throw new IOException(UNDER_UNKNOWN_EXCEPTION);
    }

}
