/*
 * Copyright 2002-2013 the original author or authors.
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

import java.io.IOException;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.servlet.ServletContext;

import org.zollty.log.LogFactory;
import org.zollty.log.Logger;
import org.zollty.util.UrlUtils;
import org.zollty.util.StringUtils;
import org.zollty.util.resource.Resource;
import org.zollty.util.resource.ResourceLoader;
import org.zollty.util.resource.UrlResource;
import org.zollty.util.resource.support.PathMatchingResourcePatternResolver;

/**
 * ServletContext-aware subclass of {@link PathMatchingResourcePatternResolver},
 * able to find matching resources below the web application root directory
 * via Servlet 2.3's {@code ServletContext.getResourcePaths}.
 * Falls back to the superclass' file system checking for other resources.
 *
 * @author Juergen Hoeller
 * @since 1.1.2
 */
public class ServletContextResourcePatternResolver extends PathMatchingResourcePatternResolver {

	private static final Logger logger = LogFactory.getLogger(ServletContextResourcePatternResolver.class);


	/**
	 * Create a new ServletContextResourcePatternResolver.
	 * @param servletContext the ServletContext to load resources with
	 * @see ServletContextResourceLoader#ServletContextResourceLoader(javax.servlet.ServletContext)
	 */
	public ServletContextResourcePatternResolver(ServletContext servletContext) {
		super(new ServletContextResourceLoader(servletContext));
	}
	
	public ServletContextResourcePatternResolver(ServletContext servletContext, ClassLoader classLoader) {
        super(new ServletContextResourceLoader(servletContext, classLoader));
    }

	/**
	 * Create a new ServletContextResourcePatternResolver.
	 * @param resourceLoader the ResourceLoader to load root directories and
	 * actual resources with
	 */
	public ServletContextResourcePatternResolver(ResourceLoader resourceLoader) {
		super(resourceLoader);
	}


	/**
	 * Overridden version which checks for ServletContextResource
	 * and uses {@code ServletContext.getResourcePaths} to find
	 * matching resources below the web application root directory.
	 * In case of other resources, delegates to the superclass version.
	 * @see #doRetrieveMatchingServletContextResources
	 * @see ServletContextResource
	 * @see javax.servlet.ServletContext#getResourcePaths
	 */
	@Override
	protected Set<Resource> doFindPathMatchingFileResources(Resource rootDirResource, String subPattern)
			throws IOException {

		if (rootDirResource instanceof ServletContextResource) {
			ServletContextResource scResource = (ServletContextResource) rootDirResource;
			ServletContext sc = scResource.getServletContext();
			String fullPattern = scResource.getPath() + subPattern;
			Set<Resource> result = new LinkedHashSet<Resource>(8);
			doRetrieveMatchingServletContextResources(sc, fullPattern, scResource.getPath(), result);
			return result;
		}
		else {
			return super.doFindPathMatchingFileResources(rootDirResource, subPattern);
		}
	}

	/**
	 * Recursively retrieve ServletContextResources that match the given pattern,
	 * adding them to the given result set.
	 * @param servletContext the ServletContext to work on
	 * @param fullPattern the pattern to match against,
	 * with preprended root directory path
	 * @param dir the current directory
	 * @param result the Set of matching Resources to add to
	 * @throws IOException if directory contents could not be retrieved
	 * @see ServletContextResource
	 * @see javax.servlet.ServletContext#getResourcePaths
	 */
	@SuppressWarnings("unchecked")
    protected void doRetrieveMatchingServletContextResources(
			ServletContext servletContext, String fullPattern, String dir, Set<Resource> result)
			throws IOException {

		Set<String> candidates = servletContext.getResourcePaths(dir);
		if (candidates != null) {
			boolean dirDepthNotFixed = fullPattern.contains("**");
			int jarFileSep = fullPattern.indexOf(UrlUtils.JAR_URL_SEPARATOR);
			String jarFilePath = null;
			String pathInJarFile = null;
			if (jarFileSep > 0 && jarFileSep + UrlUtils.JAR_URL_SEPARATOR.length() < fullPattern.length()) {
				jarFilePath = fullPattern.substring(0, jarFileSep);
				pathInJarFile = fullPattern.substring(jarFileSep + UrlUtils.JAR_URL_SEPARATOR.length());
			}
			for (String currPath : candidates) {
				if (!currPath.startsWith(dir)) {
					// Returned resource path does not start with relative directory:
					// assuming absolute path returned -> strip absolute path.
					int dirIndex = currPath.indexOf(dir);
					if (dirIndex != -1) {
						currPath = currPath.substring(dirIndex);
					}
				}
				if (currPath.endsWith("/") && (dirDepthNotFixed || StringUtils.countOccurrencesOf(currPath, "/") <=
						StringUtils.countOccurrencesOf(fullPattern, "/"))) {
					// Search subdirectories recursively: ServletContext.getResourcePaths
					// only returns entries for one directory level.
					doRetrieveMatchingServletContextResources(servletContext, fullPattern, currPath, result);
				}
				if (jarFilePath != null && getPathMatcher().match(jarFilePath, currPath)) {
					// Base pattern matches a jar file - search for matching entries within.
					String absoluteJarPath = servletContext.getRealPath(currPath);
					if (absoluteJarPath != null) {
						doRetrieveMatchingJarEntries(absoluteJarPath, pathInJarFile, result);
					}
				}
				if (getPathMatcher().match(fullPattern, currPath)) {
					result.add(new ServletContextResource(servletContext, currPath));
				}
			}
		}
	}

	/**
	 * Extract entries from the given jar by pattern.
	 * @param jarFilePath the path to the jar file
	 * @param entryPattern the pattern for jar entries to match
	 * @param result the Set of matching Resources to add to
	 */
	private void doRetrieveMatchingJarEntries(String jarFilePath, String entryPattern, Set<Resource> result) {
		if (logger.isDebugEnabled()) {
			logger.debug("Searching jar file [" + jarFilePath + "] for entries matching [" + entryPattern + "]");
		}
		try {
			JarFile jarFile = new JarFile(jarFilePath);
			for (Enumeration<JarEntry> entries = jarFile.entries(); entries.hasMoreElements();) {
				JarEntry entry = entries.nextElement();
				String entryPath = entry.getName();
				if (getPathMatcher().match(entryPattern, entryPath)) {
					result.add(new UrlResource(
							UrlUtils.URL_PROTOCOL_JAR,
							UrlUtils.FILE_URL_PREFIX + jarFilePath + UrlUtils.JAR_URL_SEPARATOR + entryPath));
				}
			}
		}
		catch (IOException ex) {
				logger.warn(ex, "Cannot search for matching resources in jar file [" + jarFilePath +
						"] because the jar cannot be opened through the file system");
		}
	}

}
