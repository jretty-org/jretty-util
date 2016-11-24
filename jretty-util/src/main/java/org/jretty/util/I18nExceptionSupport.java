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
 * Create by ZollTy on 2013-10-27 (http://blog.zollty.com/, zollty@163.com)
 */
package org.jretty.util;

/**
 * @author zollty
 * @since 2013-10-27
 */
public interface I18nExceptionSupport {

    /**
     * 获得错误代码
     */
    public String getErrorCode();

    /**
     * 获得错误信息参数（用以替换错误信息中的{}记号）
     */
    public String[] getParams();

}
