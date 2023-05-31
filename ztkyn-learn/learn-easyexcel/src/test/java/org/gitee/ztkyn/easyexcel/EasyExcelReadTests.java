package org.gitee.ztkyn.easyexcel;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author whty
 * @date 2023-05-30 13:47
 * @description SingleLineReadTests
 * @version 1.0.0
 */
public class EasyExcelReadTests {

	private static final Logger logger = LoggerFactory.getLogger(EasyExcelReadTests.class);

	@Test
	public void testSingleRead() {
		String filePath = "D:\\各种文档\\项目文档\\四机项目\\数据收集\\存货数据样本（郭瑞）\\存货库龄\\00030 DCM2.XLSX";
		long count = EasyExcelHelper.readFirstSheet(filePath, EasyExcelHelper.singleLineConsumer);
		System.out.println(count);
	}

	@Test
	public void testMultiRead() {
		String filePath = "D:\\各种文档\\项目文档\\四机项目\\数据收集\\存货数据样本（郭瑞）\\存货库龄\\00030 DCM2.XLSX";
		long count = EasyExcelHelper.batchReadFirstSheet(filePath, EasyExcelHelper.multiLineConsumer);
		System.out.println(count);
	}

	@Test
	public void testFtl() {

	}

}
