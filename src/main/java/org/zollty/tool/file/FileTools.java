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
 * Create by Zollty Tsou on 2013-06-02 [http://blog.zollty.com]
 */
package org.zollty.tool.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.zollty.log.LogFactory;
import org.zollty.log.Logger;
import org.zollty.util.IOUtils;
import org.zollty.util.StringUtils;
import org.zollty.util.match.PathMatcher;
import org.zollty.util.match.ZolltyPathMatcher;

/**
 * 本地文件常用工具类
 * 
 * @author zollty
 * @since 2013年6月2日
 */
public class FileTools {

    private static final Logger LOG = LogFactory.getLogger(FileTools.class);

    /**
     * 拷贝文件
     * 
     * @param fileIn 输入
     * @param fileOut 输出
     * @throws IOException IO异常
     */
    public static void cloneFile(final File fileIn, final File fileOut) throws IOException {
        long len = fileIn.length();
        if (len == 0) {
            return;
        }
        FileInputStream in = null;
        FileOutputStream out = null;
        try {
            in = new FileInputStream(fileIn);
            out = new FileOutputStream(fileOut);
            IOUtils.clone(in, len, out);
        }
        finally {
            IOUtils.closeIO(in, out);
        }
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
            LOG.error(e);
            return new ArrayList<String>();
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

    /**
     * 复制文件夹，默认会记录DEBUG级别的LOG
     * @param sourceDir 来源文件夹
     * @param targetDir 目标文件夹
     */
    public static void copyDirectory(String sourceDir, String targetDir) {
        copyDirectory(sourceDir, targetDir, null, true, true);
    }

    /**
     * 复制文件夹，根据patterns参数进行过滤(isDiscard=true时)或者包含(isDiscard=false时) （默认会记录DEBUG级别的LOG）
     * @param sourceDir 来源文件夹
     * @param targetDir 目标文件夹
     * @param patterns 匹配模式字符串 @see org.zollty.util.match.ZolltyPathMatcher
     * @param isDiscard 等于true时为“排除模式”，为false则为“包含模式” 
     */
    public static void copyDirectory(String sourceDir, String targetDir, String[] patterns, boolean isDiscard) {
        copyDirectory(sourceDir, targetDir, patterns, isDiscard, true);
    }

    /**
     * 复制文件夹，根据patterns参数进行过滤(isDiscard=true时)或者包含(isDiscard=false时)
     * @param sourceDir 来源文件夹
     * @param targetDir 目标文件夹
     * @param patterns 匹配模式字符串 @see org.zollty.util.match.ZolltyPathMatcher
     * @param isDiscard 等于true时为“排除模式”，为false则为“包含模式” 
     * @param isLog 是否记录日志（DEBUG级别）
     */
    public static void copyDirectory(String sourceDir, String targetDir, String[] patterns, boolean isDiscard,
            boolean isLog) {
        // 如果文件夹不存在 则建立新文件夹
        (new File(targetDir)).mkdirs();
        // 获取源文件夹当前下的文件或目录
        File[] file = (new File(sourceDir)).listFiles();

        List<PathMatcher> discardMather = new ArrayList<PathMatcher>();
        if (patterns != null) {
            for (String pattern : patterns) {
                discardMather.add(new ZolltyPathMatcher(pattern));
            }
        }

        for (int i = 0; i < file.length; i++) {
            if (file[i].isFile() && isDiscard ? isInclude(file[i].getAbsolutePath(), discardMather) : isDiscard(
                    file[i].getAbsolutePath(), discardMather)) {
                // 源文件
                File sourceFile = file[i];
                // 目标文件
                File targetFile = new File(new File(targetDir).getAbsolutePath() + File.separator + file[i].getName());
                try {
                    cloneFile(sourceFile, targetFile);
                }
                catch (IOException e) {
                    LOG.error(e, "无法拷贝该文件：" + sourceFile.getAbsolutePath());
                }
                if (isLog)
                    LOG.debug(sourceFile.getAbsolutePath());
            }
            else if (file[i].isDirectory() && isDiscard ? isInclude(file[i].getAbsolutePath() + "/", discardMather)
                    : isDiscard(file[i].getAbsolutePath() + "/", discardMather)) {
                // 准备复制的源文件夹
                String dir1 = sourceDir + "/" + file[i].getName();
                // 准备复制的目标文件夹
                String dir2 = targetDir + "/" + file[i].getName();
                copyDirectory(dir1, dir2, patterns, isDiscard, isLog);
            }
        }
    }

    private static boolean isDiscard(String path, List<PathMatcher> matcher) {
        boolean ret = false;
        for (PathMatcher pm : matcher) {
            if (pm.isMatch(path)) {
                ret = true;
            }
        }
        return ret;
    }

    private static boolean isInclude(String path, List<PathMatcher> matcher) {
        boolean ret = true;
        for (PathMatcher pm : matcher) {
            if (pm.isMatch(path)) {
                ret = false;
            }
        }
        return ret;
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
     * 改变其后缀
     * 
     * @param fileDir
     * @param suffix
     * @author zouty <br>
     *         2013-7-3
     */
    public static void fileRenameChangeSuffix(String fileDirStr, String suffix) {

        File fileDir = new File(fileDirStr);

        File[] files = fileDir.listFiles();
        for (int k = 0; k < files.length; k++) {
            if (files[k].isDirectory()) {
                fileRenameChangeSuffix(files[k].getPath(), suffix);
            }
            else {
                String simpleFileName = StringUtils.getFilenameWithoutExtension(files[k].getName());
                String newFileName = simpleFileName + "." + suffix;
                final File dirFile = new File(fileDir, newFileName);
                LOG.debug("Rename File :" + dirFile.getAbsolutePath());
                files[k].renameTo(dirFile);
            }
        }
    }

    /**
     * 每个文件夹里的文件都按顺序批量命名为： prefix_0000.FileType, prefix_0001.FileType...
     * 
     * @param fileDir
     * @param prefix
     */
    public static void fileRenameByIDNum(File fileDir, String prefix) {
        File[] files = fileDir.listFiles();
        for (int k = 0; k < files.length; k++) {
            StringBuffer sb = new StringBuffer(prefix);
            if (files[k].isDirectory()) {
                fileRenameByIDNum(files[k], prefix);
            }
            else {
                if (k < 10)
                    sb.append("_000").append(k);
                else if (k >= 10 && k < 100)
                    sb.append("_00").append(k);
                else if (k < 1000 && k >= 100)
                    sb.append("_0").append(k);
                else
                    sb.append("_").append(k);
                final int index = files[k].getName().lastIndexOf(".") + 1;
                final String fileType = files[k].getName().substring(index);
                sb.append(".").append(fileType);
                final String name = sb.toString();
                final File dirFile = new File(fileDir, name);
                LOG.debug("Rename File :" + files[k].getAbsolutePath());
                files[k].renameTo(dirFile);
            }
        }
    }

}
