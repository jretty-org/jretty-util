/* 
 * Copyright (C) 2013-2020 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License").
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * Create by ZollTy on 2019-11-5 (http://blog.zollty.com/, zollty@163.com)
 */
package org.jretty.util;

import org.jretty.log.LogFactory;
import org.jretty.util.PlaceHolderParser.PRResult;
import org.jretty.util.PlaceHolderParser.PlaceholderResolver;

/**
 * e.g. ${catalina.base:-target}/logs == target/logs (when ${catalina.base} is not define)
 * 
 * @see PlaceHolderParser
 * 
 * @author zollty
 * @since 2019-11-05
 */
public class SystemPlaceHolderUtils {
    
    /** Prefix for system property placeholders: "${". */
    public static final String PLACEHOLDER_PREFIX = "${";

    /** Suffix for system property placeholders: "}". */
    public static final String PLACEHOLDER_SUFFIX = "}";

    /** Value separator for system property placeholders: ":". */
    public static final String DEFAULT_VALUE_FLAG = ":-";
    
    private static final PlaceHolderParser PLACEHOLDER_PARSER = new PlaceHolderParser(PLACEHOLDER_PREFIX, PLACEHOLDER_SUFFIX);
    
    /**
     * 获取系统参数、环境变量，先 System.getProperty(name) 然后 System.getenv(name)
     * @param key 变量名称
     * @return null或者对应值
     */
    public static String resolveSystemVal(String key) {
        try {
            String propVal = System.getProperty(key);
            if (propVal == null) {
                // Fall back to searching the system environment.
                propVal = System.getenv(key);
            }
            return propVal;
        }
        catch (Throwable ex) {
            LogFactory.getLogger(SystemPlaceHolderUtils.class).warn(ex, "cannot get system property or env.");
            return null;
        }
    }
    
    /** use system env */
    private static final PlaceholderResolver DEFAULT_RESOLVER = new PlaceholderResolver() {
        @Override
        public PRResult resolvePlaceholder(String placeholder) {
            int separatorIndex;
            if ((separatorIndex = placeholder.indexOf(DEFAULT_VALUE_FLAG)) == -1) {
                return new PRResult(resolveSystemVal(placeholder));
            } else {
                String actualPlaceholder = placeholder.substring(0, separatorIndex);
                String propVal = resolveSystemVal(actualPlaceholder);
                if (propVal == null) {
                    String defaultValue = placeholder.substring(separatorIndex + 2);
                    return new PRResult(defaultValue);
                }
                return new PRResult(propVal);
            }
        }
    };
    
    /**
     * resolve the string with placeholder using the SystemPropertyPlaceholderResolver,
     * if can not parse throw an exception
     */
    public static String resovlePlaceHolder(String str) {
        return PLACEHOLDER_PARSER.resovlePlaceHolder(str, DEFAULT_RESOLVER);
    }

    /**
     * @param ignoreUnresolvablePlaceholders indicates whether unresolvable placeholders should
     * be ignored ({@code true}) or cause an exception ({@code false})
     */
    public static String resovlePlaceHolder(String str, boolean ignoreUnresolvablePlaceholders) {
        return PLACEHOLDER_PARSER.resovlePlaceHolder(str, DEFAULT_RESOLVER, ignoreUnresolvablePlaceholders);
    }

}
