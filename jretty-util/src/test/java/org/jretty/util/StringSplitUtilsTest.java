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
 * Create by ZollTy on 2014-6-11 (http://blog.zollty.com/, zollty@163.com)
 */
package org.jretty.util;

import java.util.Arrays;

import org.junit.Test;

/**
 * @author zollty
 * @since 2014-6-11
 */
public class StringSplitUtilsTest {
    
    @Test
    public void test(){

      splitTest("ab   de fg", null, "[ab, , , de, fg]");
      splitByWholeSeparatorTest("ab   de fg", null, "[ab, , , de, fg]");
      
      splitTest("ab  KKK\t de fg \n\r LLLL", null, "[ab, , KKK, , de, fg, , , , LLLL]");
      splitByWholeSeparatorTest("ab  KKK\t de fg \n\r LLLL", null, "[ab, , KKK, , de, fg, , , , LLLL]");
      
      splitTest("00000\n00000", "\n", "[00000, 00000]");
      splitByWholeSeparatorTest("00000\n00000", "\n", "[00000, 00000]");
      
      
      splitTest("/app/opt///was/", "/", "[, app, opt, , , was, ]");
      splitByWholeSeparatorTest("/app/opt///was/", "/", "[, app, opt, , , was, ]");
      splitByWholeSeparatorTest("/app/opt///was//", "//", "[/app/opt, /was, ]");
      splitByWholeSeparatorTest("/app/opt///was", "//", "[/app/opt, /was]");
      
      splitTest2("/app/opt///was/", "/", "[, app, opt, , , was]");
      splitByWholeSeparatorTest2("/app/opt///was/", "/", "[, app, opt, , , was]");
      splitByWholeSeparatorTest2("/app/opt///was//", "//", "[/app/opt, /was]");
      splitByWholeSeparatorTest2("/app/opt///was", "//", "[/app/opt, /was]");
      
      splitNoEmptyTest("/app/opt///was/", "/", "[app, opt, was]");
      splitByWholeSeparatorNoEmptyTest("/app/opt///was/", "/", "[app, opt, was]");
      
      
      splitTest("app/opt///was", "/", "[app, opt, , , was]");
      splitByWholeSeparatorTest("app/opt///was", "/", "[app, opt, , , was]");
      
      splitTest("app/opt///was", "//", "[app, opt, , , was]");
      splitByWholeSeparatorTest("app/opt///was", "//", "[app/opt, /was]");
      
      splitTest("app/opt  was", "/", "[app, opt  was]");
      splitByWholeSeparatorTest("app/opt  was", "/", "[app, opt  was]");
      
      splitTest("app-/-opt-/-was", "-/-", "[app, , , opt, , , was]");
      splitByWholeSeparatorTest("app-/-opt-/-was", "-/-", "[app, opt, was]");
      
      // 保留前后空白字符
      splitTest("\n\rapp -/-opt-/- was\n\r", "-/-", "[\n\rapp , , , opt, , ,  was\n\r]");
      splitByWholeSeparatorTest("\n\rapp -/-opt-/- was\n\r", "-/-", "[\n\rapp , opt,  was\n\r]");
      // trim前后空白字符
      splitTestTrim("\n\rapp -/-opt-/- was\n\r", "-/-", "[app, , , opt, , , was]");
      splitByWholeSeparatorTestTrim("\n\rapp -/-opt-/- was\n\r", "-/-", "[app, opt, was]");
      
      splitTest("app-/-opt-/--/-was", "-/--/-", "[app, , , opt, , , , , , was]");
      splitByWholeSeparatorTest("app-/-opt-/--/-was", "-/--/-", "[app-/-opt, was]");
      
      splitTest("app-KKKKKK/-opt-/--/-was", "-/--/-", "[app, KKKKKK, , opt, , , , , , was]");
      splitByWholeSeparatorTest("app-KKKKKK/-opt-/--/-was", "-/--/-", "[app-KKKKKK/-opt, was]");
      
    }
    
    private void splitTest(String src, String splitStr, String result){
        
        org.junit.Assert.assertEquals(result, Arrays.toString(StringSplitUtils.split(src, splitStr)));
    }
    
    private void splitByWholeSeparatorTest(String src, String splitStr, String result){
        
        org.junit.Assert.assertEquals(result, Arrays.toString(StringSplitUtils.splitByWholeSeparator(src, splitStr)));
    
    }
    
    private void splitTestTrim(String src, String splitStr, String result){
        
        org.junit.Assert.assertEquals(result, Arrays.toString(StringSplitUtils.split(src, splitStr, true)));
    }
    
    private void splitByWholeSeparatorTestTrim(String src, String splitStr, String result){
        
        org.junit.Assert.assertEquals(result, Arrays.toString(StringSplitUtils.splitByWholeSeparator(src, splitStr, true)));
    
    }
    
    private void splitTest2(String src, String splitStr, String result){
        
        org.junit.Assert.assertEquals(result, Arrays.toString(StringSplitUtils.splitNolastEmpty(src, splitStr)));
    }
    
    private void splitByWholeSeparatorTest2(String src, String splitStr, String result){
        
        org.junit.Assert.assertEquals(result, Arrays.toString(StringSplitUtils.splitByWholeNolastEmpty(src, splitStr)));
    
    }
    
    private void splitNoEmptyTest(String src, String splitStr, String result){
        
        org.junit.Assert.assertEquals(result, Arrays.toString(StringSplitUtils.splitIgnoreEmpty(src, splitStr)));
    }
    
    private void splitByWholeSeparatorNoEmptyTest(String src, String splitStr, String result){
        
        org.junit.Assert.assertEquals(result, Arrays.toString(StringSplitUtils.splitByWholeIgnoreEmpty(src, splitStr)));
    
    }

}
