package org.jretty.util;

/**
 * checkLine=include： true 不过滤，false 过滤
 *
 * @author zollty
 * @since 2020年11月2日
 */
public class LineCheckerTools {

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

    public static boolean checkLineSpringAop(String line) {
        return !line.startsWith("\tat org.springframework.aop");
    }

    public static boolean checkLineSpringServlet(String line) {
        return !line.startsWith("\tat org.springframework.web.servlet");
    }

    public static boolean checkLineSpringFilter(String line) {
        return !line.startsWith("\tat org.springframework.web.filter");
    }
}
