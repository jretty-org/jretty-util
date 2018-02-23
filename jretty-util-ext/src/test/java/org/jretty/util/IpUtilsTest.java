/**
 * 
 */
package org.jretty.util;

import org.jretty.tesper.LoopExecute;
import org.jretty.tesper.TestTools;

/**
 * 
 * @author zollty
 * @since 2018年2月23日
 */
public class IpUtilsTest {
    
    public static void main(String[] args) throws Exception {
        loopExecute(100); // 预热CPU

        loopExecute(10000); // 正式测试
    }
    
    // 循环执行
    static void loopExecute(final int times) throws Exception {
        TestTools.loopExecute(new LoopExecute() {

            @Override
            public int getLoopTimes() {

                return times;
            }

            @Override
            public void execute() throws Exception {

                IpUtils.getLocalIP();
            }
        });
        System.out.println("-------------------");
        TestTools.loopExecute(new LoopExecute() {

            @Override
            public int getLoopTimes() {

                return times;
            }

            @Override
            public void execute() throws Exception {

                IpUtils.getRealIP();
            }
        });
    }

}
