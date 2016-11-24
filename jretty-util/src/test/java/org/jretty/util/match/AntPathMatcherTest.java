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
 * Create by ZollTy on 2014-6-03 (http://blog.zollty.com/, zollty@163.com)
 */
package org.jretty.util.match;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author zollty
 * @since 2014-6-03
 */
public class AntPathMatcherTest {

    private static Case[] 测试用例 = new Case[] { 
            new Case("/*/a*.html", "/ssa/a.html", true),
            new Case("/s/*/*/", "/s/a/b/c", false), 
            new Case("/lesson*/**", "/lesson1/ss", true),
            new Case("/lesson?/**", "/lesson1/ss", true),
            // new Case("/*/*/*.html", "/aa/dsds/a.html"),
            // new Case("/s*-*/*/*", "/s{aa}-{cc}/{ccd}/{dd}"),
            // new Case("/a.html", "/a.html"),
            // new Case("**/csv/**", "sd/dsds/csv/saas/sads"),
            new Case("/webjars/**", "/webjars/bootstrap/3.1.0/css/bootstrap.min.css", true) };

    @Test
    public void main() {
        for (Case ca : 测试用例) {
            antMatch(ca);
        }
    }

    
    private static void antMatch(Case ca) {
        PathMatcher pa = new AntPathMatcher();

        Assert.assertEquals(ca.ret, pa.match(ca.pattern, ca.uri));

    }
    
    
    private static class Case {
        String pattern;
        String uri;
        boolean ret;

        public Case(String pattern, String uri, boolean ret) {
            super();
            this.pattern = pattern;
            this.uri = uri;
            this.ret = ret;
        }

        @Override
        public String toString() {
            return "pattern: " + pattern + ", uri: " + uri;
        }
    }

}
