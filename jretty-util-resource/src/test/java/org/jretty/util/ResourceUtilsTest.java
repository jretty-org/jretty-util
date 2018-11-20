/* 
 * Copyright (C) 2014-2015 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License").
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * Create by ZollTy on 2014-5-22 (http://blog.zollty.com/, zollty@163.com)
 */
package org.jretty.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.jretty.log.LogFactory;
import org.jretty.log.Logger;
import org.jretty.util.resource.Resource;

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
    
    @Ignore
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
    
    @Ignore
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
        getInputStreamFromJar(Logger.class, "org/jretty/log/Logger.class");
        getInputStreamFromJar(Logger.class, "org/jretty/log/");
        
        noInputStreamFromJar(Logger.class, "zollty-log0.properties");
    }
    
    /**
     * 取得clazz.getClassLoader()所在 ClassPath下的资源（非url.openStream()模式，支持动态更新）
     */
    @Test
    public void testGetInputStreamFromClassPath(){
        getInputStreamFromClassPath(getClass().getClassLoader(), "org/jretty/util/ResourceUtils.class");
        noInputStreamFromClassPath(getClass().getClassLoader(), "org/jretty/util/StringUtils222.class");
    }
    
    @Test
    @Ignore
    public void testgetResourcesByPathMatchingResolver() throws IOException{
        Resource[] resources = ResourceUtils.getResourcesByPathMatchingResolver("classpath*:jretty-log.properties");
        LOG.info("resources.length: "+ resources.length);
        System.out.println(resources[0].exists());
    }
        
    @Test
    @Ignore
    public void testGetResource(){
        Resource resource = ResourceUtils.getResource("classpath:org/jretty/util/ResourceUtils.class");
        Assert.assertTrue(resource.exists());
        try {
            LOG.info(resource.contentLength());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        
        resource = ResourceUtils.getResource("classpath:org/jretty/log/Logger.class");
        Assert.assertTrue(resource.exists());
        try {
            LOG.info(resource.contentLength());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        
        resource = ResourceUtils.getResource("classpath:org/jretty/log/");
        Assert.assertTrue(resource.exists());
        try {
            LOG.info(resource.contentLength());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        
        resource = ResourceUtils.getResource("classpath:org/jretty/util/StringUtils222.class");
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
    
    
    

}
