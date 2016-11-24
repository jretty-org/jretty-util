package org.jretty.util.resource;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.jretty.log.LogFactory;
import org.jretty.log.Logger;
import org.jretty.util.resource.support.PathMatchingResourcePatternResolver;

public class DefaultResourceLoaderTest {
    
    private static final Logger LOG = LogFactory.getLogger();
    
    private ResourceLoader dloader = new DefaultResourceLoader();
    private ResourceLoader ploader = new PathMatchingResourcePatternResolver();
    
    @Test
    @Ignore
    public void testGetResource() {
        
        String path = "C:\\Windows\\system\\SHELL.DLL";
        String path2 = "file:C:\\Windows\\system\\SHELL.DLL";
        
        Resource resource = dloader.getResource(path);
        Assert.assertFalse(resource.exists());
        
        resource = ploader.getResource(path);
        Assert.assertFalse(resource.exists());
        
        resource = dloader.getResource(path2);
        Assert.assertTrue(resource.exists());
        
        // 不存在，但是不会报错。
        String path3 = "file0:C:\\Windows\\system\\SHELL.DLL";
        resource = dloader.getResource(path3);
        Assert.assertFalse(resource.exists());
        try {
            resource.getFile();
            Assert.fail();
        }
        catch (IOException e) {
            LOG.info(e);
        }
    }

}