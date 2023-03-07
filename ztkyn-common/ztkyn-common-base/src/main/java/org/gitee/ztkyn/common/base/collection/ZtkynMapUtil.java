package org.gitee.ztkyn.common.base.collection;

import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.function.Predicate;

import org.gitee.ztkyn.core.colleciton.MapUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author whty
 * @version 1.0
 * @description
 * @date 2023/3/7 11:18
 */
public class ZtkynMapUtil {

	private static final Logger logger = LoggerFactory.getLogger(ZtkynMapUtil.class);

	/**
	 * 创建 可变 Map ，具体实现是 UnifiedMap (HashMap 的替代)
	 * @param size
	 * @return
	 * @param <K>
	 * @param <V>
	 */
	public static <K, V> Map<K, V> createUnifiedMap(int size) {
		return ZtkynECollectionMapUtil.createUnifiedMap(size);
	}

	/**
	 * 创建 可变 Map ，具体实现是 UnifiedMap (HashMap 的替代)
	 * @param key
	 * @param value
	 * @return
	 * @param <K>
	 * @param <V>
	 */
	public static <K, V> Map<K, V> createUnifiedMap(K key, V value) {
		return ZtkynECollectionMapUtil.createUnifiedMap(key, value);
	}

	/**
	 * 创建 可变 SortedMap ，具体实现是 TreeSortedMap (TreeMap 的替代)
	 * @param comparator
	 * @return
	 * @param <K>
	 * @param <V>
	 */
	public static <K, V> SortedMap<K, V> createTreeSortedMap(Comparator<K> comparator) {
		return ZtkynECollectionMapUtil.createTreeSortedMap(comparator);
	}

	/**
	 * 创建 可变 SortedMap ，具体实现是 TreeSortedMap (TreeMap 的替代)
	 * @param comparator
	 * @param key
	 * @param value
	 * @return
	 * @param <K>
	 * @param <V>
	 */
	public static <K, V> SortedMap<K, V> createTreeSortedMap(Comparator<K> comparator, K key, V value) {
		return ZtkynECollectionMapUtil.createTreeSortedMap(comparator, key, value);
	}

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
		return MapUtil.retainKeysWithNullValue(originalMap, retainKeys, retainNullValue);
	}

	/**
	 * 根据条件删除 map 中指定的key 值
	 * @param originalMap
	 * @param predicate
	 * @return
	 * @param <K>
	 * @param <V>
	 */
	public static <K, V> Map<K, V> removeKeys(Map<K, V> originalMap, Predicate<Entry<K, V>> predicate) {
		return MapUtil.removeKeys(originalMap, predicate);
	}

	/**
	 * 判断 map 不能为空
	 * @param map
	 * @return
	 */
	public static <K, V> boolean isNotBlank(Map<K, V> map) {
		return MapUtil.isNotBlank(map);
	}

	/**
	 * 判断 map 为空
	 * @param map
	 * @return
	 */
	public static <K, V> boolean isBlank(Map<K, V> map) {
		return MapUtil.isNotBlank(map);
	}

}
