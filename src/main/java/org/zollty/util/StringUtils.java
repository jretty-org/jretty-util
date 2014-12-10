/* @(#)StringUtils.java 
 * Copyright (C) 2013-2014 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License").
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * Create by zollty on 2013-6-02 [http://blog.csdn.net/zollty (or GitHub)]
 */
package org.zollty.util;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

/**
 * 通用字符串工具类
 * @author zollty 
 * @since 2013-06-02
 */
public class StringUtils {
	
	/**
	 * Checks if a String is null or empty ("").
	 */
	public static boolean isNullOrEmpty(CharSequence str) {
		return str == null || str.length() ==0;
	}
	
	/**
	 * Checks if a String is null or empty ("").
	 */
	public static boolean isNotEmpty(CharSequence str) {
		return str != null && str.length() !=0;
	}
	
	/**
	 * Checks if a String is empty (""), null or whitespace(e.g. " ", "\t", "\n").
	 */
	public static boolean isBlank(CharSequence str) {
		int strLen;
		// empty ("") or null
		if ( str == null || (strLen = str.length()) ==0 ) {
			return true;
		}
		// length > 0 then check every character
		for (int i = 0; i < strLen; i++) {
			if ( Character.isWhitespace(str.charAt(i))==false ) {
				return false; //find char is not whitespace
			}
		}
		return true;
	}
	/**
	 * Checks if a String is empty (""), null or whitespace(e.g. " ", "\t", "\n").
	 */
	public static boolean isNotBlank(CharSequence str) {
		int strLen;
		// empty ("") or null
		if ( str == null || (strLen = str.length()) ==0 ) {
			return false;
		}
		// length > 0 then check every character
		for (int i = 0; i < strLen; i++) {
			if ( Character.isWhitespace(str.charAt(i))==false ) {
				return true; //find char is not whitespace
			}
		}
		return false;
	}
	
	/**
	 * 判断两个字符串是否相等，包含空字符串的情况
	 */
	public static boolean equals(String str1, String str2) {
		return str1 != null && str1.equals(str2);
	}
	
	/**
	 * 将null转换成空字符串""
	 */
	public static String nullToEmpty(String str){
    	return str==null?"":str;
    }
	
    public static String decideCharSet(String charSet) {
        if (StringUtils.isNullOrEmpty(charSet)) {
            return Const.DEFAULT_CHARSET;
        }
        return charSet;
    }
    
    public static Charset toCharSet(String charSet) {
        if (StringUtils.isNullOrEmpty(charSet)) {
            return Charset.forName(Const.DEFAULT_CHARSET);
        }
        return Charset.forName(charSet);
    }
	
	/**
	 * 当前环境的默认编码(即file.encoding)
	 */
	public static String getJvmEncoding(){
		return Charset.defaultCharset().displayName();
	}
	
	
	/**
	 * 重新定义了String.getBytes()。默认用UTF-8编码，以便去除与平台的相关性。
	 * <p>
	 * 且将CheckedException转换成了RuntimeException。
	 * 
	 * @param str 原字符串
	 * @return str.getBytes("UTF-8")的结果
	 */
	public static byte[] getBytes(String str) {
	    if (str == null) {
            return null;
        }
	    try {
            return str.getBytes(Const.DEFAULT_CHARSET);
        }
        catch (final UnsupportedEncodingException e) {
            throw new NestedRuntimeException(e);
        }
	}
	
	/**
     * String.getBytes()，将CheckedException转换成了RuntimeException。
     * <p>
     * 默认用UTF-8编码，以便去除与平台的相关性。
     * 
     * @param str 原字符串
     * @return str.getBytes("UTF-8")的结果
     */
    public static byte[] getBytes(final String str, final String charSet) {
        if (str == null) {
            return null;
        }
        try {
            return str.getBytes(decideCharSet(charSet));
        }
        catch (final UnsupportedEncodingException e) {
            throw new NestedRuntimeException(e);
        }
    }
    
    public static String newString(final byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        try {
            return new String(bytes, Const.DEFAULT_CHARSET);
        } catch (final UnsupportedEncodingException e) {
            throw new NestedRuntimeException(e);
        }
    }
    
    public static String newString(final byte[] bytes, final String charSet) {
        if (bytes == null) {
            return null;
        }
        try {
            return new String(bytes, decideCharSet(charSet));
        } catch (final UnsupportedEncodingException e) {
            throw new NestedRuntimeException(e);
        }
    }
	
	/**
	 * 字符串编码转换
	 * @param newCharset 新编码，例如UTF-8
	 * @author zouty <br>2013-6-6
	 */
	public static String changeEncode(final String str, final String newCharset){
		if(str == null || str.length() ==0) return str;
		try{
			String iso = new String(str.getBytes(newCharset),Const.ISO_8859_1);  //ISO-8859-1
			return new String(iso.getBytes(Const.ISO_8859_1),newCharset);
		}catch(final UnsupportedEncodingException e ){
			throw new NestedRuntimeException(e);
		}
	}
	
	
	/**
	 * 从路径（url或者目录都可以）中获取文件名称（带后缀，形如 abc.txt）
	 * Extract the filename from the given path,
	 * e.g. "mypath/myfile.txt" -> "myfile.txt".
	 * @param path the file path (may be <code>null</code>)
	 * @return the extracted filename, or <code>null</code> if none
	 */
	public static String getFilenameFromPath(String path) {
		if( null==path ) return null;
		String path2 = path.replace(Const.WINDOWS_FOLDER_SEPARATOR, Const.FOLDER_SEPARATOR);
		//path = path.replace(WINDOWS_FOLDER_SEPARATOR, FOLDER_SEPARATOR);
		int separatorIndex = path2.lastIndexOf(Const.FOLDER_SEPARATOR);
		return (separatorIndex != -1 ? path2.substring(separatorIndex + 1) : path2);
	}
	
	/**
	 * 从路径（url或者目录都可以）中剥去文件名，获得文件所在的目录。
	 * Strip the filename from the given path,
	 * e.g. "mypath/myfile.txt" -> "mypath/".
	 * @param path the file path (may be <code>null</code>)
	 * @return the path with stripped filename,
	 * or <code>null</code> if none
	 */
	public static String stripFilenameFromPath(String path){
		if( null==path ) return null;
		String path2 = path.replace(Const.WINDOWS_FOLDER_SEPARATOR, Const.FOLDER_SEPARATOR);
		int folderIndex = path2.lastIndexOf(Const.FOLDER_SEPARATOR);
		if (folderIndex == -1) {
			return path2;
		}
		return path2.substring(0,folderIndex+1);
	}
	
	private static final char EXTENSION_SEPARATOR = '.';
	
	/**
	 * 从路径（url或者目录都可以）中获取文件名称（不带后缀，形如 abc）
	 * Extract the filename without it's extension from the given path,
	 * e.g. "mypath/myfile.txt" -> "myfile".
	 * @param path the file path (may be <code>null</code>)
	 * @return the extracted filename, or <code>null</code> if none
	 */
	public static String getFilenameWithoutExtension(String path){
		if( null==path ) return null;
		int extIndex = path.lastIndexOf(EXTENSION_SEPARATOR);
		if (extIndex == -1) {
		    return path.substring(path.lastIndexOf(Const.FOLDER_SEPARATOR)+1,path.length()); 
			//return null;
		}
		String path2 = path.replace(Const.WINDOWS_FOLDER_SEPARATOR, Const.FOLDER_SEPARATOR);
		int folderIndex = path2.lastIndexOf(Const.FOLDER_SEPARATOR);
		if (folderIndex > extIndex) {
			//throw new RuntimeException("the path form is not correct!--"+path);
			return null;
		}
		return path2.substring(folderIndex+1,extIndex); 
	}
	
	/**
	 * 从路径（url或者目录都可以）中获取文件后缀（比如 txt）<br>
	 * Extract the filename extension from the given path,
	 * e.g. "mypath/myfile.txt" -> "txt".
	 * @param path the file path (may be <code>null</code>)
	 * @return the extracted filename extension, or <code>null</code> if none
	 */
	public static String getFilenameExtension(String path) {
		if( null==path ) return null;
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
	 * Apply the given relative path to the given path,
	 * assuming standard Java folder separation (i.e. "/" separators).
	 * @param path the path to start from (usually a full file path)
	 * @param relativePath the relative path to apply
	 * (relative to the full file path above)
	 * @return the full file path that results from applying the relative path
	 */
	public static String applyRelativePath(String path, String relativePath) {
		String path2 = path.replace(Const.WINDOWS_FOLDER_SEPARATOR, Const.FOLDER_SEPARATOR);
		String relativePath2 = relativePath.replace(Const.WINDOWS_FOLDER_SEPARATOR, Const.FOLDER_SEPARATOR);
		
		if ( path2.endsWith(Const.FOLDER_SEPARATOR) ) {
			if ( relativePath2.startsWith(Const.FOLDER_SEPARATOR) ) {
				return path2.substring(0, path2.length()-1)+relativePath2;
			}
			return path2+relativePath2;
		}
		
		if ( relativePath2.startsWith(Const.FOLDER_SEPARATOR) ) {
			return path2+relativePath2;
		}
		return path2+Const.FOLDER_SEPARATOR+relativePath2;
	}
	
	
	private static final String REPLACE_LABEL = "{}";
	/**
	 * 用objs[]的值去替换字符串s中的{}符号
	 */
	public static String replaceParams(String s, Object... objs) {
		if(s==null) return s;
		if(objs == null || objs.length == 0)
			return s;
		if(s.indexOf(REPLACE_LABEL) == -1){
		    StringBuilder result = new StringBuilder();
		    result.append(s);
		    for(Object obj: objs){
		        result.append(" ").append(obj);
		    }
		    return result.toString();
		}
		
		String[] stra = new String[objs.length];
		int len=s.length();
		for(int i=0;i<objs.length;i++){
			if( objs[i]!=null)
				stra[i] = objs[i].toString();
			else{
				stra[i] = "null";
			}
			len += stra[i].length();
		}
		
		StringBuilder result = new StringBuilder(len);
		int cursor = 0;
		int index = 0;
		for(int start; (start = s.indexOf(REPLACE_LABEL, cursor)) != -1 ;) {
			result.append(s.substring(cursor, start));
			if(index < stra.length)
				result.append(stra[index]);
			else
				result.append(REPLACE_LABEL);
			cursor = start + 2;
			index++;
		}
		result.append(s.substring(cursor, s.length()));
        if (index < objs.length) {
            for (int i = index; i < objs.length; i++) {
                result.append(" ").append(objs[i]);
            }
        }
		return result.toString();
	}
	
	/**
	 * 转换html里面的5个特殊字符：<code>&, <, >, ', and "</code>,
	 */
	public static String simpleHtmlEscape(String str) {
		if (str == null)
			return "";
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < str.length(); ++i) {
			char c = str.charAt(i);
			switch (c) {
			case '&':
				sb.append("&amp;");
				break;
			case '<':
				sb.append("&lt;");
				break;
			case '>':
				sb.append("&gt;");
				break;
			case '\"':
				sb.append("&quot;");
				break;
			case '\'':
				sb.append("&apos;");
				break;
			default:
				sb.append(c);
				break;
			}
		}
		return sb.toString();
	}
}
