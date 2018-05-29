/**
 * 
 */
package org.jretty.indexIgnoreCase;

/**
 * 
 * @author zollty
 * @since 2018年5月29日
 */
public class Str01 {
    
    /**
     * 判断两个字符是否相等。
     * @return 若是英文字母，不区分大小写，相等true，不等返回false； 
     *  若不是则区分，相等返回true，不等返回false。
     */
    static boolean isEqual(char c1, char c2) {
        // 字母小写 字母大写
        if (((97 <= c1 && c1 <= 122) || (65 <= c1 && c1 <= 90)) 
                && ((97 <= c2 && c2 <= 122) || (65 <= c2 && c2 <= 90))
                && ((c1 - c2 == 32) || (c2 - c1 == 32))) {

            return true;
        } else if (c1 == c2) {
            return true;
        }
        return false;
    }

    public static int indexIgnoreCase(String array, String target) {
        return indexOf(array.toCharArray(), target.toCharArray());
    }
    
    private static int indexOf(char[] array, char[] target) {
        if (target.length == 0) {
          return 0;
        }
        outer:
        for (int i = 0; i < array.length - target.length + 1; i++) {
          for (int j = 0; j < target.length; j++) {
            if (!isEqual(array[i + j], target[j]) ) {
              continue outer;
            }
          }
          return i;
        }
        return -1;
      }

}
