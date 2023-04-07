package org.gitee.ztkyn.core.function;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.function.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author whty
 * @version 1.0.0
 * @date 2023-04-06 15:57
 * @description DataProcessHandler 加强版，支持获取返回值
 */
public class DataProcessFuture<T, R> {

	private static final Logger logger = LoggerFactory.getLogger(DataProcessFuture.class);

	private DataProcessHandler<T> processHandler;

	private AtomicReference<R> reference;

	private DataProcessFuture(DataProcessHandler<T> processHandler) {
		this.processHandler = processHandler;
		this.reference = new AtomicReference<>();
	}

	/**
	 * @param value
	 * @param predicate
	 * @param rClass 参数的意义在于，确定 R 的类型
	 * @param <T>
	 * @param <R>
	 * @return
	 */
	public static <T, R> DataProcessFuture<T, R> of(T value, Predicate<T> predicate, Class<R> rClass) {
		return new DataProcessFuture<>(DataProcessHandler.of(value, predicate));
	}

	public DataProcessFuture<T, R> ifTrue(Function<T, R> function) {
		if (processHandler.isJudgeResult()) {
			reference.set(function.apply(processHandler.getValue()));
		}
		return this;
	}

	public DataProcessFuture<T, R> ifFalse(Function<T, R> function) {
		if (!processHandler.isJudgeResult()) {
			reference.set(function.apply(processHandler.getValue()));
		}
		return this;
	}

	public DataProcessFuture<T, R> ifFalseThrow(RuntimeException exception) {
		if (!processHandler.isJudgeResult()) {
			throw exception;
		}
		return this;
	}

	public R getResult() {
		return reference.get();
	}

}
