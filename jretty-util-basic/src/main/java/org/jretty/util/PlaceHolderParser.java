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

/**
 * e.g. ${catalina.base:-target}/logs == target/logs (when ${catalina.base} is not define)
 * 
 * @author zollty
 * @since 2019-11-05
 */
public class PlaceHolderParser {
    
    /** Prefix for system property placeholders: "${". */
    public static final String PLACEHOLDER_PREFIX = "${";

    /** Suffix for system property placeholders: "}". */
    public static final String PLACEHOLDER_SUFFIX = "}";
    
    private String placeholderPrefix;

    private String placeholderSuffix;
    
    public PlaceHolderParser() {
        this.placeholderPrefix = PLACEHOLDER_PREFIX;
        this.placeholderSuffix = PLACEHOLDER_SUFFIX;
    }
    
    public PlaceHolderParser(String placeholderPrefix, String placeholderSuffix) {
        this.placeholderPrefix = placeholderPrefix;
        this.placeholderSuffix = placeholderSuffix;
    }
    
    /**
     * Replaces all placeholders of format "xxx${name}yyy" with the value returned
     * from the supplied {@link PlaceholderResolver}.
     * @param str the value containing the placeholders to be replaced
     * @param placeholderResolver the {@code PlaceholderResolver} to use for replacement
     * @param ignoreUnresolvablePlaceholders indicates whether unresolvable placeholders should
     * be ignored ({@code true}) or cause an exception ({@code false})
     * @return the supplied value with placeholders replaced inline
     */
    public String resovlePlaceHolder(String str, PlaceholderResolver placeholderResolver, boolean ignoreUnresolvablePlaceholders) {
        int startIndex = str.indexOf(this.placeholderPrefix);
        int prefixLen = this.placeholderPrefix.length();
        int suffixLen = 1;
        if (startIndex == -1 || startIndex > str.length() - prefixLen - suffixLen) {
            return str;
        }

        StringBuilder result = new StringBuilder(str);
        while (startIndex != -1) {
            
            int endIndex = StringUtils.matchFromIndex(result, startIndex + prefixLen, this.placeholderSuffix);

            if (endIndex != -1) {
                String placeholder = result.substring(startIndex + prefixLen, endIndex);
                PRResult presult = placeholderResolver.resolvePlaceholder(placeholder);
                
                if (presult != null && presult.getPlaceholderValue() != null) {
                    String propVal = presult.getPlaceholderValue();
                    if (presult.isWrapWithHolder()) {
                        propVal = this.placeholderPrefix + propVal + this.placeholderSuffix;
                    }
                    result.replace(startIndex, endIndex + 1, propVal);
                    startIndex = result.indexOf(this.placeholderPrefix, startIndex + propVal.length());
                } else if(ignoreUnresolvablePlaceholders) { // ignoreUnresolvablePlaceholders
                    startIndex = result.indexOf(this.placeholderPrefix, endIndex + 1);
                } else {
                    throw new IllegalArgumentException("Could not resolve placeholder '" +
                            placeholder + "'" + " in value \"" + str + "\"");
                }
                
            }
            else {
                startIndex = -1;
            }
            
        }

        return result.toString();
    }
    
    /**
     * ignoreUnresolvablePlaceholders = false;
     * @see {@link #resovlePlaceHolder(String, PlaceholderResolver, boolean)}
     */
    public String resovlePlaceHolder(String str, PlaceholderResolver placeholderResolver) {
        return resovlePlaceHolder(str, placeholderResolver, false);
    }

    /**
     * Strategy interface used to resolve replacement values for placeholders contained in Strings.
     */
    public interface PlaceholderResolver {

        /**
         * Resolve the supplied placeholder name to the replacement value.
         * @param placeholderName the name of the placeholder to resolve
         * @return the replacement value, or {@code null} if no replacement is to be made
         */
        PRResult resolvePlaceholder(String placeholderName);
    }

    public static class PRResult {

        private boolean wrapWithHolder;
        
        private String placeholderValue;

        /**
         * @param wrapWithHolder
         * @param placeholderValue
         */
        public PRResult(String placeholderValue, boolean wrapWithHolder) {
            super();
            this.wrapWithHolder = wrapWithHolder;
            this.placeholderValue = placeholderValue;
        }
        
        public PRResult(String placeholderValue) {
            super();
            this.placeholderValue = placeholderValue;
        }

        /**
         * @return the wrapWithHolder
         */
        public boolean isWrapWithHolder() {
            return wrapWithHolder;
        }

        /**
         * @param wrapWithHolder the wrapWithHolder to set
         */
        public void setWrapWithHolder(boolean wrapWithHolder) {
            this.wrapWithHolder = wrapWithHolder;
        }

        /**
         * @return the placeholderValue
         */
        public String getPlaceholderValue() {
            return placeholderValue;
        }

        /**
         * @param placeholderValue the placeholderValue to set
         */
        public void setPlaceholderValue(String placeholderValue) {
            this.placeholderValue = placeholderValue;
        }
        
    }

    /**
     * @return the placeholderPrefix
     */
    public String getPlaceholderPrefix() {
        return placeholderPrefix;
    }

    /**
     * @param placeholderPrefix the placeholderPrefix to set
     */
    public void setPlaceholderPrefix(String placeholderPrefix) {
        this.placeholderPrefix = placeholderPrefix;
    }

    /**
     * @return the placeholderSuffix
     */
    public String getPlaceholderSuffix() {
        return placeholderSuffix;
    }

    /**
     * @param placeholderSuffix the placeholderSuffix to set
     */
    public void setPlaceholderSuffix(String placeholderSuffix) {
        this.placeholderSuffix = placeholderSuffix;
    }
    

}
