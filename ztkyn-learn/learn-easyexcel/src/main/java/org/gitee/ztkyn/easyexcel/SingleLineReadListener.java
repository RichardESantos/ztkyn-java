package org.gitee.ztkyn.easyexcel;

import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author whty
 * @date 2023-05-30 10:44
 * @description SingleLineReadListener
 * @version 1.0.0
 */
public class SingleLineReadListener<T> extends AbstractReadListener<T> implements ReadListener<T> {

	private static final Logger logger = LoggerFactory.getLogger(SingleLineReadListener.class);

	/**
	 * 数据消费
	 */
	private final Consumer<T> consumer;

	public SingleLineReadListener(Consumer<T> consumer) {
		this.consumer = consumer;
	}

	public SingleLineReadListener(Consumer<T> consumer, AtomicLong count) {
		this.consumer = consumer;
		this.count = count;
	}

	@Override
	public void invoke(T data, AnalysisContext context) {
		logData(data);
		count.incrementAndGet();
		consumer.accept(data);
	}

	@Override
	public void doAfterAllAnalysed(AnalysisContext context) {
		logger.info("所有数据解析完成！");
	}

}
