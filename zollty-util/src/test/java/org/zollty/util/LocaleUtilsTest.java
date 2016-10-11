package org.zollty.util;

import java.util.Locale;

import org.junit.Assert;
import org.junit.Test;

public class LocaleUtilsTest {

    @Test
    public void toLocaleTest() {

        String[] localStrArr = new String[] { 
                "en", 
                "de_DE", 
                "_GB", 
                "en_US_WIN", 
                "de__POSIX" };

        for (int i = 0; i < localStrArr.length; i++) {
            Locale locale = LocaleUtils.toLocale(localStrArr[i]);
            
            Assert.assertEquals(localStrArr[i], locale.toString());
        }

        // Some Special..
        Assert.assertEquals("de", LocaleUtils.toLocale("de__").toString());
        Assert.assertEquals("zh_CN_#HANS", LocaleUtils.toLocale("zh_CN_#Hans").toString());
    }

}