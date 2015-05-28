package org.zollty.util;

import java.util.Date;

public class ObjectUtils {

    public static boolean safeEqual(Object obj1, Object obj2) {
        if (obj1 != null) {
            return obj1.equals(obj2);
        }
        if (obj2 == null) {
            return true;
        }
        return false;
    }

    /**
     * 将参数Object数组，转换成字符串显示
     */
    public static String arrayToString(Object[] params) {
        if (params == null) {
            return "null";
        }
        StringBuilder buff = new StringBuilder();
        buff.append("{");
        boolean first = true;
        for (Object obj : params) {
            if (first) {
                first = false;
            }
            else {
                buff.append(",");
            }

            if (obj == null) {
                buff.append("null");
            }
            else {
                if (obj instanceof Date) {
                    buff.append("[" + DateFormatUtils.format_yyyy_MM_dd((Date) obj) + "]");
                }
                else {
                    buff.append("[" + obj.toString() + "]");
                }
            }
        }
        buff.append("}");
        return buff.toString();
    }

}
