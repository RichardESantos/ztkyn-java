package org.gitee.ztkyn.easyexcel;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import org.gitee.ztkyn.core.colleciton.ECollectionUtil;
import org.gitee.ztkyn.core.ecj.DynamicJavaCompiler;
import org.gitee.ztkyn.core.json.JacksonUtil;
import org.gitee.ztkyn.easyexcel.domain.DataConfiguration;
import org.gitee.ztkyn.easyexcel.domain.DataDefinition;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author richard
 * @version 1.0.0
 * @date 2023-05-30 13:47
 * @description SingleLineReadTests
 */
public class EasyExcelReadTests {

	private static final Logger logger = LoggerFactory.getLogger(EasyExcelReadTests.class);

	@Test
	public void testSingleRead() {
		String filePath = "D:\\各种文档\\项目文档\\四机项目\\数据收集\\存货数据样本（郭瑞）\\存货库龄\\00030 DCM2.XLSX";
		long count = EasyExcelHelper.readFirstSheet(filePath, EasyExcelHelper.singleLineConsumer);
		System.out.println(count);

		EasyExcel.read(filePath, new ReadListener() {
			@Override
			public void invoke(Object data, AnalysisContext context) {
				System.out.println(JacksonUtil.obj2String(data));
			}

			@Override
			public void doAfterAllAnalysed(AnalysisContext context) {

			}
		}).sheet(0).doRead();
	}

	@Test
	public void testMultiRead() {
		String filePath = "C:\\Users\\richard\\Desktop\\1000000line.xlsx";
		long count = EasyExcelHelper.batchReadFirstSheet(filePath, EasyExcelHelper.multiLineConsumer);
		System.out.println(count);
	}

	@Test
	public void testReadExcelHeader() {
		String filePath = "D:\\各种文档\\项目文档\\四机项目\\数据收集\\存货数据样本（郭瑞）\\存货库龄\\00030 DCM2.XLSX";
		Map<Integer, String> stringMap = EasyExcelHelper.readExcelHeader(filePath, null);
		System.out.println(stringMap);
	}

	@Test
	public void testFtl() throws IOException {

		DataDefinition dataDefinition = new DataDefinition().setDescription("DCM2Data")
			.setClassName("DCMData")
			.setPackageName("org.gitee.ztkyn.gen");
		List<DataConfiguration> configurationList = ECollectionUtil.createFastList();
		String[] headerNames = { "工厂", "库存地点", "物料编号", "批号", "特殊库存标识", "特殊库存编号", "分析期间", "月", "仓储地点的描述", "物料描述（短文本）",
				"物料组", "物料组描述", "基本计量单位", "标准价格", "价格单位", "评估类", "评估类的描述", "期末库存", "合计", "合计-金额", "合计-金额单位", "3个月内(数量)",
				"3个月内(金额)", "3个月内(金额)单位", "3-6个月(数量", "3-6个月(金额)", "3-6个月(金额)单位", "6个月-1年(数量)", "6个月-1年(金额)",
				"6个月-1年(金额)单位", "1-2年(数量)", "1-2年(金额)", "1-2年(金额)单位", "2-3年(数量)", "2-3年(金额)", "2-3年(金额)单位", "3-5年(数量)",
				"3-5年(金额)", "3-5年(金额)单位", "5年以上(数量)", "5年以上(金额)", "5年以上(金额)单位", "3-4个月(数量)", "3-4个月(金额)", "3-4个月(金额)单位",
				"12-13个月(数量)", "12-13个月(金额)", "12-13个月(金额)单位", "3-12个月(数量)", "3-12个月(金额)", "3-12个月(金额)单位", "12个月以上(数量)",
				"12个月以上(金额)", "12个月以上(金额)单位", "9-12个月(数量)", "9-12个月(金额)", "9-12个月(金额)单位" };

		for (int i = 0; i < headerNames.length; i++) {
			String headerName = headerNames[i];
			DataConfiguration dataConfiguration = new DataConfiguration().setHeaderName(headerName)
				.setValueName("dcmF" + i)
				.setValueType("String");
			configurationList.add(dataConfiguration);
		}
		dataDefinition.setConfigurations(configurationList);
		byte[] source = FreeMarkerTemplateUtils.parseTemplate(dataDefinition, "DataDefinitionTemplate.ftl");
		// System.out.println(new String(source));
		String filePath = "D:\\各种文档\\项目文档\\四机项目\\数据收集\\存货数据样本（郭瑞）\\存货库龄\\00030 DCM2.XLSX";
		try (DynamicJavaCompiler compiler = new DynamicJavaCompiler();) {
			String fullName = dataDefinition.getFullName();
			boolean isCompile = compiler.javaCompiler(fullName, new String(source));
			if (isCompile) {
				Class<?> clazz = compiler.loadClass(fullName);
				EasyExcelHelper.readFirstSheet(filePath, EasyExcelHelper.singleLineConsumer);
				EasyExcel.read(filePath, clazz, new ReadListener<>() {
					@Override
					public void invoke(Object data, AnalysisContext context) {
						logger.info(JacksonUtil.obj2String(data));
					}

					@Override
					public void doAfterAllAnalysed(AnalysisContext context) {
						logger.info("所有数据解析完成！");
					}
				}).sheet(0).doRead();
				System.out.println(clazz);
				// Property p = (Property) clazz.newInstance();
				// p.setValue("name", "kang");
				// System.out.println(p.toString());
			}
		}
		catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}

	}

}
