/*
 * @(#)DebugTool.java
 * Travelsky Report Engine (TRE) Source Code, Version 1.0.0
 * Author(s): 
 * Zollty Tsou (http://blog.csdn.net/zollty, zouty@travelsky.com)
 * Copyright (C) 2013-2014 Travelsky Technology. All rights reserved.
 */
package org.zollty.util;

import java.util.Date;


/**
 * 测试时专用打印控制台日志的，上线时会将开关关掉，只作为开发时的调试，不作为日志的记录
 * @author zouty 
 * <br> 2013-3-5
 */
public class DebugTool {
	
	/**
	 * DEBUG模式： true--启用，false--禁用
	 */
	public static final boolean E = true;
	
	private static final String LEVEL_ERROR = "ERROR";
	
	private static final String LEVEL_INFO = "INFO ";
	
    private DebugTool(){
    }
    
	/**
	 * Debug工具类专用的日期格式
	 */
	public static final DateFormatUtils LOGDATEFORMAT = new DateFormatUtils(DateFormatUtils.HH_mm_ss_SSS);

	
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓
    // 以下4个方法是 测试时专用的，上线时会将开关关掉，只作为开发时调试，不作为日志的记录
    // 这些方法都是静态方法，可直接调用，打印及时、效率高
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛
	
	public static void println(Object obj){
		if(E){
//		    LOG.info(appendMsg(obj,LEVEL_INFO));
		    System.out.println(appendMsg(obj,LEVEL_INFO));
		}
	}
	
	public static void println(){
	    if(E){
            System.out.println();
        }
	}
	
	public static void print(Object obj){
		if(E){
//		    LOG.info(appendMsg(obj,LEVEL_INFO));
		    System.out.print(appendMsg(obj,LEVEL_INFO));
		}
	}
	
	public static void error(Object obj){
		if(E){
//		    LOG.error(appendMsg(obj,LEVEL_ERROR));
		    System.err.println(appendMsg(obj,LEVEL_ERROR));
		}
	}
	
	public static void printStack(Exception e){
		if(E){
//		    LOG.info(appendMsg(ExceptionUtils.getStackTraceStr(null, e),LEVEL_ERROR));
		    e.printStackTrace();
		}
	}
	
	private static String appendMsg(Object msg, String level){
		StringBuilder sb = new StringBuilder();
		sb.append(LOGDATEFORMAT.format(new Date()));
		sb.append(" ");
		sb.append(level);
		sb.append("\t");
		sb.append( null!=msg?msg.toString():"" );
		return sb.toString();
	}
	
}