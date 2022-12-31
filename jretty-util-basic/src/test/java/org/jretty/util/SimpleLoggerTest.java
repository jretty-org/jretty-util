package org.jretty.util;

import org.jretty.log.LogFactory;
import org.jretty.log.Logger;
import org.jretty.util.SimpleLogger.LogImpl;

/**
 * 
 * @author zollty
 * @since 2021年6月17日
 */
public class SimpleLoggerTest {
    private static final int N = 35000; //Nothing in console and in debug inspect
    static SimpleLogger out = new SimpleLogger("");
    
    public static final LogImpl JLOG = new LogImpl() {
        private final Logger logger = LogFactory.getLogger("");
        @Override
        public void log(String str, boolean isError, boolean newLine) {
            if (!isError) {
                logger.debug(str);
            } else {
                logger.error(str);
            }
        }
    };
    
    public static void main(String[] args) {
        
        StringBuffer a = new StringBuffer();
        for (int i = 0; i < N; i++) {
            a.append("0");
        }
        System.out.println(a);
        
        out.setAddMeta(false);
        out.setLogImpl(JLOG);
        
        out.log("");
        
        out.log(12);
        
        out.log("xxxxxxxxxxxx");
        
        out.log(a, "eeeee");
        
        out.log("xxxxxxxxxxxx", "yyy", "zzz");
        
        out.print("xxx");
        out.print("yyy");
        out.log("");
        
        out.err("eeeeexxxxxxxxxxx");
    }

}
