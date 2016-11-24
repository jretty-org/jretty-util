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
 * Create by ZollTy on 2013-6-15 (http://blog.zollty.com/, zollty@163.com)
 */
package org.jretty.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import org.jretty.log.LogFactory;
import org.jretty.log.Logger;

/**
 * IO utils best practice 
 * {高效的IO工具类}
 * 
 * @author zollty
 * @since 2013-6-15
 */
public class IOUtils {

    private static final Logger LOG = LogFactory.getLogger(IOUtils.class);

    /**
     * max read file buffer size. default 500k
     */
    private static final int MAX_BUFFER_SIZE = 512000;

    /**
     * min read file buffer size. default 1k
     */
    private static final int MIN_BUFFER_SIZE = 1024;

    /**
     * default read file buffer size. default 4k
     */
    private static final int DEFAULT_BUFFER_SIZE = 4096;

    /**
     * get BufferedWriter output stream
     * 
     * @param fileFullPath
     *            the absolute file path
     * @param append
     *            true if can append
     * @param charSet
     *            assign charSet,null if use the default charSet
     * @return BufferedWriter
     * @throws IOException
     */
    public static BufferedWriter getBufferedWriter(String fileFullPath, boolean append, String charSet) throws IOException {
        return new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileFullPath, append), StringUtils.decideCharSet(charSet)));
    }

    /**
     * get BufferedReader input stream
     * 
     * @param fileFullPath
     *            the absolute file path
     * @param charSet
     *            assign charSet,null if use the default charSet
     * @return BufferedReader
     * @throws IOException
     */
    public final static BufferedReader getBufferedReader(String fileFullPath, String charSet) throws IOException {
        return new BufferedReader(new InputStreamReader(new FileInputStream(fileFullPath), StringUtils.decideCharSet(charSet)));
    }
    
    
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓
    // String <--> Stream  相互转换
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛

    /**
     * 将字符串转换为 InputStream 输入流
     * 
     * @param charSet
     *            指定字符编码
     */
    public final static InputStream getInputStreamFromString(String str, String charSet) {
        if (str == null) {
            throw new IllegalArgumentException("str==null");
        }
        try {
            return new ByteArrayInputStream(str.getBytes(StringUtils.decideCharSet(charSet)));
        }
        catch (UnsupportedEncodingException e) {
            throw new BasicRuntimeException("UnsupportedEncodingException[String.getBytes()] charSet=" + charSet);
        }
    }

    /**
     * 将字符串转换为 Reader 输入流
     */
    public final static Reader getReaderFromString(String str) {
        if (str == null) {
            throw new IllegalArgumentException("str==null");
        }
        return new StringReader(str);
    }

    /**
     * Copy the contents of the given Reader into a String. Closes the reader when done.
     * 
     * @param in
     *            the reader to copy from
     * @return the String that has been copied to
     * @throws IOException
     *             in case of I/O errors
     */
    public static String copyToString(Reader in) throws IOException {
        StringWriter out = new StringWriter();
        clone(in, out);
        return out.toString();
    }
    

    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓
    // Clone or Copy  for STREAM
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛
    
    /**
     * @see #clone(InputStream, long, OutputStream)
     */
    public final static int clone(final InputStream in, final OutputStream out) throws IOException {
        return clone(in, DEFAULT_BUFFER_SIZE, out);
    }

    /**
     * Copy the contents of the given Reader to the given Writer. Closes both when done.
     * 
     * @param in
     *            the Reader to copy from
     * @param out
     *            the Writer to copy to
     * @return the number of characters copied
     * @throws IOException
     *             in case of I/O errors
     */
    public static int clone(Reader in, Writer out) throws IOException {
        Assert.notNull(in, "No Reader specified");
        Assert.notNull(out, "No Writer specified");
        try {
            int byteCount = 0;
            char[] buffer = new char[DEFAULT_BUFFER_SIZE];
            int bytesRead = -1;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
                byteCount += bytesRead;
            }
            return byteCount;
        } finally {
            try {
                in.close();
            }
            catch (IOException ex) {
            }
            try {
                out.flush();
            }
            catch (IOException e) {
            }
            try {
                out.close();
            }
            catch (IOException ex) {
            }
        }
    }

    /**
     * @param len
     *            in-source-length e.g. long len = fileIn.length()
     */
    public final static int clone(final InputStream in, long len, final OutputStream out) throws IOException {
        Assert.notNull(in, "No InputStream specified");
        Assert.notNull(out, "No OutputStream specified");
        try {
            byte[] buf;
            // 动态缓存大小
            // case1 LEN>200000kb(195M) -- BUF=500kb e.g. 200M--500k
            // case2 400kb< LEN <200000kb -- BUF=LEN/400 e.g. 100M--250k, 10M--25k, 400kb--1kb
            // case3 LEN<400kb -- BUF=1kb e.g. 300kb--1kb, 0kb-1kb
            if (len > MAX_BUFFER_SIZE * 400) {
                buf = new byte[MAX_BUFFER_SIZE];
            }
            else if (len > MIN_BUFFER_SIZE * 400) {
                buf = new byte[(int) len / 400];
            }
            else {
                buf = new byte[DEFAULT_BUFFER_SIZE];
            }

            int byteCount = 0;
            int bytesRead = 0;
            while (-1 != (bytesRead = in.read(buf))) {
                out.write(buf, 0, bytesRead);
                byteCount += bytesRead;
            }
            return byteCount;
        } finally {
            try {
                in.close();
            }
            catch (IOException ex) {
            }
            try {
                out.flush();
            }
            catch (IOException e) {
            }
            try {
                out.close();
            }
            catch (IOException ex) {
            }
        }
    }

    
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓
    // close IO 静默关闭IO流
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛
	
    /**
     * 静默关闭输出流
     */
    public final static void closeIO(OutputStream out) {
        if (null != out) {
            try {
                out.flush();
            }
            catch (IOException e) {
                if (LOG.isDebugEnabled()) {
                    LOG.warn("flush error", e);
                }
            }
            try {
                out.close();
            }
            catch (Exception e) {
                if (LOG.isDebugEnabled()) {
                    LOG.warn("close error", e);
                }
            }
        }
    }

    public final static void closeIO(Writer out) {
        if (null != out) {
            try {
                out.flush();
            }
            catch (IOException e) {
                if (LOG.isDebugEnabled()) {
                    LOG.warn("flush error", e);
                }
            }
            try {
                out.close();
            }
            catch (Exception e) {
                if (LOG.isDebugEnabled()) {
                    LOG.warn("close error", e);
                }
            }
        }
    }

    /**
     * 静默关闭输入流
     */
    public final static void closeIO(InputStream in) {
        if (null != in) {
            try {
                in.close();
            }
            catch (Exception e) {
                if (LOG.isDebugEnabled()) {
                    LOG.warn("close error", e);
                }
            }
        }
    }

    public final static void closeIO(Reader in) {
        if (null != in) {
            try {
                in.close();
            }
            catch (Exception e) {
                if (LOG.isDebugEnabled()) {
                    LOG.warn("close error", e);
                }
            }
        }
    }

    public final static void close(Closeable clo) {
        if (clo != null) {
            try {
                clo.close();
            }
            catch (Exception e) {
                if (LOG.isDebugEnabled()) {
                    LOG.warn("close error", e);
                }
            }
        }
    }

    public final static void closeIO(Reader in, Writer out) {
        closeIO(in);
        closeIO(out);
    }

    public final static void closeIO(InputStream in, OutputStream out) {
        closeIO(in);
        closeIO(out);
    }

}
