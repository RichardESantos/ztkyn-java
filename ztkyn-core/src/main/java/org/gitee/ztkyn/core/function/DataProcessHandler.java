package org.gitee.ztkyn.core.function;

import java.util.function.Consumer;
import java.util.function.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author whty
 * @version 1.0
 */
public final class DataProcessHandler<T> {

	private static final Logger logger = LoggerFactory.getLogger(DataProcessHandler.class);

	private final T value;

	private final boolean judgeResult;

	private DataProcessHandler(T value, boolean judgeResult) {
		this.value = value;
		this.judgeResult = judgeResult;
	}

	/**
	 * 借用外部判断结果，可以与 value 无关
	 * @param t
	 * @param judgeResult
	 * @param <T>
	 * @return
	 */
	public static <T> DataProcessHandler<T> of(T t, boolean judgeResult) {
		return new DataProcessHandler<>(t, judgeResult);
	}

	/**
	 * 使用针对于 value 本身的判断，
	 * @param t
	 * @param predicate
	 * @param <T>
	 * @return
	 */
	public static <T> DataProcessHandler<T> of(T t, Predicate<T> predicate) {
		return new DataProcessHandler<>(t, predicate.test(t));
	}

	public DataProcessHandler<T> ifTrue(Consumer<T> consumer) {
		if (judgeResult) {
			consumer.accept(value);
		}
		return this;
	}

	public DataProcessHandler<T> ifTrue(Runnable runnable) {
		if (judgeResult) {
			runnable.run();
		}
		return this;
	}

	public void ifFalse(Consumer<T> consumer) {
		if (!judgeResult) {
			consumer.accept(value);
		}
	}

	public void ifFalse(Runnable runnable) {
		if (!judgeResult) {
			runnable.run();
		}
	}

	public void ifFalseThrow(RuntimeException exception) {
		ifFalse(value -> {
			throw exception;
		});
	}

	public T getValue() {
		return value;
	}

	public boolean isJudgeResult() {
		return judgeResult;
	}

}
