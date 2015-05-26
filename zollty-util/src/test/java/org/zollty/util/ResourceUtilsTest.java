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
    
    /**
     * JDK支持的8种URL协议
     *     http://www.math.com/faq.html
     *     https://www.math.com/faq.html
     *     ftp://ftp.is.co.za/rfc/rfc1808.txt
     *     file:C:/test.dat
     *     jar:dddddd
     *     netdoc:sdfffdfdffd
     *     mailto:mduerst@ifi.unizh.ch
     *     gopher://spinaltap.micro.com/Weather/California/Los%20Angeles
     *     支持以上8种协议。参见 JRE -> lib -> rt.jar -> \sun\net\www\protocol
     */
    @Test
    public void testURL() {
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
            LOG.info(ex.toString());
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
    
    
    
    /**
     * 取得class所在 jar包 中的资源 {注意jar必须在文件目录下，不能在war或ear包中}<BR>
     */
    @Test
    public void testGetInputStreamFromJar(){
        getInputStreamFromJar(Logger.class, "org/zollty/log/Logger.class");
        getInputStreamFromJar(Logger.class, "org/zollty/log/");
        
        noInputStreamFromJar(Logger.class, "zollty-log0.properties");
    }
    
    /**
     * 取得clazz.getClassLoader()所在 ClassPath下的资源（非url.openStream()模式，支持动态更新）
     */
    @Test
    public void testGetInputStreamFromClassPath(){
        getInputStreamFromClassPath(getClass().getClassLoader(), "org/zollty/util/StringUtils.class");
        noInputStreamFromClassPath(getClass().getClassLoader(), "org/zollty/util/StringUtils222.class");
    }
    
    /**
     * 采用Java ClassLoader自带的getResourceAsStream获取资源。
     * （可以读取ClassLoader下classpath/jar/zip/war/ear中的资源）
     * 但是不支持动态更新和加载，只在ClassLoader初始化时加载一遍，以后更改，不会再次加载
     */
    @Test
    public void testGetInputStreamFromClassLoader(){
        getInputStreamFromClassLoader(getClass(), "org/zollty/util/StringUtils.class");
        noInputStreamFromClassLoader(getClass(), "org/zollty/util/StringUtils222.class");
    }
    
    @Test
    public void testGetResource(){
        Resource resource = ResourceUtils.getResource("classpath:org/zollty/util/StringUtils.class");
        Assert.assertTrue(resource.exists());
        try {
            LOG.info(resource.contentLength());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        
        resource = ResourceUtils.getResource("classpath:org/zollty/log/Logger.class");
        Assert.assertTrue(resource.exists());
        try {
            LOG.info(resource.contentLength());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        
        resource = ResourceUtils.getResource("classpath:org/zollty/log/");
        Assert.assertTrue(resource.exists());
        try {
            LOG.info(resource.contentLength());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        
        resource = ResourceUtils.getResource("classpath:org/zollty/util/StringUtils222.class");
        Assert.assertFalse(resource.exists());
    }
    
    
    private void getInputStreamFromJar(Class<?> jarClazz, String resourcePath){
        try {
            InputStream in = ResourceUtils.getInputStreamFromJar(jarClazz, resourcePath);
            LOG.info(in.available());
        }
        catch (NestedCheckedException e) {
            Assert.fail(e.toString());
        }
        catch (IOException e) {
            LOG.error(e);
        }
    }
    
    private void noInputStreamFromJar(Class<?> jarClazz, String resourcePath){
        try {
            ResourceUtils.getInputStreamFromJar(jarClazz, resourcePath);
            Assert.fail("assert no resources under "+ resourcePath);
        }
        catch (NestedCheckedException e) {
            LOG.info(e);
        }
    }
    
    
    private void getInputStreamFromClassPath(ClassLoader classLoader, String resourcePath) {
        try {
            InputStream in = ResourceUtils.getInputStreamFromClassPath(classLoader, resourcePath);
            LOG.info(in.available());
        }
        catch (NestedCheckedException e) {
            Assert.fail(e.toString());
        }
        catch (IOException e) {
            LOG.error(e);
        }
    }
    
    private void noInputStreamFromClassPath(ClassLoader classLoader, String resourcePath) {
        try {
            ResourceUtils.getInputStreamFromClassPath(classLoader, resourcePath);
            Assert.fail("assert no resources under "+ resourcePath);
        }
        catch (NestedCheckedException e) {
            LOG.info(e);
        }
    }
    
    
    @SuppressWarnings("deprecation")
    private void getInputStreamFromClassLoader(Class<?> clazz, String resourcePath) {
        try {
            InputStream in = ResourceUtils.getInputStreamFromClassLoader(getClass(), resourcePath);
            LOG.info(in.available());
        }
        catch (NestedCheckedException e) {
            Assert.fail(e.toString());
        }
        catch (IOException e) {
            LOG.error(e);
        }
    }
    
    @SuppressWarnings("deprecation")
    private void noInputStreamFromClassLoader(Class<?> clazz, String resourcePath) {
        try {
            ResourceUtils.getInputStreamFromClassLoader(getClass(), resourcePath);
            Assert.fail("assert no resources under "+ resourcePath);
        }
        catch (NestedCheckedException e) {
            LOG.info(e);
        }
    }
    
    

}
