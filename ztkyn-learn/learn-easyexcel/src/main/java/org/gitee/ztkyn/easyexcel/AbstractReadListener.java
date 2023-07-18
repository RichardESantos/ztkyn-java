package org.gitee.ztkyn.easyexcel;

import java.util.concurrent.atomic.AtomicLong;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.exception.ExcelDataConvertException;
import com.alibaba.excel.metadata.CellExtra;
import com.alibaba.excel.read.listener.ReadListener;
import org.gitee.ztkyn.core.json.JacksonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author richard
 * @date 2023-05-30 14:05
 * @description AbstractReadListener
 * @version 1.0.0
 */
public abstract class AbstractReadListener<T> implements ReadListener<T> {

	private static final Logger logger = LoggerFactory.getLogger(AbstractReadListener.class);

	/**
	 * 读取到数据总条数
	 */
	protected AtomicLong count;

	protected void logData(T data) {
		if (logger.isDebugEnabled()) {
			logger.info("解析到一条数据:{}", JacksonUtil.obj2String(data));
		}
	}

	/**
	 * 在转换异常 获取其他异常下会调用本接口。抛出异常则停止读取。如果这里不抛出异常则 继续读取下一行。
	 * @param exception
	 * @param context
	 * @throws Exception
	 */
	@Override
	public void onException(Exception exception, AnalysisContext context) {
		logger.error("解析失败，但是继续解析下一行:{}", exception.getMessage());
		// 如果是某一个单元格的转换异常 能获取到具体行号
		// 如果要获取头的信息 配合invokeHeadMap使用
		if (exception instanceof ExcelDataConvertException excelDataConvertException) {
			logger.error("第{}行，第{}列解析异常，数据为:{}", excelDataConvertException.getRowIndex(),
					excelDataConvertException.getColumnIndex(), excelDataConvertException.getCellData());
		}
	}

	@Override
	public void extra(CellExtra extra, AnalysisContext context) {
		logger.info("读取到了一条额外信息:{}", JacksonUtil.obj2String(extra));
		switch (extra.getType()) {
			case COMMENT -> logger.info("额外信息是批注,在rowIndex:{},columnIndex;{},内容是:{}", extra.getRowIndex(),
					extra.getColumnIndex(), extra.getText());
			case HYPERLINK -> {
				if ("Sheet1!A1".equals(extra.getText())) {
					logger.info("额外信息是超链接,在rowIndex:{},columnIndex;{},内容是:{}", extra.getRowIndex(),
							extra.getColumnIndex(), extra.getText());
				}
				else if ("Sheet2!A1".equals(extra.getText())) {
					logger.info(
							"额外信息是超链接,而且覆盖了一个区间,在firstRowIndex:{},firstColumnIndex;{},lastRowIndex:{},lastColumnIndex:{},"
									+ "内容是:{}",
							extra.getFirstRowIndex(), extra.getFirstColumnIndex(), extra.getLastRowIndex(),
							extra.getLastColumnIndex(), extra.getText());
				}
				else {
					logger.warn("Unknown hyperlink!");
				}
			}
			case MERGE -> logger.info(
					"额外信息是超链接,而且覆盖了一个区间,在firstRowIndex:{},firstColumnIndex;{},lastRowIndex:{},lastColumnIndex:{}",
					extra.getFirstRowIndex(), extra.getFirstColumnIndex(), extra.getLastRowIndex(),
					extra.getLastColumnIndex());
		}
	}

}
