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
 * Create by ZollTy on 2013-10-24 (http://blog.zollty.com/, zollty@163.com)
 */
package org.zollty.tool.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.zollty.util.BasicRuntimeException;
import org.zollty.util.Const;
import org.zollty.util.IOUtils;
import org.zollty.util.NestedRuntimeException;
import org.zollty.util.StringUtils;

/**
 * Servlet方式下载【通过Response.getOutputStream推送到客户端】
 * 前端建议用iframe的方式去下载
 * @author zollty 
 * @since 2013-10-24
 */
public class ServletFileDownload {
	
    public static final Map<String, String> MIME = new HashMap<String, String>();
    static {
        // MIME类型
        MIME.put("csv", "text/comma-separated-values");
        MIME.put("xls", "application/x-msexcel");
        MIME.put("xlsx", "application/x-msexcel");
        MIME.put("txt", "text/plain");
        MIME.put("htm", "text/html");
        MIME.put("html", "text/html");
        MIME.put("htmls", "text/html");
        // MIME.put("zip", "application/x-compressed");
        MIME.put("zip", "application/x-zip-compressed");
        // MIME.put("zip", "application/zip");
        // MIME.put("zip", "multipart/x-zip");
        MIME.put("gz", "application/x-gzip");
        MIME.put("rar", "application/x-rar-compressed");
        
        MIME.put("doc", "application/msword");
        MIME.put("docx", "application/msword");
        MIME.put("wps", "application/application/vnd.ms-works");
        MIME.put("ppt", "application/application/vnd.ms-powerpoint");
        
        MIME.put("pdf", "application/pdf");
        MIME.put("jpe", "image/jpeg");
        MIME.put("jpeg", "image/jpeg");
        MIME.put("jpg", "image/jpeg");
        MIME.put("bmp", "image/bmp");
        MIME.put("png", "image/png");
        MIME.put("js", "application/x-javascript");
        // MIME.put("js", "application/x-ns-proxy-autoconfig");
        
        // MIME.put("rar", "application/rar");
       
        MIME.put("css", "text/css");
        MIME.put("avi", "video/msvideo");
        MIME.put("mp3", "audio/mp3");
        MIME.put("mp4", "video/mp4");
        
        MIME.put("jar", "application/x-java-archive");
        MIME.put("java", "text/x-java-source");
        MIME.put("class", "application/octet-stream");
        MIME.put("exe", "application/octet-stream");
    }

    public static void fileDownload(String fileFullPath, HttpServletRequest request, HttpServletResponse response) {
        String fileName = StringUtils.getFilenameFromPath(fileFullPath);
        String contentType = StringUtils.getFilenameExtension(fileFullPath);
        contentType = MIME.get(contentType);
        fileDownload(fileName, fileFullPath, contentType, request, response);
    }

    public static void fileDownload(String fileFullPath, String contentType, HttpServletRequest request,
            HttpServletResponse response) {
        String fileName = StringUtils.getFilenameFromPath(fileFullPath);
        fileDownload(fileName, fileFullPath, contentType, request, response);
    }

    public static void fileDownload(String fileName, String fileFullPath, String contentType,
            HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isNullOrEmpty(fileName)) {
            throw new BasicRuntimeException("The file name is null or empty! fileName=" + fileName);
        }
        if (StringUtils.isNullOrEmpty(fileFullPath)) {
            throw new BasicRuntimeException("The file path is null or empty! fileFullPath=" + fileFullPath);
        }
        if (StringUtils.isNullOrEmpty(contentType)) {
            throw new BasicRuntimeException("The content-type is null or empty! contentType=" + contentType);
        }
        FileInputStream in = null;
        OutputStream out = null;
        File file = null;
        try {
            file = new File(fileFullPath);
            in = new FileInputStream(file);
            out = response.getOutputStream();
            // 设置输出的格式
            response.setContentType(contentType);
            response.addHeader("Content-Disposition", "attachment; " + praseAttachmentFilename(fileName, request));
            int len = (int) file.length();
            response.setContentLength(len);
//            byte[] buf = null;
//            if (len < 2048) {
//                buf = new byte[len];
//            } else {
//                buf = new byte[2048];
//            }
//            int numRead = 0;
//            while ((numRead = in.read(buf)) != -1) {
//                out.write(buf, 0, numRead);
//            }
            IOUtils.clone(in, len, out);
        } catch (Exception e) {
            throw new NestedRuntimeException(e, "Error occured when download the file[" + fileFullPath + "]");
        } finally {
            IOUtils.closeIO(in, out);
        }
    }

    private static String praseAttachmentFilename(String fileName, HttpServletRequest request)
            throws UnsupportedEncodingException {
        String rtn;
        UserAgentParser uap = new UserAgentParser(request);
        if (uap.isMSIEBrowser()) {
            rtn = "filename=" + URLEncoder.encode(fileName, Const.UTF_8);
        } else {
            rtn = "filename=" + new String(fileName.getBytes(Const.UTF_8), Const.ISO_8859_1);
        }
        // // Opera浏览器只能采用filename*
        // if (userAgent.indexOf("opera") != -1) {
        // rtn = "filename*=UTF-8''" + newFileName;
        // }
        return rtn;
    }

}