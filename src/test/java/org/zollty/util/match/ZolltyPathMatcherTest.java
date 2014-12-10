package org.zollty.util.match;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.zollty.log.LogFactory;
import org.zollty.log.Logger;

public class ZolltyPathMatcherTest {
    
private static Logger LOG = LogFactory.getLogger();
    
    //  测试用例
    private static List<TestData> forTest = new ArrayList<TestData>();
    
    private static List<String> p1 = new ArrayList<String>();
    private static List<String> p2 = new ArrayList<String>();
    private static List<Boolean> ret = new ArrayList<Boolean>();
    
    static {
        forTest.add(new TestData("/mlf4j/*/*/*", "/mlf4j/vf/logConfig/uu", "[vf, logConfig, uu]"));
        forTest.add(new TestData("*/ab/**/*", "P/ab/CC-TD/S", "[P, CC-TD, S]"));
        forTest.add(new TestData("**/ab/**/ffd/*/dff*ddf/*S", "s/s/ab/g/g/ffd/m/dffrddf/sS", "[s/s, g/g, m, r, s]"));
        forTest.add(new TestData("**/ab/**/ffd/*/dff*ddf/*S", "/ab/g/g/ffd/m/dffrddf/sS", "[, g/g, m, r, s]"));
        forTest.add(new TestData("*/ab/**/ffd/*/dff*ddf/*S", "h/ab/g/g/ffd/m/dffrddf/sS", "[h, g/g, m, r, s]"));
        // 5
        
        forTest.add(new TestData("*/ab/*-*/*", "P/ab/CC-TD/S", "[P, CC, TD, S]"));
        forTest.add(new TestData("**/ab/f/*/a", "/ab/f/CC/DD/a/ab/f/CC/a", "[/ab/f/CC/DD/a, CC]")); // 问题在于，多次匹配，忽略了第二个匹配项
        // 解决方案-向后匹配-适用于“**开头且非**结尾的字符串”
        forTest.add(new TestData("**/ab/f/*/a/**", "/ab/f/CC/DD/a/ab/f/BB/EE/a/ab/f/AA/a/ab/f/KK/EE/a/ab/f/HH/RR/a", "[/ab/f/CC/DD/a/ab/f/BB/EE/a, AA, ab/f/KK/EE/a/ab/f/HH/RR/a]"));
        forTest.add(new TestData("/ab/**/f/*/c/", "/ab/a/f/CC/f/M/c/", "[a/f/CC, M]")); // 中间通配符匹配问题
        // 终极解决方案： 向后"循环匹配"法
        forTest.add(new TestData("**/ab/**/f/*/c/**", "/ab/a/f/CC/f/M/c/", "[, a/f/CC, M, ]"));
        // 10
        
        forTest.add(new TestData("**/ab/**/f/*/c/**/g/*/h/**", "/ab/a/f/CC/f/M/c/g/h/f/M/c/k/g/m/h/", "[, a/f/CC, M, g/h/f/M/c/k, m, ]")); // 只回溯了/f/
        forTest.add(new TestData("**/a*c*d/", "/a/c/ac/d/acd/", "[/a/c/ac/d, , ]"));
        forTest.add(new TestData("**/a**c*d/", "/a/c/ac/d/cd/", "[, /c/ac/d/, ]")); //回溯了c
        forTest.add(new TestData("**m*n/**/e**c*d/", "/m/n/l/mn/l/e/c/ac/d/cd/", "[/m/n/l/, , l, /c/ac/d/, ]")); //回溯了m和c
        forTest.add(new TestData("*/ab/**/*/**", "P/ab/S//", "[P, S, , ]"));
        // 15
        
        forTest.add(new TestData("**/ab/**/ccc", "lov/cx/ab/f/h/ccc", "[lov/cx, f/h]"));
        
        forTest.add(new TestData("/a*c/**/b", "/a/c/CC-TD/b", "null"));
        String p1 = "/a/bc/k/k/b";
        forTest.add(new TestData("/a**c/*/*/b", p1, "[/b, k, k]"));
        forTest.add(new TestData("/a/*bc/**/b", p1, "[, k/k]"));
        
        
        // 以下是测试 ZolltyPathMatcher.isTwoPatternSimilar() 方法
        addPattern("/ac/**/b", "/ac/*/*/b", true);
        addPattern("/a*c/*/*/b", "/ac/*/*/b", true);
        addPattern("/ab/**/f/*/c/", "**/ab/**/f/*/c/**", true); // 例如 /ab/a/f/CC/f/M/c/
        
        // 兼容两根斜杠的情况
        addPattern("/a*c/*/*/b", "/abc/**/b", true);
        addPattern("/a*c/*/*/*/b", "/abc/**/b", false);
        
    }
    
    @Test
    public void main() {
        TestData testcase;
        String result;
        boolean fail = false;
        for(int i=0; i<forTest.size(); i++) {
            testcase = forTest.get(i);
            result = match(testcase.pattern, testcase.src);
            if(result==null){
                result = "null";
            }
            if(testcase.assume.equals(result)){
                LOG.info((i+1)+". Success. pattern="+testcase.pattern+", src="+testcase.src+", assume="+testcase.assume);
            } else {
                fail = true;
                LOG.error((i+1)+". Failure. pattern="+testcase.pattern+", src="+testcase.src+", assume="+testcase.assume+", real result="+result);
            }
        }
        
        if(fail)
            Assert.fail("ZolltyPathMatcher Test Fail!");
    }
    
    static String match(final String pattern, final String src){
        List<String> valueList = new ZolltyPathMatcher(pattern).match(src);
        if (valueList != null) {
            return valueList.toString();
        }
        return null;
    }
    
    static class TestData{
        String pattern; // 字符模板
        String src; // 实际字符
        String assume; // 预计正确匹配时的结果（list.toString()）
        public TestData(String pattern, String src, String assume) {
            super();
            this.pattern = pattern;
            this.src = src;
            this.assume = assume;
        }
    }
    
    
    private static void addPattern(String pattern1, String pattern2, boolean result){
        p1.add(pattern1);
        p2.add(pattern2);
        ret.add(result);
    }
    
    @Test
    public void testIsTwoPatternSimilar(){
        
        for(int i=0; i<p1.size(); i++){
            Assert.assertEquals(p1.get(i)+" is a duplicate of "+p2.get(i), ret.get(i), ZolltyPathMatcher.isTwoPatternSimilar(p1.get(i), p2.get(i)));
        }
        
    }

}
