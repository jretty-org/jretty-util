/* 
 * Copyright (C) 2015-2016 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License").
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * Create by ZollTy on 2015-3-17 (http://blog.zollty.com/, zollty@163.com)
 */
package org.zollty.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletContext;

import org.zollty.util.resource.Resource;
import org.zollty.util.resource.support.ResourcePatternResolver;
import org.zollty.util.resource.web.ServletContextResource;
import org.zollty.util.resource.web.ServletContextResourceLoader;
import org.zollty.util.resource.web.ServletContextResourcePatternResolver;

/**
 * @author zollty
 * @since 2015-3-17
 */
public class WebResourceUtils extends ResourceUtils {
    /**
     * 
     * Get Resource from the given path. using {@link ServletContextResourceLoader#getResource(String)}<br>
     * 
     * Support：<br>
     *  
     *  1. Standard URL ( @see java.net.URL)
     *   Defined by RFC 2396 (http://www.ietf.org/rfc/rfc2396.txt)
     *   <pre>
     *   Some examples: 
     *   
     *     http://www.math.com/faq.html
     *     https://www.math.com/faq.html
     *     ftp://ftp.is.co.za/rfc/rfc1808.txt
     *     file:C:/test.dat
     *     jar:dddddd
     *     netdoc:sdfffdfdffd
     *     mailto:mduerst@ifi.unizh.ch
     *     gopher://spinaltap.micro.com/Weather/California/Los%20Angeles
     *     支持以上8种协议。参见 JRE -> lib -> rt.jar -> \sun\net\www\protocol
     *     
     *     or a relative URI, like:
     *      file:C:/Windows/system/../system/test.dat
     *   </pre>
     *    
     *  2. Pseudo URL
     *   Defined by this Util, see 
     *   {@link ResourceUtils#CLASSPATH_URL_PREFIX}, 
     *   {@link ResourceUtils#CONTEXTPATH_URL_PREFIX}, 
     *   {@link ResourceUtils#LOCAL_FILE_URL_PREFIX}.
     *   <pre>
     *   Some examples: 
     *   
     *     classpath:test.dat
     *     contextpath:WEB-INF/test.dat
     *     file:C:/test.dat
     *     
     *   </pre>
     *   @see ServletContextResourceLoader#getResource(String)
     *   @see org.zollty.util.resource.DefaultResourceLoader#getResource(String)
     *   
     * @param path the path to resolve
     * @param classLoader the ClassLoader to load class path resources with.
     * @param servletContext the ServletContext to load resources with.
     * @return the corresponding Resource object
     */
    public static Resource getResource(String path, ClassLoader classLoader, ServletContext servletContext) {
        return new ServletContextResourceLoader(servletContext, classLoader).getResource(path);
    }
    
    
    public static ServletContextResource getServletContextResource(ServletContext servletContext, String path) {
        Assert.notNull(path, "path must not be null");
        if (path.startsWith(CONTEXTPATH_URL_PREFIX)) {
            return new ServletContextResource(servletContext, path.substring(CONTEXTPATH_URL_PREFIX.length()));
        }
        return new ServletContextResource(servletContext, path);
    }
    
    /**
     * Get Resources from the given locationPattern. using {@link ServletContextResourcePatternResolver#getResources(String) }
     * 
     * @param locationPattern the location pattern to resolve
     * @param classLoader the ClassLoader to load class path resources with.
     * @param servletContext the ServletContext to load resources with.
     * @return the corresponding Resource objects
     * @throws IOException in case of I/O errors
     */
    public static Resource[] getResources(String locationPattern, ClassLoader classLoader, ServletContext servletContext) throws IOException {
        ResourcePatternResolver resPatternLoader = new ServletContextResourcePatternResolver(servletContext, classLoader);
        return resPatternLoader.getResources(locationPattern);
    }
    
    public static Resource[] getResourcesByServletContextResolver(String locationPattern, ServletContext servletContext) throws IOException {
        ResourcePatternResolver resPatternLoader = new ServletContextResourcePatternResolver(servletContext);
        return resPatternLoader.getResources(locationPattern);
    }
    
    /**
     * See {@link #getResources(String, ClassLoader, ServletContext) }
     * @see #getResources(String, ClassLoader, ServletContext)
     * @see org.zollty.util.resource.DefaultResourceLoader#getResource(String)
     */
    public static InputStream getResourceInputStream(String resourceLocation, ClassLoader classLoader, ServletContext servletContext) throws IOException {
        Resource resource = getResource(resourceLocation, classLoader, servletContext);
        if( !resource.exists() ){
            throw new FileNotFoundException(resourceLocation + " by " + resource.getClass().getName());
        }
        return resource.getInputStream();
    }

}
