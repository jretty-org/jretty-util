/**
 * 
 */
package org.jretty.indexIgnoreCase;

/**
 * 
 * @author zollty
 * @since 2018年5月29日
 */
public class Str02 {

    public static int indexIgnoreCase(String source, String str) {
        return indexIgnoreCase(source, str, 0);
    }
    
    public static int indexIgnoreCase(String source, String str, int fromIndex) {
        char[] value = source.toCharArray();
        return indexOf(value, 0, value.length,
                str.toCharArray(), 0, str.length(), fromIndex);
    }
    
    static int indexOf(char[] source, int sourceOffset, int sourceCount,
            char[] target, int targetOffset, int targetCount,
            int fromIndex) {
        if (fromIndex >= sourceCount) {
            return (targetCount == 0 ? sourceCount : -1);
        }
        if (fromIndex < 0) {
            fromIndex = 0;
        }
        if (targetCount == 0) {
            return fromIndex;
        }

        char first = target[targetOffset];
        int max = sourceOffset + (sourceCount - targetCount);

        for (int i = sourceOffset + fromIndex; i <= max; i++) {
            /* Look for first character. */
            if ( !Str01.isEqual(source[i], first) ) {
                while (++i <= max && !Str01.isEqual(source[i], first) );
            }

            /* Found first character, now look at the rest of v2 */
            if (i <= max) {
                int j = i + 1;
                int end = j + targetCount - 1;
                for (int k = targetOffset + 1; 
                        j < end && Str01.isEqual(source[j], target[k]); j++, k++);

                if (j == end) {
                    /* Found whole string. */
                    return i - sourceOffset;
                }
            }
        }
        return -1;
    }

}
