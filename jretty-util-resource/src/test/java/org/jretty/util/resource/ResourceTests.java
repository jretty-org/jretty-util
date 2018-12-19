package org.jretty.util.resource;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;

import org.junit.Ignore;
import org.junit.Test;
import org.jretty.util.IOUtils;

/**
 * @author Juergen Hoeller
 * @author Chris Beams
 * @since 09.09.2004
 */
public class ResourceTests {

    @Test
    public void testByteArrayResource() throws IOException {
        Resource resource = new ByteArrayResource("testString".getBytes());
        assertTrue(resource.exists());
        assertFalse(resource.isOpen());
        String content = IOUtils.copyToString(new InputStreamReader(resource.getInputStream()));
        assertEquals("testString", content);
        assertEquals(resource, new ByteArrayResource("testString".getBytes()));
    }

    @Test
    public void testByteArrayResourceWithDescription() throws IOException {
        Resource resource = new ByteArrayResource("testString".getBytes(), "my description");
        assertTrue(resource.exists());
        assertFalse(resource.isOpen());
        
        String content = IOUtils.copyToString(new InputStreamReader(resource.getInputStream()));
        assertEquals("testString", content);
        assertEquals("my description", resource.getDescription());
        assertEquals(resource, new ByteArrayResource("testString".getBytes()));
    }

    @Test
    public void testInputStreamResource() throws IOException {
        InputStream is = new ByteArrayInputStream("testString".getBytes());
        Resource resource = new InputStreamResource(is);
        assertTrue(resource.exists());
        assertTrue(resource.isOpen());
        String content = IOUtils.copyToString(new InputStreamReader(resource.getInputStream()));
        assertEquals("testString", content);
        assertEquals(resource, new InputStreamResource(is));
    }

    @Test
    public void testInputStreamResourceWithDescription() throws IOException {
        InputStream is = new ByteArrayInputStream("testString".getBytes());
        Resource resource = new InputStreamResource(is, "my description");
        assertTrue(resource.exists());
        assertTrue(resource.isOpen());
        String content = IOUtils.copyToString(new InputStreamReader(resource.getInputStream()));
        assertEquals("testString", content);
        assertEquals("my description", resource.getDescription());
        assertEquals(resource, new InputStreamResource(is));
    }

    @Test
    public void testClassPathResource() throws IOException {
        Resource resource = new ClassPathResource("org/jretty/util/resource/Resource.class");
        doTestResource(resource);
        Resource resource2 = new ClassPathResource("org/jretty/util/resource/../resource/./Resource.class");
        assertEquals(resource, resource2);
        Resource resource3 = new ClassPathResource("org/jretty/util/resource/").createRelative("../resource/./Resource.class");
        assertEquals(resource, resource3);

        // Check whether equal/hashCode works in a HashSet.
        HashSet<Resource> resources = new HashSet<Resource>();
        resources.add(resource);
        resources.add(resource2);
        assertEquals(1, resources.size());
    }

    @Test
    public void testClassPathResourceWithClassLoader() throws IOException {
        Resource resource =
                new ClassPathResource("org/jretty/util/resource/Resource.class", getClass().getClassLoader());
        doTestResource(resource);
        assertEquals(resource,
                new ClassPathResource("org/jretty/util/resource/../resource/./Resource.class", getClass().getClassLoader()));
    }

    @Test
    public void testClassPathResourceWithClass() throws IOException {
        Resource resource = new ClassPathResource("Resource.class", getClass());
        doTestResource(resource);
        assertEquals(resource, new ClassPathResource("Resource.class", getClass()));
    }

    @Ignore // passes under eclipse, fails under ant
    @Test
    public void testFileSystemResource() throws IOException {
        Resource resource = new FileSystemResource(getClass().getResource("Resource.class").getFile());
        doTestResource(resource);
        assertEquals(new FileSystemResource(getClass().getResource("Resource.class").getFile()), resource);
        Resource resource2 = new FileSystemResource("resource/Resource.class");
        assertEquals(resource2, new FileSystemResource("resource/../resource/./Resource.class"));
    }

    @Test
    public void testUrlResource() throws IOException {
        Resource resource = new UrlResource(getClass().getResource("Resource.class"));
        doTestResource(resource);
        assertEquals(new UrlResource(getClass().getResource("Resource.class")), resource);
        Resource resource2 = new UrlResource("file:resource/Resource.class");
        assertEquals(resource2, new UrlResource("file:resource/../resource/./Resource.class"));
    }

    private void doTestResource(Resource resource) throws IOException {
        assertEquals("Resource.class", resource.getFilename());
        assertTrue(resource.getURL().getFile().endsWith("Resource.class"));

        Resource relative1 = resource.createRelative("ClassPathResource.class");
        assertEquals("ClassPathResource.class", relative1.getFilename());
        System.out.println(relative1);
        assertTrue(relative1.getURL().getFile().endsWith("ClassPathResource.class"));
        assertTrue(relative1.exists());

        Resource relative2 = resource.createRelative("support/ResourcePatternResolver.class");
        assertEquals("ResourcePatternResolver.class", relative2.getFilename());
        assertTrue(relative2.getURL().getFile().endsWith("ResourcePatternResolver.class"));
        assertTrue(relative2.exists());

        /*
        Resource relative3 = resource.createRelative("../SpringVersion.class");
        assertEquals("SpringVersion.class", relative3.getFilename());
        assertTrue(relative3.getURL().getFile().endsWith("SpringVersion.class"));
        assertTrue(relative3.exists());
        */
    }

    @Test
    public void testClassPathResourceWithRelativePath() throws IOException {
        Resource resource = new ClassPathResource("dir/");
        Resource relative = resource.createRelative("subdir");
        assertEquals(new ClassPathResource("dir/subdir"), relative);
    }

    @Test
    public void testFileSystemResourceWithRelativePath() throws IOException {
        Resource resource = new FileSystemResource("dir/");
        Resource relative = resource.createRelative("subdir");
        assertEquals(new FileSystemResource("dir/subdir"), relative);
    }

    @Test
    public void testUrlResourceWithRelativePath() throws IOException {
        Resource resource = new UrlResource("file:dir/");
        Resource relative = resource.createRelative("subdir");
        assertEquals(new UrlResource("file:dir/subdir"), relative);
    }

    @Ignore 
    @Test // this test is quite slow. TODO: re-enable with JUnit categories
    public void testNonFileResourceExists() throws Exception {
        Resource resource = new UrlResource("http://www.zollty.com");
        assertTrue(resource.exists());
    }

    @Test
    public void testAbstractResourceExceptions() throws Exception {
        final String name = "test-resource";

        Resource resource = new AbstractResource() {
            @Override
            public String getDescription() {
                return name;
            }
            @Override
            public InputStream getInputStream() {
                return null;
            }
        };

        try {
            resource.getURL();
            fail("FileNotFoundException should have been thrown");
        }
        catch (FileNotFoundException ex) {
            assertTrue(ex.getMessage().indexOf(name) != -1);
        }
        try {
            resource.getFile();
            fail("FileNotFoundException should have been thrown");
        }
        catch (FileNotFoundException ex) {
            assertTrue(ex.getMessage().indexOf(name) != -1);
        }
        try {
            resource.createRelative("/testing");
            fail("FileNotFoundException should have been thrown");
        }
        catch (FileNotFoundException ex) {
            assertTrue(ex.getMessage().indexOf(name) != -1);
        }

        assertThat(resource.getFilename(), nullValue());
    }

    @Test
    public void testContentLength() throws IOException {
        AbstractResource resource = new AbstractResource() {
            @Override
            public InputStream getInputStream() throws IOException {
                return new ByteArrayInputStream(new byte[] { 'a', 'b', 'c' });
            }
            @Override
            public String getDescription() {
                return null;
            }
        };
        assertThat(resource.contentLength(), is(3L));
    }

    @Test(expected=IllegalStateException.class)
    public void testContentLength_withNullInputStream() throws IOException {
        AbstractResource resource = new AbstractResource() {
            @Override
            public InputStream getInputStream() throws IOException {
                return null;
            }
            @Override
            public String getDescription() {
                return null;
            }
        };
        resource.contentLength();
    }

}
