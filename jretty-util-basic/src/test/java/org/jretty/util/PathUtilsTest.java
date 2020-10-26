package org.jretty.util;

import static org.junit.Assert.assertEquals;

import org.junit.Ignore;
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
        
        assertEquals("D:/__SYNC0/ZOLLTY-架构实践/bb", PathUtils.connectPaths("D:\\__SYNC0\\ZOLLTY-架构实践", "bb"));
        assertEquals("D:/__SYNC0/ZOLLTY-架构实践/bb/cc", PathUtils.connectPaths("D:\\__SYNC0\\ZOLLTY-架构实践", "/bb/cc"));
    }
    
    @Test
    public void testStripFilenameFromPath() {
        assertEquals("org/home/res/aaa", PathUtils.stripFileExtensionFromPath("org/home/res/aaa.txt"));
        assertEquals("org/home/res/aaa", PathUtils.stripFileExtensionFromPath("org/home/res/aaa"));
    }
    
    @Test
    public void testGetFilenameExtension(){
        assertEquals("txt", PathUtils.getFilenameExtension("org/home/res/aaa.txt"));
        assertEquals(null, PathUtils.getFilenameExtension("org/home/res/aaa"));
    }
    
    @Test
    @Ignore
    public void testGetNewPath() {
        assertEquals("D:/__SYNC1/2332/aaa.zip", PathUtils.getNewPath("D:\\__SYNC1\\2332\\aaa.zip"));
        assertEquals("D:/__SYNC1/2332/sql-3.zip", PathUtils.getNewPath("D:\\__SYNC1\\2332\\sql.zip"));
    }
    
    @Test
    public void testReplacePath() {
        String oldPath = "D:\\C\\__SYNC1\\2332\\aaa.zip";
        String newPath = "D:/__SYNC0/aaa.zip";
        assertEquals(newPath, PathUtils.replacePath(oldPath, "C/__SYNC1/2332", "__SYNC0"));
    }

}
