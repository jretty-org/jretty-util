/* 
 * Copyright (C) 2015-2016 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License").
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * Create by ZollTy on 2015-3-17 (http://blog.zollty.com/, zollty@163.com)
 */
package org.jretty.util;

import java.io.IOException;

import org.jretty.log.LogFactory;
import org.jretty.log.Logger;
import org.jretty.util.resource.ClassPathResource;
import org.jretty.util.resource.Resource;
import org.jretty.util.resource.web.ServletContextResource;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * @author zollty
 * @since 2015-3-17
 */
@RunWith(JUnit4.class)
public class WebResourceUtilsTest {
    
    private static final Logger LOG = LogFactory.getLogger();

    @Ignore
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

            Assert.assertEquals(ServletContextResource.class, resource.getClass());
        }
        catch (IOException e) {
            LOG.error(e);
        }

        String path4 = "org/jretty/util/resource/Resource.class";
        String path5 = ResourceUtils.CLASSPATH_URL_PREFIX + path4;
        String path6 = ResourceUtils.CLASSPATH_URL_PREFIX + "orgggg";
        resource = new ClassPathResource(path4);
        Assert.assertTrue(resource.exists());

        resource = WebResourceUtils.getResource(path5, null, null);
        Assert.assertTrue(resource.exists());
        Assert.assertEquals("org.jretty.util.resource.ClassPathResource", resource.getClass().getName());

        resource = WebResourceUtils.getResource(path6, null, null);
        Assert.assertFalse(resource.exists());
        Assert.assertEquals("org.jretty.util.resource.ClassPathResource", resource.getClass().getName());

    }

}
