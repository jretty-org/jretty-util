/*
 * Copyright 2002-2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.zollty.util.resource.web;

import javax.servlet.ServletContext;

import org.zollty.util.ResourceUtils;
import org.zollty.util.resource.DefaultResourceLoader;
import org.zollty.util.resource.FileSystemResourceLoader;
import org.zollty.util.resource.Resource;


/**
 * ResourceLoader implementation that resolves paths as ServletContext
 * resources, for use outside a WebApplicationContext (for example,
 * in an HttpServletBean or GenericFilterBean subclass).
 *
 * <p>Within a WebApplicationContext, resource paths are automatically
 * resolved as ServletContext resources by the context implementation.
 *
 * @author Juergen Hoeller
 * @since 1.0.2
 * @see #getResourceByPath
 * @see ServletContextResource
 * @see org.springframework.web.context.WebApplicationContext
 * @see org.springframework.web.servlet.HttpServletBean
 * @see org.springframework.web.filter.GenericFilterBean
 */
public class ServletContextResourceLoader extends DefaultResourceLoader {

	private final ServletContext servletContext;


    /**
	 * Create a new ServletContextResourceLoader.
	 * @param servletContext the ServletContext to load resources with
	 */
	public ServletContextResourceLoader(ServletContext servletContext) {
		this.servletContext = servletContext;
	}
	
	/**
     * Create a new ServletContextResourceLoader.
     * @param servletContext the ServletContext to load resources with
     */
	// add by zollty 2015-05-13
    public ServletContextResourceLoader(ServletContext servletContext, ClassLoader classLoader) {
        super(classLoader);
        this.servletContext = servletContext;
    }

	/**
	 * This implementation supports file paths beneath the root of the web application.
	 * @see ServletContextResource
	 */
	@Override
	protected Resource getResourceByPath(String path) {
	    // Assert.notNull(servletContext, "Cannot get ServletContextResource (" + path +") since servletContext is null");
	    
	    // the following two 'if' add by zollty 2015-05-13
	    if( null == servletContext ) {
	        return FileSystemResourceLoader.getFsResource(path);
	    }
	    if (path.startsWith(ResourceUtils.CONTEXTPATH_URL_PREFIX)) {
            return new ServletContextResource(this.servletContext, path.substring(ResourceUtils.CONTEXTPATH_URL_PREFIX.length()));
        }
	    
		return new ServletContextResource(this.servletContext, path);
	}

}
