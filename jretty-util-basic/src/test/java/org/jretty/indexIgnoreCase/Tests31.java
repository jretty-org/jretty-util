/**
 * 
 */
package org.jretty.indexIgnoreCase;

import org.junit.Assert;
import org.junit.Test;

/**
 * 
 * @author zollty
 * @since 2018年5月29日
 */
public class Tests31 {
    
//    @Test
    public void t0() {
        System.out.println(Str031.lastIndexIgnoreCase("aabaabaa", "a"));
        System.out.println(Str031.lastIndexIgnoreCase("aabaabaa", "b"));
        System.out.println(Str031.lastIndexIgnoreCase("aabaabaa", "ab"));
        System.out.println(Str031.lastIndexIgnoreCase("aabaabaa", "A"));
        System.out.println(Str031.lastIndexIgnoreCase("aabaabaa", "B"));
        System.out.println(Str031.lastIndexIgnoreCase("aabaabaa", "AB"));
        System.out.println(Str031.lastIndexIgnoreCase("aaba\r\nabaa", "\r\n"));
        System.out.println(Str031.lastIndexIgnoreCase("aaba\r\nabaa╋ふ▒㊧❀", "╋ふ▒㊧❀"));
    }
    
    @Test
    public void t2() {
        Assert.assertTrue(Str021.lastIndexIgnoreCase("aabaabaa", "a") == 7);
        Assert.assertTrue(Str021.lastIndexIgnoreCase("aabaabaa", "b") == 5);
        Assert.assertTrue(Str021.lastIndexIgnoreCase("aabaabaa", "ab") == 4);
        Assert.assertTrue(Str021.lastIndexIgnoreCase("aabaabaa", "A") == 7);
        Assert.assertTrue(Str021.lastIndexIgnoreCase("aabaabaa", "B") == 5);
        Assert.assertTrue(Str021.lastIndexIgnoreCase("aabaabaa", "AB") == 4);
        Assert.assertTrue(Str021.lastIndexIgnoreCase("aaba\r\nabaa", "\r\n")==4);
        Assert.assertTrue(Str021.lastIndexIgnoreCase("aaba\r\nabaa╋ふ▒㊧❀", "╋ふ▒㊧❀")==10);
    }
    
    @Test
    public void t3() {
        Assert.assertTrue(Str031.lastIndexIgnoreCase("aabaabaa", "a") == 7);
        Assert.assertTrue(Str031.lastIndexIgnoreCase("aabaabaa", "b") == 5);
        Assert.assertTrue(Str031.lastIndexIgnoreCase("aabaabaa", "ab") == 4);
        Assert.assertTrue(Str031.lastIndexIgnoreCase("aabaabaa", "A") == 7);
        Assert.assertTrue(Str031.lastIndexIgnoreCase("aabaabaa", "B") == 5);
        Assert.assertTrue(Str031.lastIndexIgnoreCase("aabaabaa", "AB") == 4);
        Assert.assertTrue(Str031.lastIndexIgnoreCase("aaba\r\nabaa", "\r\n")==4);
        Assert.assertTrue(Str031.lastIndexIgnoreCase("aaba\r\nabaa╋ふ▒㊧❀", "╋ふ▒㊧❀")==10);
    }

    @Test
    public void t4() {
        Assert.assertTrue(Str041.lastIndexIgnoreCase("aabaabaa", "a") == 7);
        Assert.assertTrue(Str041.lastIndexIgnoreCase("aabaabaa", "b") == 5);
        Assert.assertTrue(Str041.lastIndexIgnoreCase("aabaabaa", "ab") == 4);
        Assert.assertTrue(Str041.lastIndexIgnoreCase("aabaabaa", "A") == 7);
        Assert.assertTrue(Str041.lastIndexIgnoreCase("aabaabaa", "B") == 5);
        Assert.assertTrue(Str041.lastIndexIgnoreCase("aabaabaa", "AB") == 4);
        Assert.assertTrue(Str041.lastIndexIgnoreCase("aaba\r\nabaa", "\r\n")==4);
        Assert.assertTrue(Str041.lastIndexIgnoreCase("aaba\r\nabaa╋ふ▒㊧❀", "╋ふ▒㊧❀")==10);
    }

}