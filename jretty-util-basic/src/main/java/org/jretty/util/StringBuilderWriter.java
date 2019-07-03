/* 
 * Copyright (C) 2019-2021 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License").
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 */
package org.jretty.util;

import java.io.Writer;

/**
 * 用于替代<code>java.io.StringWriter</code>，内部使用{@link StringBuilder}，
 * 无锁，非线程安全，但效率更高！
 * @author zollty 高效的算法保证
 * @since 2019-07-02
 */
public class StringBuilderWriter extends Writer {
    private StringBuilder mBuffer;

    public StringBuilderWriter() {
        mBuffer = new StringBuilder();
        lock = mBuffer;
    }

    public StringBuilderWriter(int size) {
        if (size < 0)
            throw new IllegalArgumentException("Negative buffer size");
        mBuffer = new StringBuilder(size);
        lock = mBuffer;
    }
    
    public StringBuilderWriter(StringBuilder builder) {
        this.mBuffer = builder != null ? builder : new StringBuilder();
        lock = mBuffer;
    }

    @Override
    public void write(int c) {
        mBuffer.append((char) c);
    }

    @Override
    public void write(char[] cs) {
        mBuffer.append(cs, 0, cs.length);
    }

    @Override
    public void write(char[] cs, int off, int len) {
        if ((off < 0) || (off > cs.length) || (len < 0) ||
                ((off + len) > cs.length) || ((off + len) < 0))
            throw new IndexOutOfBoundsException();

        if (len > 0)
            mBuffer.append(cs, off, len);
    }

    @Override
    public void write(String str) {
        mBuffer.append(str);
    }

    @Override
    public void write(String str, int off, int len) {
        mBuffer.append(str.substring(off, off + len));
    }

    @Override
    public Writer append(CharSequence csq) {
        if (csq == null)
            write("null");
        else
            write(csq.toString());
        return this;
    }

    @Override
    public Writer append(CharSequence csq, int start, int end) {
        CharSequence cs = (csq == null ? "null" : csq);
        write(cs.subSequence(start, end).toString());
        return this;
    }

    @Override
    public Writer append(char c) {
        mBuffer.append(c);
        return this;
    }

    @Override
    public void close() {
    }

    @Override
    public void flush() {
    }
    
    public StringBuilder getBuilder() {
        return mBuffer;
    }

    @Override
    public String toString() {
        return mBuffer.toString();
    }
}
