/*
 * @(#)ResourceUtilsTest.java
 * Travelsky Report Engine (TRE) Source Code, Version 2.0
 * Author(s): 
 * Zollty Tsou (http://blog.csdn.net/zollty, zouty@travelsky.com)
 * Copyright (C) 2014-2015 Travelsky Technology. All rights reserved.
 */
package org.zollty.util;

import java.io.InputStream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.zollty.log.LogFactory;
import org.zollty.log.Logger;

/**
 * @author zollty
 * @since 2014-5-22
 */
@RunWith(JUnit4.class)
public class ResourceUtilsTest {
    
    private static final Logger LOG = LogFactory.getLogger(ResourceUtilsTest.class);
    
    // 推荐使用 JUnit4 单元测试
    // JUnit4运行方式: 单击鼠标右键-> Run As-> JUnit Test
    @Test
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
