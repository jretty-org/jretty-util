/* 
 * Copyright (C) 2013-2014 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License").
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * Create by ZollTy on 2013-6-07 (http://blog.zollty.com/, zollty@163.com)
 */
package org.jretty.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.jretty.log.LogFactory;
import org.jretty.log.Logger;
import org.jretty.util.resource.ClassPathResource;
import org.jretty.util.resource.DefaultResourceLoader;
import org.jretty.util.resource.FileSystemResource;
import org.jretty.util.resource.Resource;
import org.jretty.util.resource.ResourceLoader;
import org.jretty.util.resource.UrlResource;
import org.jretty.util.resource.support.PathMatchingResourcePatternResolver;
import org.jretty.util.resource.support.ResourcePatternResolver;

/**
 * 资源文件工具类
 * 
 * @author zollty
 * @since 2013-6-07
 */
public class ResourceUtils {

    private static final Logger LOG = LogFactory.getLogger(ResourceUtils.class);
    
    /**
     * Pseudo URL prefix for loading from the ServletContext relative file path, e.g. "contextpath:WEB-INF/test.dat",
     * 
     * @see org.zollty.util.resource.web.ServletContextResource
     * @see javax.servlet.ServletContext
     */
    public static final String CONTEXTPATH_URL_PREFIX = "contextpath:";

    /**
     * Pseudo URL prefix for loading from local file system file path, e.g. file:C:/test.dat
     * 
     * @see org.zollty.util.resource.FileSystemResource
     * @see java.io.File
     */
    public static final String LOCAL_FILE_URL_PREFIX = "file:";

    /** Pseudo URL prefix for loading from the class path: "classpath:" */
    public static final String CLASSPATH_URL_PREFIX = "classpath:";

    /**
     * Pseudo URL prefix for all matching resources from the class path: "classpath*:" 
     * This differs from ResourceLoader's classpath URL prefix in that it retrieves all matching
     * resources for a given name (e.g. "/beans.xml"), for example in the root of all deployed JAR files.
     */
    public static final String CLASSPATH_ALL_URL_PREFIX = "classpath*:";

    
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓
    // 以下几个方法 针对于 Resource API
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛
    
    public static Resource getResource(String location) {
        return new DefaultResourceLoader(ClassUtils.getDefaultClassLoader()).getResource(location);
    }
    
    public static Resource getResource(ClassLoader classLoader, String location) {
        return new DefaultResourceLoader(classLoader).getResource(location);
    }
    
    public static Resource getResource(ResourceLoader resourceLoader, String location) {
        return resourceLoader.getResource(location);
    }
    
    public static FileSystemResource getFileSystemResource(String path) {
        Assert.notNull(path, "path must not be null");
        if (path.startsWith(LOCAL_FILE_URL_PREFIX)) {
            return new FileSystemResource(path.substring(LOCAL_FILE_URL_PREFIX.length()));
        }
        return new FileSystemResource(path);
    }

    public static ClassPathResource getClassPathResource(String path) {
        return getClassPathResource(path, ClassUtils.getDefaultClassLoader());
    }

    public static ClassPathResource getClassPathResource(String path, ClassLoader classLoader) {
        Assert.notNull(path, "path must not be null");
        if (path.startsWith(CLASSPATH_URL_PREFIX)) {
            return new ClassPathResource(path.substring(CLASSPATH_URL_PREFIX.length()), classLoader);
        }
        return new ClassPathResource(path);
    }

    public static UrlResource getUrlResource(String path) throws IOException {
        try {
            // Try to parse the path as a URL...
            URL url = new URL(path);
            return new UrlResource(url);
        }
        catch (MalformedURLException ex) {
            // No URL -> resolve as resource path.
            throw new FileNotFoundException(StringUtils.replaceParams("\"{}\" {}", path, ex.getMessage()));
        }
    }

    public static Resource[] getResourcesByPathMatchingResolver(String locationPattern) throws IOException {
        ResourcePatternResolver resPatternLoader = new PathMatchingResourcePatternResolver();
        return resPatternLoader.getResources(locationPattern);
    }

    public static Resource[] getResourcesByPathMatchingResolver(String locationPattern, ClassLoader classLoader) throws IOException {
        ResourcePatternResolver resPatternLoader = new PathMatchingResourcePatternResolver(classLoader);
        return resPatternLoader.getResources(locationPattern);
    }

    public static Resource[] getResourcesByPathMatchingResolver(String locationPattern, ResourceLoader resourceLoader) throws IOException {
        ResourcePatternResolver resPatternLoader = new PathMatchingResourcePatternResolver(resourceLoader);
        return resPatternLoader.getResources(locationPattern);
    }

    public static Resource[] getResources(String locationPattern, ResourcePatternResolver resPatternLoader) throws IOException {
        return resPatternLoader.getResources(locationPattern);
    }
    
    
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓
    // 以下几个方法 为获取指定类型的resource inputstream
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛
    
    /**
     * 取得class所在 jar包 中的资源 {注意jar必须在文件目录下，不能在war或ear包中}<BR>
     * 
     * @param jarClazz
     * @param resourcePath 资源文件的相对路径（比如 FS_RQ.xsd，比如 com/zollty/config/FS_RQ.xsd）
     * @return the InputStream (may be null)
     */
    public static InputStream getInputStreamFromJar(Class<?> jarClazz, String resourcePath) throws NestedCheckedException {
        URL url = getResourceFromJar(jarClazz, resourcePath);
        try {
            return url != null ? url.openStream() : null;
        }
        catch (IOException e) {
            throw new NestedCheckedException(e);
        }
    }
    
    /**
     * 取得clazz.getClassLoader()所在 ClassPath下的资源（非url.openStream()模式，支持动态更新）
     * 
     * @param classLoader
     * @param resourcePath 相对路径
     */
    public static InputStream getInputStreamFromClassPath(ClassLoader classLoader, String resourcePath) throws NestedCheckedException {
        String resourcePathNew = resourcePath;
        if (resourcePathNew.startsWith("/") || resourcePathNew.startsWith(File.separator)) {
            resourcePathNew = resourcePathNew.substring(1);
        }
        InputStream in = null;
        URL url = classLoader.getResource(resourcePathNew); // 必须是ClassLoader
        if (url == null) {
            throw new NestedCheckedException("Can't find [{}] under common ClassPath.", resourcePathNew);
        }
        try {
            String fileUrl = url.getPath();
            fileUrl = fileUrl.replaceAll("%20", " ");
            if (!fileUrl.contains("!")) { // 含!的在jar或者ZIP中
                in = new BufferedInputStream(new FileInputStream(fileUrl));
                return in;
            }
            else {
                // in = url.openStream();
                throw new NestedCheckedException("Can't find [{}] under common ClassPath. it's in [{}].", resourcePathNew, fileUrl);
            }
        }
        catch (Exception e) {
            throw new NestedCheckedException(e);
        }
    }


    /**
     * 采用Java ClassLoader自带的getResourceAsStream获取资源。 
     * （可以读取ClassLoader下classpath/jar/zip/war/ear中的资源） 
     * 但是不支持动态更新和加载，只在ClassLoader初始化时加载一遍，以后更改，不会再次加载
     * @deprecated 建议使用{@link #getResource(String)}替代
     * @param jarClazz
     * @param resourcePath 相对路径
     * @return the resource's InputStream
     * @throws NestedCheckedException if can't get resource's InputStream
     */
    public static InputStream getInputStreamFromClassLoader(Class<?> clazz, String resourcePath) throws NestedCheckedException {
        InputStream in = null;
        try {
            in = clazz.getClassLoader().getResourceAsStream(resourcePath);
        }
        catch (Exception e) {
            // ignore
        }
        if (in != null) {
            LOG.debug("Got [{}] through [clazz.getClassLoader().getResourceAsStream()]", resourcePath);
            return in;
        }
        else {
            try {
                in = ResourceUtils.class.getClassLoader().getResourceAsStream(resourcePath);
            }
            catch (Exception e) {
                // ignore
            }
        }
        if (in != null) {
            LOG.debug("Got [{}] through [ResourceUtils.class.getClassLoader().getResourceAsStream()]", resourcePath);
            return in;
        }
        else {
            try {
                in = Thread.currentThread().getContextClassLoader().getResourceAsStream(resourcePath);
            }
            catch (Exception e) {
                // ignore
            }
        }

        if (in != null) {
            LOG.debug("Got [{}] through [Thread.currentThread().getContextClassLoader().getResourceAsStream()]", resourcePath);
            return in;
        }
        else {
            throw new NestedCheckedException(
                    "Can't find [{}] through \n1.clazz.getClassLoader().getResourceAsStream() \n2.ResourceUtils.class.getClassLoader().getResourceAsStream() \n3.Thread.currentThread().getContextClassLoader().getResourceAsStream().",
                    resourcePath);
        }
    }
    
    /**
     * 取得class所在 jar包 中的资源
     * 
     * @param jarClazz
     * @param resourcePath 相对路径
     * @return Resource's URL (null if the resourcePath is not found)
     */
    public static URL getResourceFromJar(Class<?> jarClazz, String resourcePath) throws NestedCheckedException {
        String resourcePathNew = resourcePath;
        if (resourcePathNew.startsWith("/") || resourcePathNew.startsWith(File.separator)) {
            resourcePathNew = resourcePathNew.substring(1);
        }
        // 先寻找class所在的jar，如果jar不存在，则执行返回null
        URL classDir = jarClazz.getProtectionDomain().getCodeSource().getLocation();
        if (classDir.getPath().endsWith(File.separator)) {
            throw new NestedCheckedException("[{}] not in [{} jar file]. It seems under the classpath?", resourcePath, jarClazz.getName());
            // 不存在jar，直接返回null
            // return null;
            // return jarClazz.getResource(resource); 不存在jar文件，这个类就在ClassPath下面
        }
        // else 存在jar文件
        // String path = classDir.getPath();
        URL retURL = null;
        try { // 从jar中去寻找resource文件
            Enumeration<URL> res = jarClazz.getClassLoader().getResources(resourcePathNew);
            URL tempURL;
            while (res.hasMoreElements()) {
                tempURL = res.nextElement();
                // if (tempURL.getPath().startsWith(path)) {
                retURL = tempURL;
                LOG.debug("get [{}] under [{}]", resourcePath, tempURL.getPath());
                break;
                // }
            }
        }
        catch (IOException e) {
            throw new NestedCheckedException(e);
        }
        if (retURL == null) {
            throw new NestedCheckedException("[{}] not in [{} jar file].", resourcePath, jarClazz.getName());
        }
        return retURL;
    }
    
    
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓
    // 以下两个方法 针对于 Properties API
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛
    
    /**
     * 通过输入流获取Properties实例
     */
    public static Properties getProperties(InputStream in) {
        if (in == null) {
            throw new IllegalArgumentException("in==null");
        }
        Properties props = new Properties();
        try {
            props.load(in);
            return props;
        }
        catch (IOException e) {
            throw new NestedRuntimeException(e);
        }
        finally {
            IOUtils.closeIO(in);
        }
    }
    
    /**
     * 将Properties资源转换成Map类型
     * @deprecated use CollectionUtils instand of
     */
    @Deprecated
    public static Map<String, String> covertProperties2Map(Properties props) {
        if (props == null) {
            throw new IllegalArgumentException("props==null");
        }
        Set<Entry<Object, Object>> set = props.entrySet();
        Map<String, String> mymap = new HashMap<String, String>();
        for (Entry<Object, Object> oo : set) {
            mymap.put(oo.getKey().toString(), oo.getValue().toString());
        }
        return mymap;
    }


}
