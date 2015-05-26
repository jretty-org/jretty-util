/* @(#)ThreadUtils.java 
 * Copyright (C) 2013-2014 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License").
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * Create by zollty on 2013-8-20 [http://blog.csdn.net/zollty (or GitHub)]
 */
package org.zollty.util;

import org.zollty.log.LogFactory;
import org.zollty.log.Logger;

/**
 * @author zollty 
 * @since 2013-8-20
 */
public class ThreadUtils {
	
	private static final Logger LOG = LogFactory.getLogger(ThreadUtils.class);
	
	/**
	 * 线程休眠
	 * @param sleepTime
	 */
	public static void sleepThread(long sleepTime){
		try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {
			if(LogFactory.isDebugEnabled()) LOG.info("Thread "+Thread.currentThread().getName()+" sleep be interrupted.");
		}
	}
	
	/**
	 * 强行杀死线程的方法
	 * @param thread
	 */
	@SuppressWarnings("deprecation")
	public static void stopThread(Thread thread){
		try {
			thread.setPriority(Thread.MIN_PRIORITY);
			thread.stop();
			thread.destroy();
		} catch (Throwable e) {
			if(LogFactory.isDebugEnabled()) LOG.info("stop thread ["+thread.getName()+"]...");
		}
	}

}
