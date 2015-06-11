/* 
 * Copyright (C) 2013-2015 the original author or authors.
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
package org.zollty.util;

import org.junit.Test;

/**
 * @author zollty
 * @since 2014-5-22
 */
public class StringUtilsTest {
    
    @Test
    public void testReplaceParams(){
        
        org.junit.Assert.assertEquals(StringUtils.replaceParams("dss{}-{}-{}ssssssss", new String[]{"1111", null, "2222"}),
                "dss1111-null-2222ssssssss");
        
        org.junit.Assert.assertEquals(StringUtils.replaceParams("dssssssssss", new String[]{"1111", null, "2222"}),
                "dssssssssss 1111 null 2222");
        
        org.junit.Assert.assertEquals(StringUtils.replaceParams("dsssss{}sssss", new String[]{"1111", null, "2222"}),
                "dsssss1111sssss null 2222");
        
        org.junit.Assert.assertEquals(StringUtils.replaceParams("dssssssssss{}", new String[]{"1111", null, "2222"}),
                "dssssssssss1111 null 2222");
    }
    
    @Test
    public void testApplyRelativePath(){
        String path = StringUtils.applyRelativePath("org/zollty/util/resource/Resource.class", "ClassPathResource.class");
        org.junit.Assert.assertEquals("org/zollty/util/resource/ClassPathResource.class", path);
    }
    
}
