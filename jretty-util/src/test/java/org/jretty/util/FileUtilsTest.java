/**
 * 
 */
package org.jretty.util;

import java.io.File;
import java.io.IOException;

import org.jretty.tesper.LoopExecute;
import org.jretty.tesper.TestTools;

/**
 * 
 * @author zollty
 * @since 2018年2月26日
 */
public class FileUtilsTest {
    
    static long count=0;
    
    static void findFileTest() throws IOException {
        final String dir = "D:\\1sync\\article\\_posts";
        final String target = "2017-10-27-vpn-ping-problem.html";
        System.out.println(FileUtils.findFile(new File(dir), target));
    }
    
    static void cloneFileTest() throws IOException {
        final String template = "D:\\0sync-local\\git\\fast\\fast-demo-mini\\a";
        final String target = "D:\\0sync-local\\git\\fast\\temp\\a";
        System.out.println(new File(template).isFile());
        FileUtils.cloneFile(new File(template), new File(target));
    }
    
    static void copyFolderTest() throws IOException {
        final String template = "D:\\0sync-local\\git\\fast\\fast-demo-mini\\pom.xml";
        final String target = "D:\\0sync-local\\git\\fast\\temp\\pom.xml";
        FileUtils.copyFolder(template, target);
    }
    
    public static void main(String[] args) throws Exception {
        // testGetFileLength();
        
        
        findFileTest();
        
        
    }
    
    static void testPerf() throws Exception {
        final String template = "D:\\0sync-local\\git\\fast\\fast-demo-mini\\pom.xml";
        final String target = "D:\\0sync-local\\git\\fast\\temp\\aaa";
        
        TestTools.loopExecute(new LoopExecute() {
            
            @Override
            public int getLoopTimes() {
                return 100;
            }
            
            @Override
            public void execute() throws Exception {
                // 164.06 ms/n
                FileUtils.copyFolder(template, target);
            }
        });
    }
    
    static void testGetFileLength() throws Exception {
        final String srcUrl = "D:\\WindowsImageBackup\\LAPTOP-RI69UHU9\\Backup 2017-12-12 071326\\c4218075-9b41-4e42-bd4a-781f4cd2b1bb.vhdx";
        
        TestTools.loopExecute(new LoopExecute() {
            
            @Override
            public int getLoopTimes() {
                
                return 10000;
            }
            
            @Override
            public void execute() throws Exception {
                File src = new File(srcUrl);
                count = 100l + src.length(); // 19 GB
            }
        });
        
        System.out.println(count/1024/1024/1024);
    }

}
