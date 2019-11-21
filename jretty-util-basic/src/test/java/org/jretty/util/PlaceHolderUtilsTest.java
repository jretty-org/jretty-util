/* 
 * Copyright (C) 2013-2020 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License").
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * Create by ZollTy on 2019-11-5 (http://blog.zollty.com/, zollty@163.com)
 */
package org.jretty.util;

import static org.junit.Assert.assertEquals;

import org.jretty.util.PlaceHolderParser.PRResult;
import org.junit.Assert;
import org.junit.Test;

/**
 * 
 * @author zollty
 * @since 2019-11-05
 */
public class PlaceHolderUtilsTest {
    
    @Test
    public void testResovlePlaceHolder0(){
        // 用于测试的输入字符串
        String input = "Do <a href=\"XXX\" >me a fa11vor? <a href=\"YYY\" >favor BBB<a href=\"CCC\"";

        String expect = "Do <a href=\"#?id=5\" >me a fa11vor? <a href=\"#?id=5\" >favor BBB<a href=\"#?id=5\"";

        // String patt = "<a href=\"(.*?)\"";
        final String placeholderPrefix = "<a href=\"";
        final String placeholderSuffix = "\"";
        final PlaceHolderParser pu = new PlaceHolderParser(placeholderPrefix, placeholderSuffix);
        
        String aaa = pu.resovlePlaceHolder(input, new PlaceHolderParser.PlaceholderResolver() {
            @Override
            public PRResult resolvePlaceholder(String placeholderName) {
                return new PRResult("#?id=5", true);
            }
        });
        
        Assert.assertEquals(expect, aaa);
    }


    @Test
    public void testResovlePlaceHolder(){
        String ret = SystemPlaceHolderUtils.resovlePlaceHolder("${catalina.base:-target}/logs");
        assertEquals("target/logs", ret);
        
        ret = SystemPlaceHolderUtils.resovlePlaceHolder("${TMP:-target}/logs");

        ret = SystemPlaceHolderUtils.resovlePlaceHolder("${TMP}/logs");

        ret = SystemPlaceHolderUtils.resovlePlaceHolder("${TMP/logs");
        assertEquals("${TMP/logs", ret);

        ret = SystemPlaceHolderUtils.resovlePlaceHolder("${TMP:-target/logs");
        assertEquals("${TMP:-target/logs", ret);
        
        ret  = SystemPlaceHolderUtils.resovlePlaceHolder("${TMP}}/logs");
        // System.out.println(ret);
    }
    
    /** Value separator for system property placeholders: ":". */
    public static final String DEFAULT_VALUE_FLAG = ":-";
    
    public static String resolveSystemVal(String placeholderName) {
        try {
            String propVal = System.getProperty(placeholderName);
            if (propVal == null) {
                // Fall back to searching the system environment.
                propVal = System.getenv(placeholderName);
            }
            return propVal;
        }
        catch (Throwable ex) {
            return null;
        }
    }
    
    @Test
    public void testResovlePlaceHolder2(){
        final PlaceHolderParser pu = new PlaceHolderParser();
        
        PlaceHolderParser.PlaceholderResolver sysPlaceholder = new PlaceHolderParser.PlaceholderResolver() {
            @Override
            public PRResult resolvePlaceholder(String placeholder) {
                int separatorIndex;
                if ((separatorIndex = placeholder.indexOf(DEFAULT_VALUE_FLAG)) == -1) {
                    return new PRResult(resolveSystemVal(placeholder));
                } else {
                    String actualPlaceholder = placeholder.substring(0, separatorIndex);
                    String propVal = resolveSystemVal(actualPlaceholder);
                    if (propVal == null) {
                        String defaultValue = placeholder.substring(separatorIndex + 2);
                        return new PRResult(defaultValue);
                    }
                    return new PRResult(propVal);
                }
            }
        };
        
        String ret = pu.resovlePlaceHolder("${catalina.base:-target}/logs", sysPlaceholder);
        assertEquals("target/logs", ret);
        
        ret = pu.resovlePlaceHolder("${TMP/logs", sysPlaceholder);
        assertEquals("${TMP/logs", ret);

        ret = pu.resovlePlaceHolder("${TMP:-target/logs", sysPlaceholder);
        assertEquals("${TMP:-target/logs", ret);
        
        ret = pu.resovlePlaceHolder("${XXXXX}/logs", sysPlaceholder, true);
        assertEquals("${XXXXX}/logs", ret);
    }
    
   
}
