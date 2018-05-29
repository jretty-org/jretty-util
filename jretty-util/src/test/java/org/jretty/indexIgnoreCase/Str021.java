/**
 * 
 */
package org.jretty.indexIgnoreCase;

/**
 * 
 * @author zollty
 * @since 2018年5月29日
 */
public class Str021 {

    public static int lastIndexIgnoreCase(String source, String str) {
        return lastIndexIgnoreCase(source, str, source.length());
    }
    
    public static int lastIndexIgnoreCase(String source, String str, int fromIndex) {
        char[] value = source.toCharArray();
        return lastIndexOf(value, 0, value.length,
                str.toCharArray(), 0, str.length(), fromIndex);
    }
    
    static int lastIndexOf(char[] source, int sourceOffset, int sourceCount,
            char[] target, int targetOffset, int targetCount,
            int fromIndex) {
        /*
         * Check arguments; return immediately where possible. For
         * consistency, don't check for null str.
         */
        int rightIndex = sourceCount - targetCount;
        if (fromIndex < 0) {
            return -1;
        }
        if (fromIndex > rightIndex) {
            fromIndex = rightIndex;
        }
        /* Empty string always matches. */
        if (targetCount == 0) {
            return fromIndex;
        }

        int strLastIndex = targetOffset + targetCount - 1;
        char strLastChar = target[strLastIndex];
        int min = sourceOffset + targetCount - 1;
        int i = min + fromIndex;

        startSearchForLastChar:
        while (true) {
            while (i >= min && !Str01.isEqual(source[i], strLastChar)) {
                i--;
            }
            if (i < min) {
                return -1;
            }
            int j = i - 1;
            int start = j - (targetCount - 1);
            int k = strLastIndex - 1;

            while (j > start) {
                if (!Str01.isEqual(source[j--], target[k--])) {
                    i--;
                    continue startSearchForLastChar;
                }
            }
            return start - sourceOffset + 1;
        }
    }

}
