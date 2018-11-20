/**
 * 
 */
package org.jretty.indexIgnoreCase;

/**
 * 
 * @author zollty
 * @since 2018年5月29日
 */
public class Str031 {

    /**
     * <p>Case in-sensitive find of the last index within a String.</p>
     *
     * <p>A <code>null</code> String will return <code>-1</code>.
     * A negative start position returns <code>-1</code>.
     * An empty ("") search String always matches unless the start position is negative.
     * A start position greater than the string length searches the whole string.</p>
     *
     * <pre>
     * StringUtils.lastIndexOfIgnoreCase(null, *)          = -1
     * StringUtils.lastIndexOfIgnoreCase(*, null)          = -1
     * StringUtils.lastIndexOfIgnoreCase("aabaabaa", "A")  = 7
     * StringUtils.lastIndexOfIgnoreCase("aabaabaa", "B")  = 5
     * StringUtils.lastIndexOfIgnoreCase("aabaabaa", "AB") = 4
     * </pre>
     *
     * @param subject  the String to check, may be null
     * @param search  the String to find, may be null
     * @return the first index of the search String,
     *  -1 if no match or <code>null</code> string input
     */
    public static int lastIndexIgnoreCase(String subject, String search) {
        return lastIndexIgnoreCase(subject, search, subject.length());
    }

    public static int lastIndexIgnoreCase(String subject, String search, int fromIndex) {
        if (subject == null || search == null) {
            return -1;
        }
        int slen = subject.length();
        int tlen = search.length();
        if (fromIndex > (slen - tlen)) {
            fromIndex = slen - tlen;
        }
        if (fromIndex < 0) {
            return -1;
        }
        if (search.length() == 0) {
            return fromIndex;
        }

        int index1 = fromIndex;
        int index2 = 0;

        char c1;
        char c2;
        tlen = tlen - 1;
        loop1: while (true) {

            if (index1 >= 0) {
                c1 = subject.charAt(index1);
                c2 = search.charAt(index2);
            } else {
                break loop1;
            }

            while (true) {
                if (Str01.isEqual(c1, c2)) {
                    if (index1 < slen - 1 && index2 < tlen) {

                        c1 = subject.charAt(++index1);
                        c2 = search.charAt(++index2);
                    } else if (index2 == tlen) {

                        return fromIndex;
                    } else {

                        break loop1;
                    }
                } else {
                    // 在比较时，发现查找子字符串中某个字符不匹配，则重新开始查找子字符串
                    index2 = 0;
                    break;
                }
            }
            // 重新查找子字符串的位置
            index1 = --fromIndex;
        }

        return -1;
    }

}