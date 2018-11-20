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
 * Create by ZollTy on 2014-5-17 (http://blog.zollty.com/, zollty@163.com)
 */
package org.jretty.util.json.support;

import org.jretty.util.json.DefaultWriter;

/**
 * 
 * @author zollty
 * @since 2014-5-17
 */
public class StringWriter implements ObjectWriter<String> {

    private static final String[] REPLACEMENT_CHARS;
    private static final String[] HTML_SAFE_REPLACEMENT_CHARS;
    static {
        REPLACEMENT_CHARS = new String[128];
        for (int i = 0; i <= 0x1f; i++) {
            REPLACEMENT_CHARS[i] = String.format("\\u%04x", (int) i);
        }
        REPLACEMENT_CHARS['"'] = "\\\"";
        REPLACEMENT_CHARS['\\'] = "\\\\";
        REPLACEMENT_CHARS['\t'] = "\\t";
        REPLACEMENT_CHARS['\b'] = "\\b";
        REPLACEMENT_CHARS['\n'] = "\\n";
        REPLACEMENT_CHARS['\r'] = "\\r";
        REPLACEMENT_CHARS['\f'] = "\\f";
        HTML_SAFE_REPLACEMENT_CHARS = REPLACEMENT_CHARS.clone();
        HTML_SAFE_REPLACEMENT_CHARS['<'] = "\\u003c";
        HTML_SAFE_REPLACEMENT_CHARS['>'] = "\\u003e";
        HTML_SAFE_REPLACEMENT_CHARS['&'] = "\\u0026";
        HTML_SAFE_REPLACEMENT_CHARS['='] = "\\u003d";
        HTML_SAFE_REPLACEMENT_CHARS['\''] = "\\u0027";
    }

    private boolean htmlSafe;

    public StringWriter() {

    }

    public StringWriter(boolean htmlSafe) {
        this.htmlSafe = htmlSafe;
    }

    @Override
    public void write(DefaultWriter out, String value) {

        String[] replacements = htmlSafe ? HTML_SAFE_REPLACEMENT_CHARS : REPLACEMENT_CHARS;
        out.write("\"");
        int last = 0;
        int length = value.length();
        for (int i = 0; i < length; i++) {
            char c = value.charAt(i);
            String replacement;
            if (c < 128) {
                replacement = replacements[c];
                if (replacement == null) {
                    continue;
                }
            }
            else if (c == '\u2028') {
                replacement = "\\u2028";
            }
            else if (c == '\u2029') {
                replacement = "\\u2029";
            }
            else {
                continue;
            }
            if (last < i) {
                out.write(String.valueOf(value.toCharArray(), last, i - last));
            }
            out.write(replacement);
            last = i + 1;
        }
        if (last < length) {
            out.write(String.valueOf(value.toCharArray(), last, length - last));
        }
        out.write("\"");
    }

    public boolean isHtmlSafe() {
        return htmlSafe;
    }

    public void setHtmlSafe(boolean htmlSafe) {
        this.htmlSafe = htmlSafe;
    }

}
