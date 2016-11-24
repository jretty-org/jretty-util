package org.jretty.util;

import org.junit.Assert;
import org.junit.Test;

public class EqualsBuilderTest {

    @Test
    public void test() {

        double[] d1 = new double[] { 0.000001, 3.1215926 };

        double[] d2 = new double[] { 0.000001, 3.1215926 };

        boolean ret = new EqualsBuilder().append(d1, d2).isEquals();

        Assert.assertTrue(ret);

        // 多一个0： 0.0000
        d2 = new double[] { 0.0000001, 3.1215926 };

        ret = new EqualsBuilder().append(d1, d2).isEquals();

        Assert.assertFalse(ret);

        d2 = null;

        ret = new EqualsBuilder().append(d1, d2).isEquals();

        Assert.assertFalse(ret);
        
        
        d1 = null;

        ret = new EqualsBuilder().append(d1, d2).isEquals();

        Assert.assertTrue(ret);

    }

}
