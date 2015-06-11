/* 
 * Copyright (C) 2013-2015 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License").
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * Create by ZollTy on 2013-10-24 (http://blog.zollty.com/, zollty@163.com)
 */
package org.zollty.tool.web;

import javax.servlet.http.HttpServletRequest;

/**
 * 获取浏览器的 User-Agent
 * 
 * @author zollty
 * @since 2013-10-24
 */
public class UserAgentParser {

    private String userAgentString;
    private HttpServletRequest request;

    public UserAgentParser(HttpServletRequest request) {
        this.request = request;
        this.userAgentString = request.getHeader("User-Agent");
    }

    public UserAgentParser(String userAgentString) {
        this.userAgentString = userAgentString;
    }

    public boolean isMSIEBrowser() {
        boolean rtn = false;
        if (userAgentString != null && userAgentString.indexOf("MSIE") != -1) {
            rtn = true;
        }
        return rtn;
    }

    public String getUserAgentString() {
        return userAgentString;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

}