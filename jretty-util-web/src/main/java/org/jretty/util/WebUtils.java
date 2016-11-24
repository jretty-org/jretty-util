/* 
 * Copyright (C) 2014-2015 the original author or authors.
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
package org.jretty.util;

import java.io.FileNotFoundException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

/**
 * @author zollty
 * @since 2015-3-17
 */
public class WebUtils {
    
    /**
     * Return the real path of the given path within the web application,
     * as provided by the servlet container.
     * <p>Prepends a slash if the path does not already start with a slash,
     * and throws a FileNotFoundException if the path cannot be resolved to
     * a resource (in contrast to ServletContext's {@code getRealPath},
     * which returns null).
     * @param servletContext the servlet context of the web application
     * @param path the path within the web application
     * @return the corresponding real path
     * @throws FileNotFoundException if the path cannot be resolved to a resource
     * @see javax.servlet.ServletContext#getRealPath
     */
    public static String getRealPath(ServletContext servletContext, String path) throws FileNotFoundException {
        Assert.notNull(servletContext, "ServletContext must not be null");
        // Interpret location as relative to the web application root directory.
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        String realPath = servletContext.getRealPath(path);
        if (realPath == null) {
            throw new FileNotFoundException(
                    "ServletContext resource [" + path + "] cannot be resolved to absolute file path - " +
                    "web application archive not expanded?");
        }
        return realPath;
    }
    
    /**
     * 判断是否为Ajax类型的请求（支持jquery，其他方式未测试过）
     */
    public static boolean isAjaxRequest(HttpServletRequest request) {
        String header = request.getHeader("X-Requested-With");
        if (header != null && "XMLHttpRequest".equals(header)) {
            return true;
        }
        return false;
    }

}
