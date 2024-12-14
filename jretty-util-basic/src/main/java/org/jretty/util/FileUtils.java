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
 * Create by ZollTy on 2013-6-23 (http://blog.zollty.com/, zollty@163.com)
 */
package org.jretty.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.jretty.log.LogFactory;
import org.jretty.log.Logger;

/**
 * file utils best practice 
 * [常用的高性能文件工具类]
 * 
 * @author zollty
 * @since 2013-6-23
 */
public class FileUtils {

    private static final Logger LOG = LogFactory.getLogger(FileUtils.class);

    public static final char SEPARATOR = '/';
    
    /**
     * The file copy buffer size (30 MB)
     */
    private static final long FILE_COPY_BUFFER_SIZE = 1024 * 1024 * 30;
    
    
    /**
     * get BufferedWriter output stream
     * 
     * @param fileFullPath the absolute file path
     * @param append true if can append
     * @param charSet assign charSet,null if use the default charSet
     * @return BufferedWriter
     * @throws IOException
     */
    public static BufferedWriter getBufferedWriter(String fileFullPath, boolean append,
            String charSet) throws IOException {

        return new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileFullPath, append),
                StringUtils.decideCharSet(charSet)));
    }
    
    /**
     * get BufferedReader input stream
     * 
     * @param fileFullPath the absolute file path
     * @param charSet assign charSet,null if use the default charSet
     * @return BufferedReader
     * @throws IOException
     */
    public final static BufferedReader getBufferedReader(String fileFullPath, String charSet)
            throws IOException {

        return new BufferedReader(new InputStreamReader(new FileInputStream(fileFullPath),
                StringUtils.decideCharSet(charSet)));
    }
    
    /**
     * 将字符串写入文件
     * 
     * @param fileFullPath 文件全路径（务必保证外层目录存在）
     * @param str 字符内容
     * @param charSet null默认为UTP-8
     * @param append 是否为追加
     */
    public static void writeStr2File(String fileFullPath, String str, String charSet,
            boolean append) {
        BufferedWriter out = null;
        try {
            out = getBufferedWriter(fileFullPath, append, charSet);
            out.write(str);
            out.flush();
        } catch (Exception e) {
            LOG.error(e);
        } finally {
            IOUtils.closeIO(out);
        }
    }
    

    /**
     * deletes file or folder with all subfolders and subfiles.
     * 
     * @param file [file or directory to delete]
     * @return true [if all files are deleted]
     */
    public static boolean deleteAll(final File file) {
        if (file.isDirectory()) {
            for (File subFile : file.listFiles()) {
                if (!deleteAll(subFile)) {
                    return false;
                }
            }
        }
        return file.delete();
    }
    
    public static boolean deleteAll(final String fullPath) {
        final File file = new File(fullPath);
        if (file.isDirectory()) {
            for (File subFile : file.listFiles()) {
                if (!deleteAll(subFile)) {
                    return false;
                }
            }
        }
        return file.delete();
    }
    
    /**
     * 递归创建文件的父目录
     */
    public static void forceMkdirParent(final File file) throws IOException {
        final File parent = file.getParentFile();
        if (parent == null) {
            return;
        }
        forceMkdir(parent);
    }
    
    /**
     * 递归创建目录
     */
    public static void forceMkdir(final File directory) throws IOException {
        if (directory.exists()) {
            if (!directory.isDirectory()) {
                final String message =
                        "File "
                                + directory
                                + " exists and is "
                                + "not a directory. Unable to create directory.";
                throw new IOException(message);
            }
        } else {
            if (!directory.mkdirs()) {
                // Double-check that some other thread or process hasn't made
                // the directory in the background
                if (!directory.isDirectory()) {
                    final String message =
                            "Unable to create directory " + directory;
                    throw new IOException(message);
                }
            }
        }
    }
    
    /**
     * 复制整个文件夹内容
     * 
     * @param oldPath 原文件路径 如：c:/fqf
     * @param newPath 复制后路径 如：f:/fqf/ff
     * @return boolean
     */
    public static boolean copyFolder(String oldPath, String newPath) {
        return copyFolder(oldPath, newPath, true);
    }

    /**
     * 复制整个文件夹内容
     * 
     * @param oldPath 原文件路径 如：c:/fqf
     * @param newPath 复制后路径 如：f:/fqf/ff
     * @return true if all success
     */
    public static boolean copyFolder(String oldPath, String newPath,
            final boolean discardFileDate) {
        File oldFile = new File(oldPath);
        if (oldFile.isFile()) {
            try {
                if(discardFileDate) {
                    cloneFile(oldFile, new File(newPath));
                } else {
                    doCopyFile(oldFile, new File(newPath), true);
                }
                return true;
            } catch (IOException e) {
                if (LOG.isInfoEnabled()) {
                    LOG.warn(e, "cloneFile error. filePath=" 
                            + oldPath + ", newPath=" + newPath);
                }
                return false;
            }
        }
        String[] files = oldFile.list();
        if (null == files) {
            if (LOG.isInfoEnabled()) {
                LOG.warn("#copyFolder() - dir is empty, no file to copy. [oldPath={}]", oldPath);
            }
            return true;
        }
        File nfile = new File(newPath);
        if (!(nfile.exists() || nfile.mkdirs())) { // 如果文件夹不存在 则建立新文件夹
            if (LOG.isInfoEnabled()) {
                LOG.error("#copyFolder() - can't mk dir - [newPath={}]", newPath);
            }
            return false;
        }
        else if (!nfile.isDirectory()) {
            if (LOG.isInfoEnabled()) {
                LOG.error("#copyFolder() - newPath is not a directory - [newPath={}]", newPath);
            }
            return false;
        }
        File sourceFile;
        String fileName;
        boolean ret = true;
        for (int i = 0; i < files.length; i++) {
            fileName = files[i];
            sourceFile = new File(oldPath + SEPARATOR + fileName);
            if (sourceFile.isFile()) {
                try {
                    if(discardFileDate) {
                        cloneFile(sourceFile, new File(newPath + SEPARATOR + fileName));
                    } else {
                        doCopyFile(sourceFile, new File(newPath + SEPARATOR + fileName), true);
                    }
                }
                catch (IOException e) {
                    if (LOG.isInfoEnabled()) {
                        LOG.warn(e, "cloneFile error. filePath=" 
                                + oldPath + SEPARATOR + fileName);
                    }
                    ret = false;
                }
            }
            else if (sourceFile.isDirectory()) {
                // 如果是子文件夹
                copyFolder(oldPath + SEPARATOR + fileName, newPath + SEPARATOR + fileName, discardFileDate);
            }
        }
        return ret;
    }

    public static void cloneFile(final File fileIn, final File fileOut) throws IOException {
        long len = fileIn.length();
        FileInputStream in = null;
        FileOutputStream out = null;
        try {
            in = new FileInputStream(fileIn);
            out = new FileOutputStream(fileOut);
            IOUtils.clone(in, len, out);
        } finally {
            IOUtils.closeIO(in, out);
        }
    }
    
    public static void cloneFile(final File srcFile, final File destFile, final boolean preserveFileDate)
            throws IOException {
        if (srcFile.isDirectory()) {
            throw new IOException("Source '" + srcFile + "' exists but is a directory");
        }
        final File parentFile = destFile.getParentFile();
        if (parentFile != null) {
            if (!parentFile.mkdirs() && !parentFile.isDirectory()) {
                throw new IOException("Destination '" + parentFile + "' directory cannot be created");
            }
        }
        if (destFile.exists() && destFile.canWrite() == false) {
            throw new IOException("Destination '" + destFile + "' exists but is read-only");
        }
        doCopyFile(srcFile, destFile, preserveFileDate);
    }
    
    //-----------------------------------------------------------------------
    /**
     * Copies a file to a directory preserving the file date.
     * <p>
     * This method copies the contents of the specified source file
     * to a file of the same name in the specified destination directory.
     * The destination directory is created if it does not exist.
     * If the destination file exists, then this method will overwrite it.
     * <p>
     * <strong>Note:</strong> This method tries to preserve the file's last
     * modified date/times using {@link File#setLastModified(long)}, however
     * it is not guaranteed that the operation will succeed.
     * If the modification operation fails, no indication is provided.
     *
     * @param srcFile an existing file to copy, must not be {@code null}
     * @param destDir the directory to place the copy in, must not be {@code null}
     *
     * @throws NullPointerException if source or destination is null
     * @throws IOException          if source or destination is invalid
     * @throws IOException          if an IO error occurs during copying
     * @see #copyFile(File, File, boolean)
     */
    public static void copyFileToDirectory(final File srcFile, final File destDir) throws IOException {
        copyFileToDirectory(srcFile, destDir, true);
    }

    /**
     * Copies a file to a directory optionally preserving the file date.
     * <p>
     * This method copies the contents of the specified source file
     * to a file of the same name in the specified destination directory.
     * The destination directory is created if it does not exist.
     * If the destination file exists, then this method will overwrite it.
     * <p>
     * <strong>Note:</strong> Setting <code>preserveFileDate</code> to
     * {@code true} tries to preserve the file's last modified
     * date/times using {@link File#setLastModified(long)}, however it is
     * not guaranteed that the operation will succeed.
     * If the modification operation fails, no indication is provided.
     *
     * @param srcFile          an existing file to copy, must not be {@code null}
     * @param destDir          the directory to place the copy in, must not be {@code null}
     * @param preserveFileDate true if the file date of the copy
     *                         should be the same as the original
     *
     * @throws NullPointerException if source or destination is {@code null}
     * @throws IOException          if source or destination is invalid
     * @throws IOException          if an IO error occurs during copying
     * @throws IOException          if the output file length is not the same as the input file length after the copy
     * completes
     * @see #copyFile(File, File, boolean)
     */
    public static void copyFileToDirectory(final File srcFile, final File destDir, final boolean preserveFileDate)
            throws IOException {
        if (destDir == null) {
            throw new NullPointerException("Destination must not be null");
        }
        if (destDir.exists() && destDir.isDirectory() == false) {
            throw new IllegalArgumentException("Destination '" + destDir + "' is not a directory");
        }
        if (srcFile.isDirectory()) {
            throw new IOException("Source '" + srcFile + "' exists but is a directory");
        }
        final File destFile = new File(destDir, srcFile.getName());
        doCopyFile(srcFile, destFile, preserveFileDate);
    }
    
    /**
     * Internal copy file method.
     * This caches the original file length, and throws an IOException
     * if the output file length is different from the current input file length.
     * So it may fail if the file changes size.
     * It may also fail with "IllegalArgumentException: Negative size" if the input file is truncated part way
     * through copying the data and the new file size is less than the current position.
     *
     * @param srcFile          the validated source file, must not be {@code null}
     * @param destFile         the validated destination file, must not be {@code null}
     * @param preserveFileDate whether to preserve the file date
     * @throws IOException     if an error occurs
     * @throws IOException     if the output file length is not the same as the input file length after the
     * copy completes
     * @throws IllegalArgumentException "Negative size" if the file is truncated so that the size is less than the
     * position
     */
    private static void doCopyFile(final File srcFile, final File destFile, final boolean preserveFileDate)
            throws IOException {
        try (FileInputStream fis = new FileInputStream(srcFile);
             FileChannel input = fis.getChannel();
             FileOutputStream fos = new FileOutputStream(destFile);
             FileChannel output = fos.getChannel()) {
            final long size = input.size(); // TODO See IO-386
            long pos = 0;
            long count = 0;
            while (pos < size) {
                final long remain = size - pos;
                count = remain > FILE_COPY_BUFFER_SIZE ? FILE_COPY_BUFFER_SIZE : remain;
                final long bytesCopied = output.transferFrom(input, pos, count);
                if (bytesCopied == 0) { // IO-385 - can happen if file is truncated after caching the size
                    break; // ensure we don't loop forever
                }
                pos += bytesCopied;
            }
        }

        final long srcLen = srcFile.length(); // TODO See IO-386
        final long dstLen = destFile.length(); // TODO See IO-386
        if (srcLen != dstLen) {
            throw new IOException("Failed to copy full contents from '" +
                    srcFile + "' to '" + destFile + "' Expected length: " + srcLen + " Actual: " + dstLen);
        }
        if (preserveFileDate) {
            destFile.setLastModified(srcFile.lastModified());
        }
    }
    
    /**
     * 获取 目录以及子目录中的所有无子目录的目录文件（也就是说不包含目录的父级目录）
     * @param file 最外层的目录
     */
    public static List<File> loopFolders(File file) {
        return loopFolders(file, false);
    }
    
    /**
     * 获取 目录以及子目录中的所有（无子目录的）目录文件
     * @param file 最外层的目录
     * @param includeInterFolder 是否包含中间目录，如果为false则获取“所有无子目录的目录文件“
     */
    public static List<File> loopFolders(File file, boolean includeInterFolder) {
        ArrayList<File> result = new ArrayList<File>();
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files == null) {
                LOG.warn("cannot read dir " + file.getAbsolutePath());
                return result;
            }
            boolean finalDir = true;
            for (File filePath : files) {
                if (filePath.isFile()) {
                    continue;
                }
                finalDir = false;
                result.addAll(loopFolders(filePath, includeInterFolder));
            }
            if (includeInterFolder || finalDir) {
                result.add(file);
            }
        }
        return result;
    }
    
    /**
     * 获取 目录以及子目录中的所有文件
     */
    public static List<File> loopFiles(File file) {
        return loopFiles(file, null);
    }

    /**
     * 获取 目录以及子目录中的所有文件
     * @param inludeType 文件结尾类型（endsWith），比如 jpg, .jpg 都可以
     */
    public static List<File> loopFiles(File file, String[] inludeType) {
        ArrayList<File> result = new ArrayList<File>();
        if (file.isFile() && checkFileType(file, inludeType)) {
            result.add(file);
        }
        else if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files == null) {
                LOG.warn("cannot read dir " + file.getAbsolutePath());
                return result;
            }
            for (File filePath : files) {
                result.addAll(loopFiles(filePath, inludeType));
            }
        }
        return result;
    }
    
    /** 递归查找 目录以及子目录中 名为@fileName的文件 */
    public static File findFile(File folder, String fileName) {
        if (folder.isFile()) {
            return folder.getName().equalsIgnoreCase(fileName) ? folder : null;
        } else if (folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files == null) {
                LOG.warn("cannot read dir " + folder.getAbsolutePath());
                return null;
            }
            File ret = null;
            for (File filePath : files) {
                ret = findFile(filePath, fileName);
                if (ret != null) {
                    return ret;
                }
            }
            return ret;
        } else {
            return null;
        }
    }
    
    /**
     * 递归删除parent目录下面的所有空目录
     */
    public static boolean deleteEmptyDir(File parent) {
        if (parent.isDirectory()) {
            File[] files = parent.listFiles();
            if (files == null) {
                LOG.warn("cannot read dir " + parent.getAbsolutePath());
                return false;
            }
            if (files.length == 0) {
                return true;
            }
            boolean ret = true;
            for (File filePath : files) {
                if (filePath.isFile()) {
                    ret = false;
                    continue;
                }
                if (deleteEmptyDir(filePath)) {
                    filePath.delete();
                } else {
                    ret = false;
                }
            }
            return ret;
        }
        return false;
    }
    
    /**
     * 解析文本内容
     * @deprecated use getTextContent(in, null)
     */
    public static String getTextContent(InputStream in) {
        return getTextContent(in, null);
    }
    
    /**
     * 按行解析文本文件
     * @deprecated use getTextFileContent(in, null)
     */
    public static List<String> getTextFileContent(InputStream in) {
        return getTextFileContent(in, null);
    }

    /**
     * 解析文本内容
     */
    public static String getTextContent(InputStream in, String charSet) {
        List<String> ret = getTextFileContent(in, charSet);
        return CollectionUtils.toString(ret, Const.CRLF);
    }
    
    /**
     * 解析文本内容
     */
    public static String getTextContent(String fileFullPath, String charSet) {
        List<String> ret = getTextFileContent(fileFullPath, charSet);
        return CollectionUtils.toString(ret, Const.CRLF);
    }
    
    /**
     * 解析文本内容
     */
    public static String getTextContent(java.io.File file, String charSet) {
        try {
            return getTextContent(new FileInputStream(file), charSet);
        } catch (FileNotFoundException e) {
            throw new NestedRuntimeException(e);
        }
    }
    
    @SuppressWarnings("unchecked")
    public static List<String> getTextFileContent(String fileFullPath, String charSet) {
        InputStream in;
        try {
            in = new FileInputStream(fileFullPath);
        }
        catch (FileNotFoundException e) {
            return Collections.EMPTY_LIST;
        }
        return getTextFileContent(in, charSet);
    }
    
    /**
     * 按行解析文本文件
     */
    public static List<String> getTextFileContent(InputStream in, String charSet) {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(in, StringUtils.decideCharSet(charSet)));
            String buf = null;
            List<String> ret = new ArrayList<String>();
            while (null != (buf = br.readLine())) {
                ret.add(buf);
            }
            return ret;
        }
        catch (Exception e) {
            throw new NestedRuntimeException(e);
        }
        finally {
            IOUtils.closeIO(br);
        }
    }
    
    /**
     * 按行解析文本文件
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> parseTextFile(String fileFullPath, TextFileParse<T> parser, String charSet) {
        InputStream in;
        try {
            in = new FileInputStream(fileFullPath);
        }
        catch (FileNotFoundException e) {
            return Collections.EMPTY_LIST;
        }
        return parseTextFile(in, parser, charSet);
    }

    /**
     * 按行解析文本文件
     */
    public static <T> List<T> parseTextFile(InputStream in, TextFileParse<T> parser, String charSet) {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(in, StringUtils.decideCharSet(charSet)));
            T o;
            String lineStr;
            List<T> list = new ArrayList<T>();
            while ((lineStr = br.readLine()) != null) {
                if (StringUtils.isNotEmpty(lineStr)) {
                    o = parser.parseOneLine(lineStr);
                    if (o != null) {
                        list.add(o);
                    }
                }
            }
            return list;
        }
        catch (Exception e) {
            LOG.error(e);
            return null;
        }
        finally {
            IOUtils.closeIO(in);
            IOUtils.closeIO(br);
        }
    }

    private static boolean checkFileType(File file, String[] inludeType) {
        if (inludeType == null) {
            return true;
        }
        String name = file.getName();
        for (String type : inludeType) {
            if (name.endsWith(type)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * 在Java的缓存目录（System.getProperty "java.io.tmpdir"）下创建一个新文件夹（如果已存在，则会清空）
     */
    public static File createTempDir(String folderName) {
        File tmpdir = new File(System.getProperty("java.io.tmpdir"));
        tmpdir = new File(tmpdir, folderName);
        if (tmpdir.exists()) {
            deleteAll(tmpdir);
        }
        tmpdir.mkdir();
        return tmpdir;
    }
    
    /**
     * 按文件修改日期降序排列【最新的排在最上面】
     */
    public static class FileModifiedTimeComparator implements Comparator<File>, Serializable {

        private static final long serialVersionUID = -5254632622733699354L;

        @Override
        public int compare(File f1, File f2) {
            // 降序
            return f1.lastModified() < f2.lastModified() ? 1 : -1;
        }
    }
    
    /**
     * 将文本文件内容解析、封装成相应的对象。
     * 
     * @author zollty
     * @date 2013-8-02
     */
    public interface TextFileParse<T> {

        T parseOneLine(String line);

    }

}