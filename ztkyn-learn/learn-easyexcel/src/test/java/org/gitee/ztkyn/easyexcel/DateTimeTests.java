package org.gitee.ztkyn.easyexcel;

import java.util.Map;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.gitee.ztkyn.core.date.DateUtil;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author richard
 * @version 1.0.0
 * @date 2023-07-18 10:29
 * @description DateTimeTests
 */
public class DateTimeTests {

	private static final Logger logger = LoggerFactory.getLogger(DateTimeTests.class);

	@Test
	public void read() {
		String filePath = "E:\\日期时间模版处理.xlsx";
		EasyExcel.read(filePath, new AnalysisEventListener<Map<Integer, Object>>() {
			@Override
			public void invoke(Map<Integer, Object> dateTemplate, AnalysisContext context) {
				for (Object value : dateTemplate.values()) {
					System.out.println(DateUtil.parseDateTime(String.valueOf(value)));
				}
			}

			@Override
			public void doAfterAllAnalysed(AnalysisContext context) {

			}
		}).sheet(0).doRead();
	}

}
