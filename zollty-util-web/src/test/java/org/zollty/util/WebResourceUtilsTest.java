/*
 * @(#)WebResourceUtilsTest.java
 * Travelsky Report Engine (TRE) Source Code, Version 2.0
 * Author(s): 
 * Zollty Tsou (http://blog.csdn.net/zollty, zouty@travelsky.com)
 * Copyright (C) 2014-2015 Travelsky Technology. All rights reserved.
 */
package org.zollty.util;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.zollty.log.LogFactory;
import org.zollty.log.Logger;
import org.zollty.util.resource.ClassPathResource;
import org.zollty.util.resource.FileSystemContextResource;
import org.zollty.util.resource.Resource;

/**
 * @author zollty
 * @since 2014-5-22
 */
@RunWith(JUnit4.class)
public class WebResourceUtilsTest {
    
    private static final Logger LOG = LogFactory.getLogger();

    @Test
    public void testGetResource() {
        String path = "file:C:\\Windows\\system\\SHELL.DLL";
        String path2 = "file:C:\\Windows\\system\\SHELL.DLL";

        Resource resource;
        resource = WebResourceUtils.getResource(path, null, null);
        Assert.assertTrue(resource.exists());

        resource = WebResourceUtils.getResource(path2, null, null);
        Assert.assertTrue(resource.exists());

        // 不存在，但是不会报错。获取的resource默认为FileSystemContextResource
        String path3 = "file0:C:\\Windows\\system\\SHELL.DLL";
        resource = WebResourceUtils.getResource(path3, null, null);
        Assert.assertFalse(resource.exists());
        try {
            resource.getFile();

            Assert.assertEquals(FileSystemContextResource.class, resource.getClass());
        }
        catch (IOException e) {
            LOG.error(e);
        }

        String path4 = "org/zollty/util/resource/Resource.class";
        String path5 = ResourceUtils.CLASSPATH_URL_PREFIX + path4;
        String path6 = ResourceUtils.CLASSPATH_URL_PREFIX + "orgggg";
        resource = new ClassPathResource(path4);
        Assert.assertTrue(resource.exists());

        resource = WebResourceUtils.getResource(path5, null, null);
        Assert.assertTrue(resource.exists());
        Assert.assertEquals("org.zollty.util.resource.ClassPathResource", resource.getClass().getName());

        resource = WebResourceUtils.getResource(path6, null, null);
        Assert.assertFalse(resource.exists());
        Assert.assertEquals("org.zollty.util.resource.ClassPathResource", resource.getClass().getName());

    }

}
