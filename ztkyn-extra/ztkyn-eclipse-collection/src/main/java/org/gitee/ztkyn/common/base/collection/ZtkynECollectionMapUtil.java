package org.gitee.ztkyn.common.base.collection;

import java.util.Comparator;

import org.eclipse.collections.impl.map.mutable.UnifiedMap;
import org.eclipse.collections.impl.map.sorted.mutable.TreeSortedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author whty
 * @version 1.0
 * @description eclipse Collection Map 集合工具类
 * @date 2023/3/7 10:38
 */
public class ZtkynECollectionMapUtil {

	private static final Logger logger = LoggerFactory.getLogger(ZtkynECollectionMapUtil.class);

	/**
	 * 创建 可变 Map ，具体实现是 UnifiedMap (HashMap 的替代)
	 * @param size
	 * @return
	 * @param <K>
	 * @param <V>
	 */
	public static <K, V> UnifiedMap<K, V> createUnifiedMap(int size) {
		return UnifiedMap.newMap(size);
	}

	/**
	 * 创建 可变 Map ，具体实现是 UnifiedMap (HashMap 的替代)
	 * @param key
	 * @param value
	 * @return
	 * @param <K>
	 * @param <V>
	 */
	public static <K, V> UnifiedMap<K, V> createUnifiedMap(K key, V value) {
		return UnifiedMap.newWithKeysValues(key, value);
	}

	/**
	 * 创建 可变 MutableSortedMap ，具体实现是 TreeSortedMap (TreeMap 的替代)
	 * @param comparator
	 * @return
	 * @param <K>
	 * @param <V>
	 */
	public static <K, V> TreeSortedMap<K, V> createTreeSortedMap(Comparator<K> comparator) {
		return TreeSortedMap.newMap(comparator);
	}

	/**
	 * 创建 可变 MutableSortedMap ，具体实现是 TreeSortedMap (TreeMap 的替代)
	 * @param comparator
	 * @param key
	 * @param value
	 * @return
	 * @param <K>
	 * @param <V>
	 */
	public static <K, V> TreeSortedMap<K, V> createTreeSortedMap(Comparator<K> comparator, K key, V value) {
		return TreeSortedMap.newMapWith(comparator, key, value);
	}

}
