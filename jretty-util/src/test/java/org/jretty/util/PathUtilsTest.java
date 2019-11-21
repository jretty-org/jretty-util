/**
 * 
 */
package org.jretty.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * 
 * @author zollty
 * @since 2019年11月5日
 */
public class PathUtilsTest {

    @Test
    public void testApplyRelativePath(){
        String path = PathUtils.applyRelativePath("org/jretty/util/resource/Resource.class", "ClassPathResource.class");
        assertEquals("org/jretty/util/resource/ClassPathResource.class", path);
    }

}
