/**
 * 
 */
package org.jretty.indexIgnoreCase;

import org.junit.Test;

/**
 * 
 * @author zollty
 * @since 2018年5月29日
 */
public class Str03Test {

    @Test
    public void t0() {
        System.out.println(Str03.indexIgnoreCase("", "xxx"));
        System.out.println(Str03.indexIgnoreCase("xxx", ""));
        System.out.println(Str03.indexIgnoreCase("", ""));
        
        System.out.println(Str03.indexIgnoreCase("", "xxx", 1));
        System.out.println(Str03.indexIgnoreCase("xxx", "", 1));
        System.out.println(Str03.indexIgnoreCase("", "", 1));
    }

}
