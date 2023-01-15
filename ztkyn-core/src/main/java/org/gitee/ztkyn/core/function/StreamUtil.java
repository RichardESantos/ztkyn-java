package org.gitee.ztkyn.core.function;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Java Stream Util
 *
 * @date 2022-11-01 23:16:44
 */
public class StreamUtil {

	private static final Logger logger = LoggerFactory.getLogger(StreamUtil.class);

	/**
	 * list 转换
	 * @param collection
	 * @param mapper
	 * @return
	 * @param <T>
	 * @param <U>
	 */
	public static <T, U> List<U> toList(Collection<T> collection, Function<? super T, ? extends U> mapper) {
		return collection.stream().map(mapper).collect(Collectors.toList());
	}

	/**
	 * list 转换+去重
	 * @param collection
	 * @param mapper
	 * @return
	 * @param <T>
	 * @param <U>
	 */
	public static <T, U> Set<U> toSet(Collection<T> collection, Function<? super T, ? extends U> mapper) {
		return collection.stream().map(mapper).collect(Collectors.toSet());
	}

	/**
	 * collection 转换成 map，遇到重复的值会自动丢弃
	 * @param collection
	 * @param keyMapper
	 * @return
	 * @param <T>
	 */
	public static <T> Map<String, T> toMap(Collection<T> collection, Function<? super T, String> keyMapper) {
		return collection.stream().collect(Collectors.toMap(keyMapper, Function.identity(), (v1, v2) -> v1));
	}

	/**
	 * collection 转换成 map，根据制定的key 值进行分组
	 * @param collection
	 * @param keyMapper
	 * @return
	 * @param <T>
	 */
	public static <T> Map<String, List<T>> groupBy(List<T> collection, Function<? super T, String> keyMapper) {
		return collection.stream().collect(Collectors.groupingBy(keyMapper));
	}

	/**
	 * 集合，先转换，再过滤
	 * @param collection
	 * @param mapper
	 * @param predicate
	 * @return
	 * @param <T>
	 * @param <U>
	 */
	public static <T, U> Optional<U> findAny(Collection<T> collection, Function<? super T, U> mapper,
			Predicate<U> predicate) {
		return collection.stream().map(mapper).filter(predicate).findAny();
	}

	/**
	 * 集合，先转换，再过滤
	 * @param collection
	 * @param mapper
	 * @param predicate
	 * @return
	 * @param <T>
	 * @param <U>
	 */
	public static <T, U> Optional<U> findFirst(Collection<T> collection, Function<? super T, U> mapper,
			Predicate<U> predicate) {
		return collection.stream().map(mapper).filter(predicate).findFirst();
	}

}
