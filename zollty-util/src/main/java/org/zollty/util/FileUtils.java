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
package org.zollty.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.zollty.log.LogFactory;
import org.zollty.log.Logger;

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
	 * @param file  [file or directory to delete]
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
	 * @param oldPath 原文件路径 如：c:/fqf
	 * @param newPath 复制后路径 如：f:/fqf/ff
	 * @return boolean
	 */
    public static void copyFolder(String oldPath, String newPath) {
        File oldFile = new File(oldPath);
        String[] files = oldFile.list();
        if (null == files) {
            if (LOG.isInfoEnabled()) {
                LOG.error("#copyFolder() - file is null. [oldPath={}]", oldPath);
            }
            return;
        }
        File nfile = new File(newPath);
        if (!nfile.exists() && (new File(newPath)).mkdirs()) { // 如果文件夹不存在 则建立新文件夹
            if (LOG.isInfoEnabled()) {
                LOG.error("#copyFolder() - can't mk dir - [newPath={}]", newPath);
            }
            return;
        }
        else if (!nfile.isDirectory()) {
            if (LOG.isInfoEnabled()) {
                LOG.error("#copyFolder() - newPath is not a directory - [newPath={}]", newPath);
            }
            return;
        }
        File sourceFile;
        String fileName;
        for (int i = 0; i < files.length; i++) {
            fileName = files[i];
            sourceFile = new File(oldPath + SEPARATOR + fileName);
            if (sourceFile.isFile()) {
                try {
                    cloneFile(sourceFile, new File(newPath + SEPARATOR + fileName));
                } catch (IOException e) {
                    if (LOG.isInfoEnabled()) {
                        LOG.warn(e, "cloneFile error. [filePath={}]", oldPath + SEPARATOR + fileName);
                    }
                }
            } else if (sourceFile.isDirectory()) {// 如果是子文件夹
                copyFolder(oldPath + SEPARATOR + fileName, newPath + SEPARATOR + fileName);
            }
        }
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

    /** 获取 目录以及子目录中的所有文件 */
    public static List<File> loopFiles(File file) {
        return loopFiles(file, null);
    }

    /** 获取 目录以及子目录中的所有文件 */
    public static List<File> loopFiles(File file, String inludeType[]) {
        ArrayList<File> result = new ArrayList<File>();
        if (file.isFile() && checkFileType(file, inludeType)) {
            result.add(file);
        } else if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File filePath : files) {
                result.addAll(loopFiles(filePath, inludeType));
            }
        }
        return result;
    }

    private static boolean checkFileType(File file, String inludeType[]) {
        if (inludeType == null) {
            return true;
        }
        String name = file.getName();
        for (String type : inludeType) {
            if (name.endsWith(type))
                return true;
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
            if (f1.lastModified() < f2.lastModified()) { // 降序
                return 1;
            } else {
                return -1;
            }
        }
    }

}