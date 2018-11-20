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
public class Tests2 {
    
//    @Test
    public void t0() {
        System.out.println(Str04.indexIgnoreCase("aaba\r\nabaa╋ふ▒㊧❀", "╋ふ▒㊧❀"));
    }
    
    @Test
    public void t1() {
        Assert.assertTrue(Str01.indexIgnoreCase("aabaabaa", "a") == 0);
        Assert.assertTrue(Str01.indexIgnoreCase("aabaabaa", "b") == 2);
        Assert.assertTrue(Str01.indexIgnoreCase("aabaabaa", "ab") == 1);
        Assert.assertTrue(Str01.indexIgnoreCase("aabaabaa", "A") == 0);
        Assert.assertTrue(Str01.indexIgnoreCase("aabaabaa", "B") == 2);
        Assert.assertTrue(Str01.indexIgnoreCase("aabaabaa", "AB") == 1);
        Assert.assertTrue(Str01.indexIgnoreCase("aaba\r\nabaa", "\r\n")==4);
        Assert.assertTrue(Str01.indexIgnoreCase("aaba\r\nabaa╋ふ▒㊧❀", "╋ふ▒㊧❀")==10);
    }
    
    @Test
    public void t2() {
        Assert.assertTrue(Str02.indexIgnoreCase("aabaabaa", "a") == 0);
        Assert.assertTrue(Str02.indexIgnoreCase("aabaabaa", "b") == 2);
        Assert.assertTrue(Str02.indexIgnoreCase("aabaabaa", "ab") == 1);
        Assert.assertTrue(Str02.indexIgnoreCase("aabaabaa", "A") == 0);
        Assert.assertTrue(Str02.indexIgnoreCase("aabaabaa", "B") == 2);
        Assert.assertTrue(Str02.indexIgnoreCase("aabaabaa", "AB") == 1);
        Assert.assertTrue(Str02.indexIgnoreCase("aaba\r\nabaa", "\r\n")==4);
        Assert.assertTrue(Str02.indexIgnoreCase("aaba\r\nabaa╋ふ▒㊧❀", "╋ふ▒㊧❀")==10);
    }
    
    @Test
    public void t3() {
        Assert.assertTrue(Str03.indexIgnoreCase("aabaabaa", "a") == 0);
        Assert.assertTrue(Str03.indexIgnoreCase("aabaabaa", "b") == 2);
        Assert.assertTrue(Str03.indexIgnoreCase("aabaabaa", "ab") == 1);
        Assert.assertTrue(Str03.indexIgnoreCase("aabaabaa", "A") == 0);
        Assert.assertTrue(Str03.indexIgnoreCase("aabaabaa", "B") == 2);
        Assert.assertTrue(Str03.indexIgnoreCase("aabaabaa", "AB") == 1);
        Assert.assertTrue(Str03.indexIgnoreCase("aaba\r\nabaa", "\r\n")==4);
        Assert.assertTrue(Str03.indexIgnoreCase("aaba\r\nabaa╋ふ▒㊧❀", "╋ふ▒㊧❀")==10);
    }

    @Test
    public void t4() {
        Assert.assertTrue(Str04.indexIgnoreCase("aabaabaa", "a") == 0);
        Assert.assertTrue(Str04.indexIgnoreCase("aabaabaa", "b") == 2);
        Assert.assertTrue(Str04.indexIgnoreCase("aabaabaa", "ab") == 1);
        Assert.assertTrue(Str04.indexIgnoreCase("aabaabaa", "A") == 0);
        Assert.assertTrue(Str04.indexIgnoreCase("aabaabaa", "B") == 2);
        Assert.assertTrue(Str04.indexIgnoreCase("aabaabaa", "AB") == 1);
        Assert.assertTrue(Str04.indexIgnoreCase("aaba\r\nabaa", "\r\n")==4);
        Assert.assertTrue(Str04.indexIgnoreCase("aaba\r\nabaa╋ふ▒㊧❀", "╋ふ▒㊧❀")==10);
    }

}