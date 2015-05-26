package org.zollty.util;

import java.util.Arrays;

import org.junit.Test;

public class StringSplitUtilsTest {
    
    @Test
    public void test(){

      // System.out.println();
      // System.out.println("\"ab   de fg\", null");
      splitTest("ab   de fg", null, "[ab, , , de, fg]");
      splitByWholeSeparatorTest("ab   de fg", null, "[ab, , , de, fg]");
      
      // System.out.println();
      // System.out.println("\"ab  KKK\\t de fg \\n\\r LLLL\", null");
      splitTest("ab  KKK\t de fg \n\r LLLL", null, "[ab, , KKK, , de, fg, , , , LLLL]");
      splitByWholeSeparatorTest("ab  KKK\t de fg \n\r LLLL", null, "[ab, , KKK, , de, fg, , , , LLLL]");
      
      // System.out.println();
      // System.out.println("\"00000\\n00000\", \"\\n\"");
      splitTest("00000\n00000", "\n", "[00000, 00000]");
      splitByWholeSeparatorTest("00000\n00000", "\n", "[00000, 00000]");
      
      
      // System.out.println();
      // System.out.println("\"/app/opt///was/\", \"/\"");
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
      
      
      // System.out.println();
      // System.out.println("\"app/opt///was\", \"/\"");
      splitTest("app/opt///was", "/", "[app, opt, , , was]");
      splitByWholeSeparatorTest("app/opt///was", "/", "[app, opt, , , was]");
      
      // System.out.println();
      // System.out.println("\"app/opt///was\", \"//\"");
      splitTest("app/opt///was", "//", "[app, opt, , , was]");
      splitByWholeSeparatorTest("app/opt///was", "//", "[app/opt, /was]");
      
      // System.out.println();
      // System.out.println("\"app/opt  was\", \"/\"");
      splitTest("app/opt  was", "/", "[app, opt  was]");
      splitByWholeSeparatorTest("app/opt  was", "/", "[app, opt  was]");
      
      // System.out.println();
      // System.out.println("\"app-/-opt-/-was\", \"-/-\"");
      splitTest("app-/-opt-/-was", "-/-", "[app, , , opt, , , was]");
      splitByWholeSeparatorTest("app-/-opt-/-was", "-/-", "[app, opt, was]");
      
      // System.out.println();
      // System.out.println("\"app-/-opt-/--/-was\", \"-/--/-\"");
      splitTest("app-/-opt-/--/-was", "-/--/-", "[app, , , opt, , , , , , was]");
      splitByWholeSeparatorTest("app-/-opt-/--/-was", "-/--/-", "[app-/-opt, was]");
      
      // System.out.println();
      // System.out.println("\"app-KKKKKK/-opt-/--/-was\", \"-/--/-\"");
      splitTest("app-KKKKKK/-opt-/--/-was", "-/--/-", "[app, KKKKKK, , opt, , , , , , was]");
      splitByWholeSeparatorTest("app-KKKKKK/-opt-/--/-was", "-/--/-", "[app-KKKKKK/-opt, was]");
      
    }
    
    private void splitTest(String src, String splitStr, String result){
        
        org.junit.Assert.assertEquals(result, Arrays.toString(StringSplitUtils.split(src, splitStr)));
    }
    
    private void splitByWholeSeparatorTest(String src, String splitStr, String result){
        
        org.junit.Assert.assertEquals(result, Arrays.toString(StringSplitUtils.splitByWholeSeparator(src, splitStr)));
    
    }
    
    private void splitTest2(String src, String splitStr, String result){
        
        org.junit.Assert.assertEquals(result, Arrays.toString(StringSplitUtils.splitNolastEmpty(src, splitStr)));
    }
    
    private void splitByWholeSeparatorTest2(String src, String splitStr, String result){
        
        org.junit.Assert.assertEquals(result, Arrays.toString(StringSplitUtils.splitByWholeSeparatorNolastEmpty(src, splitStr)));
    
    }
    
    private void splitNoEmptyTest(String src, String splitStr, String result){
        
        org.junit.Assert.assertEquals(result, Arrays.toString(StringSplitUtils.splitIgnoreEmpty(src, splitStr)));
    }
    
    private void splitByWholeSeparatorNoEmptyTest(String src, String splitStr, String result){
        
        org.junit.Assert.assertEquals(result, Arrays.toString(StringSplitUtils.splitByWholeSeparatorIgnoreEmpty(src, splitStr)));
    
    }

}
