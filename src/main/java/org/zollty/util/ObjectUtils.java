package org.zollty.util;

public class ObjectUtils {
    
    public static boolean safeEqual(Object obj1, Object obj2){
        if( obj1!=null ) {
            return obj1.equals(obj2);
        }
        if(obj2==null) {
            return true;
        }
        return false;
    }

}
