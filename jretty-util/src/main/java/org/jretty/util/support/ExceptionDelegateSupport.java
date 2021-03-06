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
 * Create by ZollTy on 2014-12-13 (http://blog.zollty.com/, zollty@163.com)
 */
package org.jretty.util.support;

/**
 * 支持Exception Delegate接口
 * 
 * @author zollty
 * @since 2014-12-13
 */
public interface ExceptionDelegateSupport {
    
    /**
     * 获取 Exception Delegate
     */
    NestedExceptionDelegate getDelegate();

}
