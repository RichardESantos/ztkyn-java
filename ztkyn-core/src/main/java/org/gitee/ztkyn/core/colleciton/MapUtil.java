package org.gitee.ztkyn.core.colleciton;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author whty
 * @version 1.0
 * @description map 工具类
 * @date 2023/3/6 15:54
 */
public class MapUtil {

	private static final Logger logger = LoggerFactory.getLogger(MapUtil.class);

	/**
	 * 保留 map 中指定的 key 值，如果 key 值对应的 value 为null，则丢弃
	 * @param originalMap
	 * @param retainKeys
	 * @return
	 * @param <K>
	 * @param <V>
	 */
	public static <K, V> Map<K, V> retainKeys(Map<K, V> originalMap, Set<K> retainKeys) {
		return retainKeysWithNullValue(originalMap, retainKeys, false);
	}

	/**
	 * 保留 map 中指定的 key 值，如果 key 值对应的 value 为null，根据 retainNullValue 确定是否保留
	 * @param originalMap
	 * @param retainKeys
	 * @param retainNullValue
	 * @return
	 * @param <K>
	 * @param <V>
	 */
	public static <K, V> Map<K, V> retainKeysWithNullValue(Map<K, V> originalMap, Set<K> retainKeys,
			boolean retainNullValue) {
		Map<K, V> targetMap = new HashMap<>();
		retainKeys.forEach(k -> {
			V v = originalMap.getOrDefault(k, null);
			if (retainNullValue) {
				targetMap.put(k, v);
			}
		});
		return targetMap;
	}

	/**
	 * 根据条件删除 map 中指定的key 值
	 * @param originalMap
	 * @param predicate
	 * @return
	 * @param <K>
	 * @param <V>
	 */
	public static <K, V> Map<K, V> removeKeys(Map<K, V> originalMap, Predicate<Map.Entry<K, V>> predicate) {
		Iterator<Entry<K, V>> iterator = originalMap.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<K, V> next = iterator.next();
			if (predicate.test(next)) {
				iterator.remove();
			}
		}
		return originalMap;
	}

	/**
	 * 判断 map 不能为空
	 * @param map
	 * @return
	 */
	public static <K, V> boolean isNotBlank(Map<K, V> map) {
		return Objects.nonNull(map) && !map.isEmpty();
	}

	/**
	 * 判断 map 为空
	 * @param map
	 * @return
	 */
	public static <K, V> boolean isBlank(Map<K, V> map) {
		return !isNotBlank(map);
	}

}
