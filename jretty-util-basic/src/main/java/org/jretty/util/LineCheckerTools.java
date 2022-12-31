package org.jretty.util;

/**
 * 
 * @author zollty
 * @since 2020年11月2日
 */
public class LineCheckerTools {

    // checkLine=include：
    //    true 不过滤，false 过滤

    public static boolean checkLineJava(String line) {
        return !line.startsWith("\tat sun.reflect");
    }

    public static boolean checkLineTomcat(String line) {
        return !(line.startsWith("\tat org.apache.catalina")
                || line.startsWith("\tat org.apache.coyote"));
    }

    public static boolean checkLineWas(String line) {
        return !(line.startsWith("\tat com.ibm.ws") 
                || line.startsWith("\tat com.ibm.io"));
    }
    
    public static boolean checkLineSpAOP(String line) {
        return !line.startsWith("\tat org.springframework.aop");
    }
    
    public static boolean checkLineSpServlet(String line) {
        return !line.startsWith("\tat org.springframework.web.servlet");
    }
    
    public static boolean checkLineSpFilter(String line) {
        return !line.startsWith("\tat org.springframework.web.filter");
    }
}
