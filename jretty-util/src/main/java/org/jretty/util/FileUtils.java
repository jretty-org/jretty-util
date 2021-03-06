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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.jretty.log.LogFactory;
import org.jretty.log.Logger;

/**
 * file utils best practice 
 * {高效的常用文件工具类}
 * 
 * @author zollty
 * @since 2013-6-23
 */
public class FileUtils {

    private static final Logger LOG = LogFactory.getLogger(FileUtils.class);

    public static final char SEPARATOR = '/';

    /**
     * deletes file or folder with all subfolders and subfiles.
     * 
     * @param file
     *            [file or directory to delete]
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

    /**
     * 复制整个文件夹内容
     * 
     * @param oldPath
     *            原文件路径 如：c:/fqf
     * @param newPath
     *            复制后路径 如：f:/fqf/ff
     * @return boolean
     */
    public static boolean copyFolder(String oldPath, String newPath) {
        File oldFile = new File(oldPath);
        if (oldFile.isFile()) {
            try {
                cloneFile(oldFile, new File(newPath));
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
                    cloneFile(sourceFile, new File(newPath + SEPARATOR + fileName));
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
                copyFolder(oldPath + SEPARATOR + fileName, newPath + SEPARATOR + fileName);
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

    /** 获取 目录以及子目录中的所有文件 */
    public static List<File> loopFiles(File file) {
        return loopFiles(file, null);
    }

    /** 获取 目录以及子目录中的所有文件 */
    public static List<File> loopFiles(File file, String inludeType[]) {
        ArrayList<File> result = new ArrayList<File>();
        if (file.isFile() && checkFileType(file, inludeType)) {
            result.add(file);
        }
        else if (file.isDirectory()) {
            File[] files = file.listFiles();
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
    
    public static void appendStr2File(String fileFullPath, String str, String charSet) {
        BufferedWriter out = null;
        try {
            out = IOUtils.getBufferedWriter(fileFullPath, true, charSet);
            out.write(str);
            out.flush();
        }
        catch (Exception e) {
            LOG.error(e);
        }
        finally {
            IOUtils.closeIO(out);
        }
    }
    
    
    /**
     * 解析文本内容
     */
    public static String getTextContent(InputStream in) {
        return getTextContent(in, null);
    }
    
    /**
     * 按行解析文本文件
     */
    public static List<String> getTextFileContent(InputStream in) {
        return getTextFileContent(in, null);
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
     * 解析文本内容
     */
    public static String getTextContent(InputStream in, String charSet) {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(in, StringUtils.decideCharSet(charSet)));
            String buf = null;
            StringBuilder builder = new StringBuilder();
            while (null != (buf = br.readLine())) {
                builder.append(buf).append(Const.CRLF);
            }
            return builder.toString();
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
                    // 读出文件中一行的数据
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
        }
    }

    private static boolean checkFileType(File file, String inludeType[]) {
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