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
 * Create by ZollTy on 2013-6-02 (http://blog.zollty.com/, zollty@163.com)
 */
package org.jretty.util;

import java.util.LinkedList;
import java.util.List;

/**
 * Path and fileName 相关工具
 * 
 * @author zollty
 * @since 2013-06-02
 */
public class PathUtils {

    /**
     * 转换成标准unix path
     * 
     * @param path the path (may be <code>null</code>)
     * @return the unix path
     */
    public static String normalPath(String path) {
        if (null == path) {
            return null;
        }
        return path.replace(Const.WINDOWS_FOLDER_SEPARATOR, Const.FOLDER_SEPARATOR);
    }

    /**
     * 从路径（url或者目录都可以）中获取文件名称（带后缀，形如 abc.txt） <br>
     * Extract the filename from the given path, e.g. "mypath/myfile.txt" -> "myfile.txt".
     * 
     * @param path
     *            the file path (may be <code>null</code>)
     * @return the extracted filename, or <code>null</code> if none
     */
    public static String getFilenameFromPath(String path) {
        if (null == path) {
            return null;
        }
        String path2 = path.replace(Const.WINDOWS_FOLDER_SEPARATOR, Const.FOLDER_SEPARATOR);
        int separatorIndex = path2.lastIndexOf(Const.FOLDER_SEPARATOR);
        return (separatorIndex != -1 ? path2.substring(separatorIndex + 1) : path2);
    }

    /**
     * 从路径（url或者目录都可以）中剥去文件名，获得文件所在的目录。 <br>
     * Strip the filename from the given path, e.g. "mypath/myfile.txt" -> "mypath/".
     * 
     * @param path
     *            the file path (may be <code>null</code>)
     * @return the path with stripped filename, or <code>null</code> if none
     */
    public static String stripFilenameFromPath(String path) {
        if (null == path) {
            return null;
        }
        int extIndex = path.lastIndexOf(EXTENSION_SEPARATOR);
        if (extIndex == -1) {
            return path;
        }
        String path2 = path.replace(Const.WINDOWS_FOLDER_SEPARATOR, Const.FOLDER_SEPARATOR);
        int folderIndex = path2.lastIndexOf(Const.FOLDER_SEPARATOR);
        return folderIndex > extIndex ? path2 : path2.substring(0, extIndex);
    }

    private static final char EXTENSION_SEPARATOR = '.';

    /**
     * 从路径（url或者目录都可以）中获取文件名称（不带后缀，形如 abc） <br>
     * Extract the filename without it's extension from the given path, e.g. "mypath/myfile.txt" -> "myfile".
     * 
     * @param path
     *            the file path (may be <code>null</code>)
     * @return the extracted filename, or <code>null</code> if none
     */
    public static String getFilenameWithoutExtension(String path) {
        if (null == path) {
            return null;
        }
        int extIndex = path.lastIndexOf(EXTENSION_SEPARATOR);
        if (extIndex == -1) {
            return path.substring(path.lastIndexOf(Const.FOLDER_SEPARATOR) + 1, path.length());
        }
        String path2 = path.replace(Const.WINDOWS_FOLDER_SEPARATOR, Const.FOLDER_SEPARATOR);
        int folderIndex = path2.lastIndexOf(Const.FOLDER_SEPARATOR);
        if (folderIndex > extIndex) {
            // throw new RuntimeException("the path form is not correct!--"+path);
            return null;
        }
        return path2.substring(folderIndex + 1, extIndex);
    }

    /**
     * 从路径（url或者目录都可以）中获取文件后缀（比如 txt）<br>
     * Extract the filename extension from the given path, e.g. "mypath/myfile.txt" -> "txt".
     * 
     * @param path
     *            the file path (may be <code>null</code>)
     * @return the extracted filename extension, or <code>null</code> if none
     */
    public static String getFilenameExtension(String path) {
        if (null == path) {
            return null;
        }
        int extIndex = path.lastIndexOf(EXTENSION_SEPARATOR);
        if (extIndex == -1) {
            return null;
        }
        String path2 = path.replace(Const.WINDOWS_FOLDER_SEPARATOR, Const.FOLDER_SEPARATOR);
        int folderIndex = path2.lastIndexOf(Const.FOLDER_SEPARATOR);
        if (folderIndex > extIndex) {
            return null;
        }
        return path2.substring(extIndex + 1);
    }

    /**
     * Apply the given relative path to the given path, assuming standard Java folder separation (i.e. "/" separators).
     * <p>(org/home/aa.txt, bb.txt) --> org/home/bb.txt
     * <p>(org/home/cc, bb.txt) --> org/home/bb.txt
     * <p>(org/home/cc/, bb.txt) --> org/home/cc/bb.txt
     * 
     * @param path
     *            the path to start from (usually a full file path)
     * @param relativePath
     *            the relative path to apply (relative to the full file path above)
     * @return the full file path that results from applying the relative path
     */
    public static String applyRelativePath(String path, String relativePath) {
        String path2 = path.replace(Const.WINDOWS_FOLDER_SEPARATOR, Const.FOLDER_SEPARATOR);
        String relativePath2 = relativePath.replace(Const.WINDOWS_FOLDER_SEPARATOR, Const.FOLDER_SEPARATOR);

        int separatorIndex = path2.lastIndexOf(Const.FOLDER_SEPARATOR);
        if (separatorIndex != -1) {
            String newPath = path.substring(0, separatorIndex);
            if (!relativePath2.startsWith(Const.FOLDER_SEPARATOR)) {
                newPath += Const.FOLDER_SEPARATOR;
            }
            return newPath + relativePath2;
        }
        else {
            return relativePath2;
        }
    }
    
    /**
     * connect multi paths to one
     * 
     * @param paths
     *            multi paths
     * @return one path which connect all the given path list
     */
    public static String connectPaths(String... paths) {
        if (paths.length == 1) {
            return paths[0];
        }
        StringBuilder sbu = new StringBuilder();
        if (paths[0].endsWith(Const.FOLDER_SEPARATOR)) {
            sbu.append(paths[0].substring(0, paths[0].length() - 1));
        } else {
            sbu.append(paths[0]);
        }
        for (int i = 1; i < paths.length; i++) {
            if (!paths[i].startsWith(Const.FOLDER_SEPARATOR)) {
                sbu.append(Const.FOLDER_SEPARATOR);
            }
            if (i != paths.length - 1 && paths[i].endsWith(Const.FOLDER_SEPARATOR)) {
                sbu.append(paths[i].substring(0, paths[i].length() - 1));
            } else {
                sbu.append(paths[i]);
            }
        }
        return sbu.toString();
    }

    /**
     * Normalize the path by suppressing sequences like "path/.." and
     * inner simple dots.
     * <p>The result is convenient for path comparison. For other uses,
     * notice that Windows separators ("\") are replaced by simple slashes.
     * @param path the original path
     * @return the normalized path
     */
    public static String cleanPath(String path) {
        if (null == path) {
            return null;
        }
        String pathToUse = StringUtils.replace(path, Const.WINDOWS_FOLDER_SEPARATOR, Const.FOLDER_SEPARATOR);

        // Strip prefix from path to analyze, to not treat it as part of the
        // first path element. This is necessary to correctly parse paths like
        // "file:core/../core/io/Resource.class", where the ".." should just
        // strip the first "core" directory while keeping the "file:" prefix.
        int prefixIndex = pathToUse.indexOf(":");
        String prefix = Const.STRING_LEN0;
        if (prefixIndex != -1) {
            prefix = pathToUse.substring(0, prefixIndex + 1);
            if (prefix.contains(Const.FOLDER_SEPARATOR)) {
                prefix = Const.STRING_LEN0;
            }
            else {
                pathToUse = pathToUse.substring(prefixIndex + 1);
            }
        }
        if (pathToUse.startsWith(Const.FOLDER_SEPARATOR)) {
            prefix = prefix + Const.FOLDER_SEPARATOR;
            pathToUse = pathToUse.substring(1);
        }

        String[] pathArray = StringSplitUtils.splitByWholeSeparator(pathToUse, Const.FOLDER_SEPARATOR);
        List<String> pathElements = new LinkedList<String>();
        int tops = 0;

        for (int i = pathArray.length - 1; i >= 0; i--) {
            String element = pathArray[i];
            if (Const.CURRENT_PATH.equals(element)) {
                // Points to current directory - drop it.
            }
            else if (Const.PARENT_PATH.equals(element)) {
                // Registering top path found.
                tops++;
            }
            else {
                if (tops > 0) {
                    // Merging path element with element corresponding to top path.
                    tops--;
                }
                else {
                    // Normal path element found.
                    pathElements.add(0, element);
                }
            }
        }

        // Remaining top paths need to be retained.
        for (int i = 0; i < tops; i++) {
            pathElements.add(0, Const.PARENT_PATH);
        }

        return prefix + CollectionUtils.toString(pathElements, Const.FOLDER_SEPARATOR);
    }

}
