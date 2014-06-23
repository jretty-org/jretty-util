/* @(#)ServletFileUpload.java 
 * Copyright (C) 2013-2014 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License").
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * Create by zollty on 2013-10-24 [http://blog.csdn.net/zollty (or GitHub)]
 */
package org.zollty.tool.web;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import org.zollty.log.LogFactory;
import org.zollty.log.Logger;
import org.zollty.util.BasicRuntimeException;
import org.zollty.util.IOUtils;
import org.zollty.util.StringUtils;

/**
 * 基于纯servlet的文件上传工具【完美版，不会损坏文件】
 * 注意，网上其他很多类似的纯servlet的文件上传工具都会造成文件损坏，
 * 比如jar，上传后会损坏，导致无法加载。（其原因是文件末尾多了一个换行符）
 * @author zollty
 * @since 2013-10-24
 */
public class ServletFileUpload {

	private static final Logger LOG = LogFactory.getLogger(ServletFileUpload.class);
	
	public static final int MAX_POST_SIZE = 30720; // 30M
	private static final String CHARSET = "UTF-8"; // Use UTF-8 encoding
	
	private HttpServletRequest request;
	private String absoluteTargetDir;
	private String inludeSuffix[];
	private String includeMimeType[];
	private String reName;
	private String uploadFileName;
	private boolean allowCover;
	
	protected ServletFileUpload(){}
	
	public ServletFileUpload(HttpServletRequest request, String absoluteTargetDir){
		this.request = request;
		this.absoluteTargetDir = absoluteTargetDir;
	}
	
	public void fileUpload()
			throws IOException {
		
		if( StringUtils.isNullOrEmpty(absoluteTargetDir) ){
			throw new BasicRuntimeException("Argument error: absoluteTargetDir=" + absoluteTargetDir);
		}
		File dir = new File(absoluteTargetDir);
		if (!dir.exists() && !dir.mkdirs() ) {
			throw new BasicRuntimeException("Can not create the directory: " + absoluteTargetDir);
		}
		
		ServletInputStream inputStream = null;
		FileOutputStream outputStream = null; // 输出流
		try{
		
			long startUploadTime = System.currentTimeMillis();
	
			inputStream = request.getInputStream(); // 获取客户端上传的数据
			
			// 限制文件size小于30M
			checkSize();
			
			byte[] buffer = new byte[1024]; // 缓冲区
			// 读取第一行
			int position = inputStream.readLine(buffer, 0, buffer.length); 
			String breakStr = ""; // 终止字符串
			//String objRef = ""; // 对象引用
			if (position != -1) { // 第一行不为空
				breakStr = new String(buffer, 0, 28, CHARSET);
				//objRef = new String(buffer, 29, position);
				if (LogFactory.isTraceEnabled()){
				    LOG.trace("终止字符串：" + breakStr);
					//LOG.trace("分隔符：" + objRef);
				}
			}
			 // 读取第二行
			position = inputStream.readLine(buffer, 0, buffer.length);
			if (position != -1) {
				String head = new String(buffer, 0, position, CHARSET);
				if (LogFactory.isTraceEnabled())
				    LOG.trace("头信息：" + head);
				int fileNamePosition = 0;
				if ((fileNamePosition = head.indexOf("filename=\"")) != -1) {
					uploadFileName = head.substring(fileNamePosition).split("\"")[1];
					if( StringUtils.isBlank(uploadFileName) ) {
						throw new BasicRuntimeException("upload file name is empty! Please ensure that a file is choosed!");
					}
					if( ! checkFileType(uploadFileName) ){
						throw new BasicRuntimeException("The file type is prohibited uploaded: "+uploadFileName);
					}
					if (LogFactory.isDebugEnabled())
					    LOG.debug("源文件名（含扩展名）：" + uploadFileName);
					if( StringUtils.isNotBlank(reName) ){
						uploadFileName = reName; // 重命名
					}
					File file = new File( dir.getAbsolutePath() + File.separator + StringUtils.getFilenameFromPath(uploadFileName));
					if (file.exists() && !allowCover) {
					    throw new BasicRuntimeException("The file already exists! Do not allowed to cover!");
//					    if(!allowCover){
//					        throw new BasicRuntimeException("The file already exists! Do not allowed to cover!");
//					    }
//					    file.delete();
					}
					outputStream = new FileOutputStream(file);
				}
			}
			// 读取第三行
			position = inputStream.readLine(buffer, 0, buffer.length);
			String type = "";
			if (position != -1) {
				type = new String(buffer, 0, position, CHARSET);
				if (LogFactory.isTraceEnabled())
				    LOG.trace("文件类型：" + type);//Content-Type: application/x-compress
				String mimeType = (String) type.subSequence(type.indexOf("Content-Type:") + 14, type.length());
				if (LogFactory.isTraceEnabled())
				    LOG.trace("MIME类型：" + mimeType);
			    
			}
			inputStream.readLine(buffer, 0, buffer.length); // 读取第四行
			int totalSize = 0;
			String isBreakStr = "";
			Queue<ByteBufferInfo> queue = new LinkedList<ByteBufferInfo>();
			ByteBufferInfo tempbb = null;
			while ((position = inputStream.readLine(buffer, 0, buffer.length)) != -1) {
				if (queue.size() > 1) {
					tempbb = queue.poll(); // 出队
					outputStream.write(tempbb.buffer, 0, tempbb.size);
				}
				isBreakStr = new String(buffer, 0, position, CHARSET);
				if (isBreakStr.length() > 28
						&& isBreakStr.substring(0, 28).equals(breakStr)) // 遇到分隔符则终止读取
				{
					break;
				}
				queue.offer(new ByteBufferInfo(buffer, position)); // 入队
				totalSize += position;
			}
			int qs = queue.size();
			int tint = qs - 1;
			for (int i = 0; i < qs; i++) {
				tempbb = queue.poll();
				if (i < tint) {
					outputStream.write(tempbb.buffer, 0, tempbb.size);
				} else {
					outputStream.write(tempbb.buffer, 0, tempbb.size - 2);
					totalSize = totalSize - 2;
				}
			}
			if (LogFactory.isDebugEnabled())
			    LOG.debug("文件大小 = " + totalSize + "(Byte) = " + (float) totalSize/1024/1024 + "(MB)");
			
			long finishUploadTime = System.currentTimeMillis();
			if (LogFactory.isDebugEnabled())
			    LOG.debug("文件上传成功，耗时：" + (finishUploadTime - startUploadTime) + " 毫秒");
			
		}finally{
			
			if( null!=outputStream ) {
			    IOUtils.closeIO(outputStream);
			}
			if( null!=inputStream ) {
			    IOUtils.closeIO(inputStream);
			}
		}
	}
	
	private void checkSize(){
		int len = request.getContentLength();
		float len2 = (float) len / 1024; // b--kb
		if (LogFactory.isTraceEnabled())
		    LOG.trace("文件近似大小 = " + len+ "(Byte) = "+len2+"(KB)");
		//float len3 = (float) (Math.round(len2 * 100)) / 100;
		if (MAX_POST_SIZE < len2) { // 文件大小不可超过 30 M
			throw new BasicRuntimeException("File size should not exceed 30 M.");
		}
	}
	
	private boolean checkFileType(String name){
		if(inludeSuffix==null){ return true;}
		for(String type: inludeSuffix){
			if(name.endsWith(type))
				return true;
		}
		return false;
	}
	
	public String getUploadFileName(){
	    return uploadFileName;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public String getAbsoluteTargetDir() {
		return absoluteTargetDir;
	}

	public void setAbsoluteTargetDir(String absoluteTargetDir) {
		this.absoluteTargetDir = absoluteTargetDir;
	}

    public String[] getInludeSuffix() {
        if (null != inludeSuffix) {
            return inludeSuffix.clone();
        }
        return null;
    }

	public void setInludeSuffix(String inludeSuffix[]) {
	    if(inludeSuffix!=null){
	        this.inludeSuffix = inludeSuffix.clone();
	    }
	}

    public String[] getIncludeMimeType() {
        if (null != includeMimeType) {
            return includeMimeType.clone();
        }
        return null;
    }

	public void setIncludeMimeType(String includeMimeType[]) {
	    if(includeMimeType!=null){
	        this.includeMimeType = includeMimeType.clone();
        }
	}

	public String getReName() {
		return reName;
	}

	public void setReName(String reName) {
		this.reName = reName;
	}

	public boolean isAllowCover() {
        return allowCover;
    }

    public void setAllowCover(boolean allowCover) {
        this.allowCover = allowCover;
    }

    private static class ByteBufferInfo {
		public byte[] buffer;
		public int size;

		public ByteBufferInfo(byte[] buffer, int size) {
			this.buffer = buffer.clone();
			this.size = size;
		}
	}
}