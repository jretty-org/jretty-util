/**
 * 
 */
package org.jretty.indexIgnoreCase;

/**
 * 
 * @author zollty
 * @since 2018年5月29日
 */
public class Str041 {

    public static int lastIndexIgnoreCase(String subject, String search) {
        return lastIndexIgnoreCase(subject, search, subject.length());
    }

    public static int lastIndexIgnoreCase(String subject, String search, int soffset) {
        // 当被查找字符串或查找子字符串为空时，抛出空指针异常。
        if (subject == null || search == null) {
            throw new NullPointerException("输入的参数为空");
        }
        if (soffset <= 0 && search.equals("")) {
            return 0;
        }
        for (int i = soffset; 0 < i; i--) {
            if (subject.regionMatches(true, i, search, 0, search.length())) {
                return i;
            }
        }

        return -1;
    }

}