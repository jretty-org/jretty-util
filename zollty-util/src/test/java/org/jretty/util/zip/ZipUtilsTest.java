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
 * Create by ZollTy on 2014-7-11 (http://blog.zollty.com/, zollty@163.com)
 */
package org.zollty.util.zip;

import org.junit.Ignore;
import org.junit.Test;

/**
 * @author zollty
 * @since 2014-7-11
 */
public class ZipUtilsTest {

    @Test
    @Ignore
    public void testZipFile() throws ZipException {

        String sourceDir = "D:\\tojar";
        String targetZip = "D:\\tojar.zip";

        // 压缩文件夹或者文件
        ZipUtils.zipFile(targetZip, sourceDir);

    }

    @Test
    @Ignore
    public void testUnzipFile() throws ZipException {

        String sourceZipPath = "D:\\tojar.zip";
        String targetDir = "D:\\tojar2";

        // 解压缩文件到指定路径
        ZipUtils.unzipFile(targetDir, sourceZipPath);

    }

}
