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
 * Create by ZollTy on 2013-6-07 (http://blog.zollty.com/, zollty@163.com)
 */
package org.jretty.util.zip;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.jretty.log.LogFactory;
import org.jretty.log.Logger;
import org.jretty.util.Const;
import org.jretty.util.IOUtils;

/**
 * ZIP 压缩/解压 工具类
 * <br>
 * 参见 zipFile() unzipFile() compress() uncompress()
 * 
 * @author zollty 
 * @since 2013-6-07
 */
public class ZipUtils {

    private static final Logger LOG = LogFactory.getLogger(ZipUtils.class);

    /**
     * 压缩文件
     */
    public static void zipFile(File outputZipFile, File inputSourceFile) throws ZipException {
        zipFile(outputZipFile, inputSourceFile, null);
    }

    /**
     * 压缩文件
     */
    public static void zipFile(File outputZipFile, File inputSourceFile, String charSet) throws ZipException {
        ZipOutputStream out = null;
        try {
            if (null == charSet || charSet.length() == 0) {
                out = new ZipOutputStream(new FileOutputStream(outputZipFile));
            }
            else {
                out = new ZipOutputStream(new FileOutputStream(outputZipFile), charSet);
            }
            if (inputSourceFile.isDirectory()) {
                zip(out, inputSourceFile, "");
            }
            else {
                zip(out, inputSourceFile, inputSourceFile.getName());
            }
        }
        catch (Exception e) {
            throw new NestedZipException(e);
        } finally {
            IOUtils.closeIO(out);
        }
    }

    /**
     * 压缩文件
     */
    public static void zipFile(String fullOutputZipFileName, String fullInputFileOrFolderName) 
            throws ZipException {
        zipFile(fullOutputZipFileName, fullInputFileOrFolderName, null);
    }

    /**
     * 压缩文件
     */
    public static void zipFile(String fullOutputZipFileName, String fullInputFileOrFolderName, String charSet)
            throws ZipException {
        ZipOutputStream out = null;
        try {
            if (null == charSet || charSet.length() == 0) {
                out = new ZipOutputStream(new FileOutputStream(fullOutputZipFileName));
            }
            else {
                out = new ZipOutputStream(new FileOutputStream(fullOutputZipFileName), charSet);
            }
            File sourceFile = new File(fullInputFileOrFolderName);
            if (sourceFile.isDirectory()) {
                zip(out, sourceFile, "");
            }
            else {
                zip(out, sourceFile, sourceFile.getName());
            }
        }
        catch (Exception e) {
            throw new NestedZipException(e);
        } finally {
            IOUtils.closeIO(out);
        }
    }

    private static void zip(ZipOutputStream zipOut, File sourceFile, String itemName) throws IOException {
        if (sourceFile.isDirectory()) {
            File[] fl = sourceFile.listFiles();
            zipOut.putNextEntry(new ZipEntry(itemName + Const.FOLDER_SEPARATOR));
            try {
                for (int i = 0; i < fl.length; i++) {
                    if (itemName.length() > 0) {
                        zip(zipOut, fl[i], itemName + Const.FOLDER_SEPARATOR + fl[i].getName());
                    }
                    else {
                        zip(zipOut, fl[i], fl[i].getName());
                    }
                }
            }
            catch (IOException e) {
                if (LOG.isDebugEnabled()) {
                    LOG.warn(e);
                }
            }
            if (fl.length == 0) {
                zipOut.flush();
                zipOut.closeEntry();
            }
        }
        else {
            zipOut.putNextEntry(new ZipEntry(itemName));
            if (LOG.isTraceEnabled()) {
                LOG.trace(itemName);
            }
            FileInputStream in = null;
            try {
                in = new FileInputStream(sourceFile);
                IOUtils.clone(in, sourceFile.length(), zipOut);
            }
            catch (IOException e) {
                if (LOG.isDebugEnabled()) {
                    LOG.warn(e);
                }
            } finally {
                IOUtils.closeIO(in);
            }

            zipOut.flush();
            zipOut.closeEntry();
        }
    }

    /**
     * 解压ZIP文件
     */
    public static void unzipFile(String directory, String fullIutputZipFileName) throws ZipException {
        unzipFile(directory, fullIutputZipFileName, null);
    }

    /**
     * 解压ZIP文件
     */
    public static void unzipFile(String directory, String fullIutputZipFileName, String charSet) 
            throws ZipException {
        ZipInputStream in = null;
        try {
            if (null == charSet || charSet.length() == 0) {
                in = new ZipInputStream(new FileInputStream(fullIutputZipFileName));
            }
            else {
                in = new ZipInputStream(new FileInputStream(fullIutputZipFileName), charSet);
            }
            unzip(directory, in);
        }
        catch (Exception e) {
            throw new NestedZipException(e);
        } finally {
            IOUtils.closeIO(in);
        }
    }

    /**
     * 解压ZIP文件
     */
    public static void unzipFile(String directory, File zip) throws ZipException {
        unzipFile(directory, zip, null);
    }

    /**
     * 解压ZIP文件
     */
    public static void unzipFile(String directory, File zip, String charSet) throws ZipException {
        ZipInputStream in = null;
        try {
            if (null == charSet || charSet.length() == 0) {
                in = new ZipInputStream(new FileInputStream(zip));
            }
            else {
                in = new ZipInputStream(new FileInputStream(zip), charSet);
            }
            unzip(directory, in);
        }
        catch (Exception e) {
            throw new NestedZipException(e);
        } finally {
            IOUtils.closeIO(in);
        }
    }

    private static void unzip(String directory, ZipInputStream in) throws IOException {
        ZipEntry ze = null;
        File parent = new File(directory);

        if (!parent.exists() && !parent.mkdirs()) {
            throw new IOException("create directory \"" + parent.getAbsolutePath() + "\" failed.");
        }
        while ((ze = in.getNextEntry()) != null) {
            String name = ze.getName();
            if (LOG.isTraceEnabled()) {
                LOG.trace(name);
            }
            File child = new File(directory + Const.FOLDER_SEPARATOR + name);
            if (name.endsWith(Const.FOLDER_SEPARATOR)) { // directory name.lastIndexOf("/") == (name.length() - 1)
                if (!child.exists() && !child.mkdirs()) {
                    throw new IOException("create directory \"" + child.getAbsolutePath() + "\" failed.");
                }
                continue;
            }
            FileOutputStream output = null;
            try {
                output = new FileOutputStream(child);
                IOUtils.clone(in, child.length(), output);
            }
            catch (IOException e) {
                if (LOG.isDebugEnabled()) {
                    LOG.warn(e);
                }
            } finally {
                IOUtils.closeIO(output);
            }
        }
    }

    /**
     * 字符串的压缩
     * 
     * @param str
     *            待压缩的字符串
     * @return 返回压缩后的字符串，注意，编码为ISO_8859_1，不可直接使用
     * @author zollty
     */
    public static String compress(String str, String orgCharset) throws ZipException {
        if (null == str || str.length() == 0) {
            return str;
        }
        // 创建一个新的 byte 数组输出流
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        GZIPOutputStream gzipout = null;
        try {
            // 使用默认缓冲区大小创建新的输出流 ---512 byte
            gzipout = new GZIPOutputStream(out);
            // 写入此输出流
            if (null == orgCharset) {
                orgCharset = Const.UTF_8;
            }
            gzipout.write(str.getBytes(orgCharset));
        }
        catch (UnsupportedEncodingException e) {
            throw new ZipException("compress failed! UnsupportedEncodingException - " + orgCharset);
        }
        catch (IOException e) {
            throw new NestedZipException(e, "compress failed!");
        } finally {
            IOUtils.closeIO(gzipout);
        }
        try {
            // 使用指定的 charsetName，通过解码字节将缓冲区内容转换为字符串
            return out.toString(Const.ISO_8859_1); // 必须先关闭gzipout才能调用out.toString()
        }
        catch (UnsupportedEncodingException e) {
            throw new ZipException("UnsupportedEncodingException - ISO-8859-1");
        }
    }

    /**
     * 字符串的解压
     * 
     * @param str
     *            待解压的字符串，注意必须为ISO_8859_1编码
     * @param orgCharset
     *            原（未压缩的）字符串的编码格式，以便还原原字符串，如果不设置，将默认取当前JVM的编码
     * @return 返回解压缩后的字符串
     */
    public static String uncompress(String str, String orgCharset) throws ZipException {
        if (null == str || str.length() == 0) {
            return str;
        }
        byte buf[];
        try {
            buf = str.getBytes(Const.ISO_8859_1);
        }
        catch (UnsupportedEncodingException e) {
            throw new ZipException("UnsupportedEncodingException - ISO-8859-1");
        }

        // 创建一个 ByteArrayInputStream，使用 buf 作为其缓冲区数组
        ByteArrayInputStream in = new ByteArrayInputStream(buf);
        // 使用默认缓冲区大小创建新的输入流 ---512 byte
        GZIPInputStream gzipin = null;
        ByteArrayOutputStream out = null;
        try {
            gzipin = new GZIPInputStream(in);
            out = new ByteArrayOutputStream();
            byte[] buffer = new byte[256];
            int n = 0;
            while ((n = gzipin.read(buffer)) != -1) {// 将未压缩数据读入字节数组
                // 将指定 byte 数组中从偏移量 off 开始的 len 个字节写入此 byte数组输出流
                out.write(buffer, 0, n);
            }
        }
        catch (IOException e) {
            throw new NestedZipException(e, "Uncompress failed!");
        } finally {
            IOUtils.closeIO(gzipin);
            IOUtils.closeIO(out);
        }
        try {
            // 使用指定的 charsetName，通过解码字节将缓冲区内容转换为字符串
            return out.toString(orgCharset);// "GBK"
        }
        catch (UnsupportedEncodingException e) {
            throw new ZipException("Uncompress failed! UnsupportedEncodingException - " + orgCharset);
        }
    }

}
