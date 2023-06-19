package org.gitee.ztkyn.easyexcel;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.alibaba.excel.util.ConverterUtils;
import org.gitee.ztkyn.core.colleciton.CollectionUtil;
import org.gitee.ztkyn.core.colleciton.ECollectionUtil;
import org.gitee.ztkyn.core.json.JacksonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author richard
 * @date 2023-05-30 10:56
 * @description EasyExcelHelper
 * @version 1.0.0
 */
public class EasyExcelHelper {

	private static final Logger logger = LoggerFactory.getLogger(EasyExcelHelper.class);

	/**
	 * 默认无对象读取，无操作,实际需求请自定义
	 */
	public static final Consumer<Map<Integer, String>> singleLineConsumer = stringMap -> {
	};

	/**
	 * 默认无对象读取，无操作,实际需求请自定义
	 */
	public static final Consumer<List<Map<Integer, String>>> multiLineConsumer = maps -> {

	};

	/**
	 * 多行读取，读取第一个sheet
	 * @param filePath
	 * @param multiLineConsumer
	 * @param <T>
	 * @return
	 */
	public static <T> long batchReadFirstSheet(String filePath, Consumer<List<T>> multiLineConsumer) {
		return batchReadSheet(filePath, 0, multiLineConsumer);
	}

	/**
	 * 单行读取，读取第一个sheet
	 * @param filePath
	 * @param singleLineConsumer
	 * @param <T>
	 * @return
	 */
	public static <T> long readFirstSheet(String filePath, Consumer<T> singleLineConsumer) {
		return readSheet(filePath, 0, singleLineConsumer);
	}

	/**
	 * 多行读取，读取第一个sheet
	 * @param inputStream
	 * @param multiLineConsumer
	 * @param <T>
	 * @return
	 */
	public static <T> long batchReadFirstSheet(InputStream inputStream, Consumer<List<T>> multiLineConsumer) {
		return batchReadSheet(inputStream, 0, multiLineConsumer);
	}

	/**
	 * 单行读取，读取第一个sheet
	 * @param inputStream
	 * @param singleLineConsumer
	 * @param <T>
	 * @return
	 */
	public static <T> long readFirstSheet(InputStream inputStream, Consumer<T> singleLineConsumer) {
		return readSheet(inputStream, 0, singleLineConsumer);
	}

	/**
	 * 多行读取，读取第一个sheet
	 * @param filePath
	 * @param tClass
	 * @param multiLineConsumer
	 * @param <T>
	 * @return
	 */
	public static <T> long batchReadFirstSheet(String filePath, Class<T> tClass, Consumer<List<T>> multiLineConsumer) {
		return batchReadSheet(filePath, 0, tClass, multiLineConsumer);
	}

	/**
	 * 单行读取，读取第一个sheet
	 * @param filePath
	 * @param tClass
	 * @param singleLineConsumer
	 * @param <T>
	 * @return
	 */
	public static <T> long readFirstSheet(String filePath, Class<T> tClass, Consumer<T> singleLineConsumer) {
		return readSheet(filePath, 0, tClass, singleLineConsumer);
	}

	/**
	 * 多行读取，读取第一个sheet
	 * @param inputStream
	 * @param tClass
	 * @param multiLineConsumer
	 * @param <T>
	 * @return
	 */
	public static <T> long batchReadFirstSheet(InputStream inputStream, Class<T> tClass,
			Consumer<List<T>> multiLineConsumer) {
		return batchReadSheet(inputStream, 0, tClass, multiLineConsumer);
	}

	/**
	 * 单行读取，读取第一个sheet
	 * @param inputStream
	 * @param tClass
	 * @param singleLineConsumer
	 * @param <T>
	 * @return
	 */
	public static <T> long readFirstSheet(InputStream inputStream, Class<T> tClass, Consumer<T> singleLineConsumer) {
		return readSheet(inputStream, 0, tClass, singleLineConsumer);
	}

	/**
	 * 多行读取，读取指定sheet
	 * @param filePath
	 * @param multiLineConsumer
	 * @param <T>
	 * @return
	 */
	public static <T> long batchReadSheet(String filePath, int sheetNo, Consumer<List<T>> multiLineConsumer) {
		AtomicLong count = new AtomicLong(0);
		EasyExcel.read(filePath, new MultiLineReadListener<>(multiLineConsumer, count)).sheet(sheetNo).doRead();
		return count.longValue();
	}

	/**
	 * 多行读取，读取指定sheet,此方法的用处在于，不用知道具体类型（也就是无法准确知道泛型类型的时候），只需要传入对应的 class
	 * @param filePath
	 * @param sheetNo
	 * @param tClass
	 * @param multiLineConsumer
	 * @return
	 * @param <T>
	 */
	public static <T> long batchReadSheet(String filePath, int sheetNo, Class<T> tClass,
			Consumer<List<T>> multiLineConsumer) {
		AtomicLong count = new AtomicLong(0);
		EasyExcel.read(filePath, tClass, new MultiLineReadListener<>(multiLineConsumer, count)).sheet(sheetNo).doRead();
		return count.longValue();
	}

	/**
	 * 单行读取，读取指定sheet
	 * @param filePath
	 * @param singleLineConsumer
	 * @param <T>
	 * @return
	 */
	public static <T> long readSheet(String filePath, int sheetNo, Consumer<T> singleLineConsumer) {
		AtomicLong count = new AtomicLong(0);
		EasyExcel.read(filePath, new SingleLineReadListener<>(singleLineConsumer, count)).sheet(sheetNo).doRead();
		return count.longValue();
	}

	/**
	 * 单行读取，读取指定sheet,此方法的用处在于，不用知道具体类型（也就是无法准确知道泛型类型的时候），只需要传入对应的 class
	 * @param filePath
	 * @param sheetNo
	 * @param tClass
	 * @param singleLineConsumer
	 * @return
	 * @param <T>
	 */
	public static <T> long readSheet(String filePath, int sheetNo, Class<T> tClass, Consumer<T> singleLineConsumer) {
		AtomicLong count = new AtomicLong(0);
		EasyExcel.read(filePath, tClass, new SingleLineReadListener<>(singleLineConsumer, count))
			.sheet(sheetNo)
			.doRead();
		return count.longValue();
	}

	/**
	 * 多行读取，读取指定sheet
	 * @param inputStream
	 * @param multiLineConsumer
	 * @param <T>
	 * @return
	 */
	public static <T> long batchReadSheet(InputStream inputStream, int sheetNo, Consumer<List<T>> multiLineConsumer) {
		AtomicLong count = new AtomicLong(0);
		EasyExcel.read(inputStream, new MultiLineReadListener<>(multiLineConsumer)).sheet(sheetNo).doRead();
		return count.longValue();
	}

	/**
	 * 多行读取，读取指定sheet,此方法的用处在于，不用知道具体类型（也就是无法准确知道泛型类型的时候），只需要传入对应的 class
	 * @param inputStream
	 * @param sheetNo
	 * @param tClass
	 * @param multiLineConsumer
	 * @return
	 * @param <T>
	 */
	public static <T> long batchReadSheet(InputStream inputStream, int sheetNo, Class<T> tClass,
			Consumer<List<T>> multiLineConsumer) {
		AtomicLong count = new AtomicLong(0);
		EasyExcel.read(inputStream, tClass, new MultiLineReadListener<>(multiLineConsumer)).sheet(sheetNo).doRead();
		return count.longValue();
	}

	/**
	 * 单行读取，读取指定sheet
	 * @param inputStream
	 * @param singleLineConsumer
	 * @param <T>
	 * @return
	 */
	public static <T> long readSheet(InputStream inputStream, int sheetNo, Consumer<T> singleLineConsumer) {
		AtomicLong count = new AtomicLong(0);
		EasyExcel.read(inputStream, new SingleLineReadListener<>(singleLineConsumer)).sheet(sheetNo).doRead();
		return count.longValue();
	}

	/**
	 * 单行读取，读取指定sheet, 此方法的用处在于，不用知道具体类型（也就是无法准确知道泛型类型的时候），只需要传入对应的 class
	 * @param inputStream
	 * @param sheetNo
	 * @param tClass
	 * @param singleLineConsumer
	 * @return
	 * @param <T>
	 */
	public static <T> long readSheet(InputStream inputStream, int sheetNo, Class<T> tClass,
			Consumer<T> singleLineConsumer) {
		AtomicLong count = new AtomicLong(0);
		EasyExcel.read(inputStream, tClass, new SingleLineReadListener<>(singleLineConsumer)).sheet(sheetNo).doRead();
		return count.longValue();
	}

	/**
	 * 多行读取，读取所有sheet,数据处理方式一致
	 * @param filePath
	 * @param multiLineConsumer
	 * @param <T>
	 */
	public static <T> void batchReadMultiSheet(String filePath, Consumer<List<T>> multiLineConsumer) {
		EasyExcel.read(filePath, new MultiLineReadListener<>(multiLineConsumer)).doReadAll();
	}

	/**
	 * 单行读取，读取所有sheet,数据处理方式一致
	 * @param filePath
	 * @param singleLineConsumer
	 * @param <T>
	 */
	public static <T> void readMultiSheet(String filePath, Consumer<T> singleLineConsumer) {
		EasyExcel.read(filePath, new SingleLineReadListener<>(singleLineConsumer)).doReadAll();
	}

	/**
	 * 多行读取，一个sheet 一种处理方式，consumerList 顺序对应 sheet 顺序，为空，则跳过对应sheet 数据读取
	 * @param filePath
	 * @param multiLineConsumerList
	 * @param <T>
	 */
	public static <T> void batchReadMultiSheet(String filePath, List<Consumer<List<T>>> multiLineConsumerList) {
		try (ExcelReader excelReader = EasyExcel.read(filePath).build()) {
			List<ReadSheet> sheetList = ECollectionUtil.MutableList.newList();
			for (int i = 0; i < multiLineConsumerList.size(); i++) {
				final int tmpInt = i;
				Optional.ofNullable(multiLineConsumerList.get(i)).ifPresent(listConsumer -> {
					sheetList.add(EasyExcel.readSheet(tmpInt)
						.registerReadListener(new MultiLineReadListener<>(listConsumer))
						.build());
				});

			}
			if (CollectionUtil.notBlank(sheetList)) {
				excelReader.read(sheetList);
			}
		}
	}

	/**
	 * 单行读取，一个sheet 一种处理方式，consumerList 顺序对应 sheet 顺序，为空，则跳过对应sheet 数据读取
	 * @param filePath
	 * @param singleLineConsumer
	 * @param <T>
	 */
	public static <T> void readMultiSheet(String filePath, List<Consumer<T>> singleLineConsumer) {
		try (ExcelReader excelReader = EasyExcel.read(filePath).build()) {
			List<ReadSheet> sheetList = ECollectionUtil.MutableList.newList();
			for (int i = 0; i < singleLineConsumer.size(); i++) {
				final int tmpInt = i;
				Optional.ofNullable(singleLineConsumer.get(i)).ifPresent(consumer -> {
					sheetList.add(EasyExcel.readSheet(tmpInt)
						.registerReadListener(new SingleLineReadListener<>(consumer))
						.build());
				});

			}
			if (CollectionUtil.notBlank(sheetList)) {
				excelReader.read(sheetList);
			}
		}
	}

	/**
	 * 读取excel 表头
	 * @param inputStream
	 * @param headLine
	 * @return
	 */
	public static Map<Integer, String> readExcelHeader(InputStream inputStream, Integer headLine) {
		Map<Integer, String> headerMap = ECollectionUtil.MutableMap.newMap(64);
		headLine = Objects.isNull(headLine) ? 1 : headLine;
		// 获取表头
		EasyExcel.read(inputStream, readHeaderListener(headerMap)).sheet(0).doRead();
		return headerMap;
	}

	/**
	 * 读取excel 表头
	 * @param filePath
	 * @param headLine
	 * @return
	 */
	public static Map<Integer, String> readExcelHeader(String filePath, Integer headLine) {
		Map<Integer, String> headerMap = ECollectionUtil.MutableMap.newMap(64);
		headLine = Objects.isNull(headLine) ? 1 : headLine;
		// 获取表头
		EasyExcel.read(filePath, readHeaderListener(headerMap)).sheet(0).doRead();
		return headerMap;
	}

	private static ReadListener<Object> readHeaderListener(Map<Integer, String> headerMap) {
		return new ReadListener<>() {

			@Override
			public void invokeHead(Map<Integer, ReadCellData<?>> headMap, AnalysisContext context) {
				Map<Integer, String> map = ConverterUtils.convertToStringMap(headMap, context);
				if (CollectionUtil.notBlank(map)) {
					headerMap.putAll(map);
				}
			}

			@Override
			public boolean hasNext(AnalysisContext context) {
				// 返回false ，跳过具体的excel 数据读取
				return false;
			}

			@Override
			public void invoke(Object o, AnalysisContext analysisContext) {
				logger.info("读取到数据：{}", JacksonUtil.obj2String(o));
			}

			@Override
			public void doAfterAllAnalysed(AnalysisContext analysisContext) {
				logger.info("所有数据解析完成！");
			}
		};
	}

}
