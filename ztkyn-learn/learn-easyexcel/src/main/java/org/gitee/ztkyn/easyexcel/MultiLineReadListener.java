package org.gitee.ztkyn.easyexcel;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ListUtils;
import org.gitee.ztkyn.core.colleciton.CollectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author richard
 * @date 2023-05-29 16:52
 * @description 多行数据读取监听器
 * @version 1.0.0
 */
public class MultiLineReadListener<T> extends AbstractReadListener<T> implements ReadListener<T> {

	private static final Logger logger = LoggerFactory.getLogger(MultiLineReadListener.class);

	/**
	 * 每隔5条存储数据库，实际使用中可以100条，然后清理list ，方便内存回收
	 */
	private static final int BATCH_COUNT = 100;

	/**
	 * 缓存的数据
	 */
	private List<T> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);

	/**
	 * 数据消费
	 */
	private final Consumer<List<T>> batConsumer;

	public MultiLineReadListener(Consumer<List<T>> batConsumer) {
		this.batConsumer = batConsumer;
	}

	public MultiLineReadListener(Consumer<List<T>> batConsumer, AtomicLong count) {
		this.batConsumer = batConsumer;
		this.count = count;
	}

	/**
	 * 这个每一条数据解析都会来调用
	 * @param data one row value. It is same as {@link AnalysisContext#readRowHolder()}
	 * @param context analysis context
	 */
	@Override
	public void invoke(T data, AnalysisContext context) {
		logData(data);
		cachedDataList.add(data);
		count.incrementAndGet();
		// 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
		if (cachedDataList.size() >= BATCH_COUNT) {
			// 进行数据处理
			batConsumer.accept(cachedDataList);
			// 存储完成清理 list
			cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
		}
	}

	/**
	 * 所有数据解析完成了 都会来调用
	 * @param context
	 */
	@Override
	public void doAfterAllAnalysed(AnalysisContext context) {
		if (CollectionUtil.notBlank(cachedDataList)) {
			// 进行数据处理
			batConsumer.accept(cachedDataList);
		}
		logger.info("所有数据解析完成！");
	}

}
