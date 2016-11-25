/* 
 * Create by ZollTy (blog.zollty.com) on 2015年12月3日
 */
package org.jretty.util;

import java.util.Map;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeansException;
import org.springframework.cglib.beans.BeanMap;

/**
 * 两个Z（Zz）开头的类，仅供本项目内部使用，外部勿调用！！（外部调用走 Utils.xxx）
 * @author zollty
 * @since 2015年12月3日
 */
class ZzBeanUtils {
    

    /**
     * Copy the property values of the given source bean into the target bean.
     * <p>Note: The source and target classes do not have to match or even be derived
     * from each other, as long as the properties match. Any bean properties that the
     * source bean exposes but the target bean does not will silently be ignored.
     * <p>This is just a convenience method. For more complex transfer needs,
     * consider using a full BeanWrapper.
     * @param source the source bean
     * @param target the target bean
     * @throws BeansException if the copying failed
     * @see BeanWrapper
     */
    public static void copyObject(Object source, Object target){
        org.springframework.beans.BeanUtils.copyProperties(source, target);
    }
    
    /**
     * convert object to Map
     * @param obj
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static Map objectToMap(Object obj) {
        if (obj == null)
            return null;
        return BeanMap.create(obj);
    }
    
}