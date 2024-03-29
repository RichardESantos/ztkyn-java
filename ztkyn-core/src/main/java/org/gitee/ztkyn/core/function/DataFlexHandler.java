package org.gitee.ztkyn.core.function;

import lombok.Getter;
import org.gitee.ztkyn.core.string.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * 数据处理工具类
 *
 * @author richard
 * @version 1.0
 */
public final class DataFlexHandler<T> {

	private static final Logger logger = LoggerFactory.getLogger(DataFlexHandler.class);

	private final T value;

	private final boolean judgeResult;

	private DataFlexHandler(T value, boolean judgeResult) {
		this.value = value;
		this.judgeResult = judgeResult;
	}

	/**
	 * 使用针对于 value 本身的判断，
	 * @param t
	 * @param predicate
	 * @param <T>
	 * @return
	 */
	public static <T> DataFlexHandler<T> of(T t, Predicate<T> predicate) {
		return new DataFlexHandler<>(t, predicate.test(t));
	}

	/**
	 * 简写方法
	 * @param t
	 * @param <T>
	 * @return
	 */
	public static <T> DataFlexHandler<T> notNull(T t) {
		return new DataFlexHandler<>(t, Objects.nonNull(t));
	}

	/**
	 * 简写方法
	 * @param t
	 * @param <T>
	 * @return
	 */
	public static <T> DataFlexHandler<T> isNull(T t) {
		return new DataFlexHandler<>(t, Objects.isNull(t));
	}

	/**
	 * 简写方法
	 * @param content
	 * @return
	 */
	public static DataFlexHandler<String> notBlank(String content) {
		return new DataFlexHandler<>(content, StringUtil.isNotBlank(content));
	}

	/**
	 * 简写方法
	 * @param content
	 * @return
	 */
	public static DataFlexHandler<String> isBlank(String content) {
		return new DataFlexHandler<>(content, StringUtil.isBlank(content));
	}

	/**
	 * 简写方法
	 * @param list
	 * @param <E>
	 * @return
	 */
	public static <E> DataFlexHandler<List<E>> notBlank(List<E> list) {
		return new DataFlexHandler<>(list, Objects.nonNull(list) && !list.isEmpty());
	}

	/**
	 * 简写方法
	 * @param list
	 * @param <E>
	 * @return
	 */
	public static <E> DataFlexHandler<List<E>> isBlank(List<E> list) {
		return new DataFlexHandler<>(list, Objects.isNull(list) || list.isEmpty());
	}

	/**
	 * 简写方法
	 * @param set
	 * @param <E>
	 * @return
	 */
	public static <E> DataFlexHandler<Set<E>> notBlank(Set<E> set) {
		return new DataFlexHandler<>(set, Objects.nonNull(set) && !set.isEmpty());
	}

	/**
	 * 简写方法
	 * @param set
	 * @param <E>
	 * @return
	 */
	public static <E> DataFlexHandler<Set<E>> isBlank(Set<E> set) {
		return new DataFlexHandler<>(set, Objects.isNull(set) || set.isEmpty());
	}

	/**
	 * 简写方法
	 * @param kvMap
	 * @param <K>
	 * @param <V>
	 * @return
	 */
	public static <K, V> DataFlexHandler<Map<K, V>> notBlank(Map<K, V> kvMap) {
		return new DataFlexHandler<>(kvMap, Objects.nonNull(kvMap) && !kvMap.isEmpty());
	}

	/**
	 * 简写方法
	 * @param kvMap
	 * @param <K>
	 * @param <V>
	 * @return
	 */
	public static <K, V> DataFlexHandler<Map<K, V>> isBlank(Map<K, V> kvMap) {
		return new DataFlexHandler<>(kvMap, Objects.isNull(kvMap) || kvMap.isEmpty());
	}

	public DataFlexHandler<T> ifTrue(Consumer<T> consumer) {
		if (judgeResult) {
			consumer.accept(value);
		}
		return this;
	}

	public DataFlexHandler<T> ifTrue(Runnable runnable) {
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

	/**
	 * 对结果进行转换
	 * @param function
	 * @param <R>
	 * @return
	 */
	public <R> R ifTrueAndConvert(Function<T, R> function) {
		return judgeResult ? function.apply(value) : null;
	}

	/**
	 * 对结果进行转换
	 * @param function
	 * @param <R>
	 * @return
	 */
	public <R> R ifTrueAndConvert(Function<T, R> function, R defaultResult) {
		return judgeResult ? function.apply(value) : defaultResult;
	}

	/**
	 * 对结果进行转换
	 * @param function
	 * @param <R>
	 * @return
	 */
	public <R> R ifFalseAndConvert(Function<T, R> function) {
		return judgeResult ? null : function.apply(value);
	}

	/**
	 * 对结果进行转换
	 * @param trueFunction
	 * @param falseFunction
	 * @param <R>
	 * @return
	 */
	public <R> R convert(Function<T, R> trueFunction, Function<T, R> falseFunction) {
		return judgeResult ? trueFunction.apply(value) : falseFunction.apply(value);
	}

	/**
	 * 如果为false，则抛出指定异常
	 * @param exception
	 */
	public void ifFalseThrow(RuntimeException exception) {
		if (!judgeResult)
			throw exception;
	}

	public T getValue() {
		return value;
	}

	public boolean isJudgeResult() {
		return judgeResult;
	}

}
