package org.zollty.util.zip;

import org.junit.Ignore;
import org.junit.Test;

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
