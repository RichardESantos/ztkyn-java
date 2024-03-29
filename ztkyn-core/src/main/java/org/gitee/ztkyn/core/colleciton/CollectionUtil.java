package org.gitee.ztkyn.core.colleciton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;

/**
 * @author richard
 * @version 1.0
 */
public class CollectionUtil {

	private static final Logger logger = LoggerFactory.getLogger(CollectionUtil.class);

	/**
	 * 判断集合不能为空
	 * @param list
	 * @param <T>
	 * @return
	 */
	public static <T> boolean notBlank(List<T> list) {
		return Objects.nonNull(list) && !list.isEmpty();
	}

	/**
	 * 判断集合为空
	 * @param list
	 * @param <T>
	 * @return
	 */
	public static <T> boolean isBlank(List<T> list) {
		return !notBlank(list);
	}

	/**
	 * 判断集合不能为空
	 * @param set
	 * @param <T>
	 * @return
	 */
	public static <T> boolean notBlank(Set<T> set) {
		return Objects.nonNull(set) && !set.isEmpty();
	}

	/**
	 * 判断集合为空
	 * @param set
	 * @param <T>
	 * @return
	 */
	public static <T> boolean isBlank(Set<T> set) {
		return !notBlank(set);
	}

	/**
	 * 判断 map 不能为空
	 * @param map
	 * @return
	 */
	public static <K, V> boolean notBlank(Map<K, V> map) {
		return Objects.nonNull(map) && !map.isEmpty();
	}

	/**
	 * 判断 map 为空
	 * @param map
	 * @return
	 */
	public static <K, V> boolean isBlank(Map<K, V> map) {
		return !notBlank(map);
	}

	/**
	 * 删除所有value==null 的值
	 * @param map
	 * @param <K>
	 * @param <V>
	 * @return
	 */
	public static <K, V> Map<K, V> removeNulls(Map<K, V> map) {
		if (isBlank(map)) {
			return map;
		}
		return removeValues(map, kvEntry -> Objects.isNull(kvEntry.getValue()));
	}

	/**
	 * 删除满足条件的(k,v)
	 * @param map
	 * @param predicate
	 * @param <K>
	 * @param <V>
	 * @return
	 */
	public static <K, V> Map<K, V> removeValues(Map<K, V> map, Predicate<Map.Entry<K, V>> predicate) {
		if (isBlank(map)) {
			return map;
		}
		map.entrySet().removeIf(predicate);
		return map;
	}

}
