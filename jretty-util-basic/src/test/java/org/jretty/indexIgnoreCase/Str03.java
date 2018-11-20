/**
 * 
 */
package org.jretty.indexIgnoreCase;

/**
 * 
 * @author zollty
 * @since 2018年5月29日
 */
public class Str03 {

    /**
     * <p>Case in-sensitive find of the first index within a String.</p>
     *
     * <p>A <code>null</code> String will return <code>-1</code>.
     * A negative start position is treated as zero.
     * An empty ("") search String always matches.
     * A start position greater than the string length only matches
     * an empty search String.</p>
     *
     * <pre>
     * StringUtils.indexIgnoreCase(null, *)          = -1
     * StringUtils.indexIgnoreCase(*, null)          = -1
     * StringUtils.indexIgnoreCase("", "")           = 0
     * StringUtils.indexIgnoreCase("aabaabaa", "a")  = 0
     * StringUtils.indexIgnoreCase("aabaabaa", "b")  = 2
     * StringUtils.indexIgnoreCase("aabaabaa", "ab") = 1
     * </pre>
     *
     * @param subject  the String to check, may be null
     * @param search  the String to find, may be null
     * @return the first index of the search String,
     *  -1 if no match or <code>null</code> string input
     */
    public static int indexIgnoreCase(String subject, String search) {
        return indexIgnoreCase(subject, search, 0);
    }

    public static int indexIgnoreCase(String subject, String search, int fromIndex) {
        if (subject == null || search == null) {
            return -1;
        }
        if (fromIndex < 0) {
            fromIndex = 0;
        }
        if (search.length() == 0) {
            return fromIndex;
        }
        int index1 = fromIndex;
        int index2 = 0;

        char c1;
        char c2;
        int slen = subject.length();
        int tlen = search.length() - 1;
        loop1: while (true) {

            if (index1 < slen) {
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

                    index2 = 0;
                    break;
                }
            }
            // 重新查找子字符串的位置
            index1 = ++fromIndex;
        }

        return -1;
    }

}