/*
 * @(#)ResourceUtilsTest.java
 * Travelsky Report Engine (TRE) Source Code, Version 2.0
 * Author(s): 
 * Zollty Tsou (http://blog.csdn.net/zollty, zouty@travelsky.com)
 * Copyright (C) 2014-2015 Travelsky Technology. All rights reserved.
 */
package org.zollty.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.zollty.log.LogFactory;
import org.zollty.log.Logger;
import org.zollty.util.resource.Resource;

/**
 * @author zollty
 * @since 2014-5-22
 */
@RunWith(JUnit4.class)
public class ResourceUtilsTest {
    
    private static final Logger LOG = LogFactory.getLogger(ResourceUtilsTest.class);
    
    @Test
    public void test() {
        try {
            new URL("netdoc://g");
        }
        catch (MalformedURLException ex) {
            Assert.fail(ex.toString());
        }
        try {
            new URL("doc://g");
            Assert.fail("该协议未被支持");
        }
        catch (MalformedURLException ex) {
//            ex.printStackTrace();
        }
    }
    
    
    @Test
    public void testGetUrlResource () {
        String path = "file:C:\\Windows\\system\\SHELL.DLL";
        
        String path2 = "file:C:\\Windows\\system\\..\\system\\SHELL.DLL";
        
        Resource resource;
        try {
            resource = ResourceUtils.getUrlResource(path);
            Assert.assertTrue(resource.exists());
            
            resource = ResourceUtils.getUrlResource(path2);
            Assert.assertTrue(resource.exists());
        }
        catch (IOException e) {
            Assert.fail(e.toString());
        }
    }
    
    @Test
    public void testGetFileSystemResource () {
        String path = "file:C:\\Windows\\system\\SHELL.DLL";
        
        String path2 = "file:C:\\Windows\\system\\..\\system\\SHELL.DLL";
        
        Resource resource;
        resource = ResourceUtils.getFileSystemResource(path);
        Assert.assertTrue(resource.exists());
        
        resource = ResourceUtils.getFileSystemResource(path2);
        Assert.assertTrue(resource.exists());
    }
    
    
    
    // @Test
    public void test01() throws Exception {
       // getInputStreamFromJar();
    }
    
    
    public void getInputStreamFromJar(){
        try {
//            InputStream in = ResourceUtils.getInputStreamFromJar(Logger.class, "org/zollty/log/Logger.class");
            InputStream in = ResourceUtils.getInputStreamFromJar(Logger.class, "zollty-log0.properties");
            LOG.info(in);
        }
        catch (NestedCheckedException e) {
            LOG.error(e);
        }
    }
    
    public void getInputStreamFromClassPath(){
        try {
            InputStream in = ResourceUtils.getInputStreamFromClassPath(getClass().getClassLoader(), "org/zollty/util/StringUtils.class");
            LOG.info(in);
        }
        catch (NestedCheckedException e) {
            LOG.error(e);
        }
    }
    
    public void getInputStreamFromClassLoader(){
        try {
            InputStream in = ResourceUtils.getInputStreamFromClassLoader(getClass(), "org/zollty/util/StringUtils.class");
            LOG.info(in);
        }
        catch (NestedCheckedException e) {
            LOG.error(e);
        }
    }
    
    

}
