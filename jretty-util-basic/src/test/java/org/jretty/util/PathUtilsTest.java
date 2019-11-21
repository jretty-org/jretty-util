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
        
        assertEquals("org/home/res/aaa.txt", PathUtils.applyRelativePath("org/home/res/", "aaa.txt"));
        assertEquals("org/home/aaa.txt", PathUtils.applyRelativePath("org/home/res", "aaa.txt"));
        assertEquals("/home/aaa", PathUtils.applyRelativePath("/home/res", "aaa"));
    }
    
    @Test
    public void testConnectPaths() {
        assertEquals("/etc/", PathUtils.connectPaths("/etc/"));
        assertEquals("/etc", PathUtils.connectPaths("/etc"));
        assertEquals("/etc/aaa/bb", PathUtils.connectPaths("/etc/aaa", "bb"));
        assertEquals("etc/aaa/bb/cc", PathUtils.connectPaths("etc/aaa", "bb/cc"));
        assertEquals("/etc/aaa/bb", PathUtils.connectPaths("/etc/aaa/", "bb"));
        assertEquals("/etc/aaa/bb", PathUtils.connectPaths("/etc/aaa/", "/bb"));
        assertEquals("/etc/aaa/bb", PathUtils.connectPaths("/etc/aaa", "/bb"));
        assertEquals("/etc/aaa/bb/", PathUtils.connectPaths("/etc/aaa", "/bb/"));
        assertEquals("/etc/aaa/bb/cc/dd", PathUtils.connectPaths("/etc/aaa", "/bb/", "/cc/", "dd"));
        assertEquals("/etc/aaa/bb/cc/dd/", PathUtils.connectPaths("/etc/aaa", "/bb/", "/cc/", "dd/"));
    }

}
