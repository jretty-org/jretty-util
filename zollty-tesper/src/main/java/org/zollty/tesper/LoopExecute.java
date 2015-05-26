/*
 * @(#)LoopExecute.java
 * Create by Zollty_Tsow on 2013-12-2 
 * you may find ZollTy at csdn, github, oschina, stackoverflow...
 * e.g. https://github.com/zollty  http://www.cnblogs.com/zollty 
 * 
 * Copyright 2012-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * 
 */
package org.zollty.tesper;

/**
 * @author zollty 
 * @since 2013-12-2
 */
public interface LoopExecute {
	
	public void execute() throws Exception;
	
	/**
	 * 循环的次数
	 */
	public int getLoopTimes();
	
}
