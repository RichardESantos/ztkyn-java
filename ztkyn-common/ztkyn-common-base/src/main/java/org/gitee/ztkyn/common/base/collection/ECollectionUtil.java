package org.gitee.ztkyn.common.base.collection;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.function.Predicate;

import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.map.ImmutableMap;
import org.eclipse.collections.api.set.ImmutableSet;
import org.gitee.ztkyn.core.colleciton.MapUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author whty
 * @version 1.0.0
 * @date 2023-05-04 18:21
 * @description ECollectionUtil
 */
public class ECollectionUtil {

	private static final Logger logger = LoggerFactory.getLogger(ECollectionUtil.class);

	/**
	 * 创建 List ,使用 Eclipse Collection FastList 替代 ArrayList
	 * @return
	 * @param <T>
	 */
	public static <T> List<T> createFastList() {
		return ECollectionListUtil.createFastList();
	}

	/**
	 * 创建 List ,使用 Eclipse Collection FastList 替代 ArrayList
	 * @param elements
	 * @return
	 * @param <T>
	 */
	@SafeVarargs
	public static <T> List<T> createFastList(T... elements) {
		return ECollectionListUtil.createMutList(elements);
	}

	/**
	 * 创建不可变 list
	 * @param elements
	 * @return
	 * @param <T>
	 */
	@SafeVarargs
	public static <T> ImmutableList<T> createImmutableList(T... elements) {
		return ECollectionListUtil.createImmutList(elements);
	}

	/**
	 * 创建不可变 map
	 * @param key
	 * @param value
	 * @return
	 * @param <K>
	 * @param <V>
	 */
	public static <K, V> ImmutableMap<K, V> createImmutableMap(K key, V value) {
		return ECollectionMapUtil.createImmutableMap(key, value);
	}

	/**
	 * 创建不可变 map
	 * @param orgMap
	 * @return
	 * @param <K>
	 * @param <V>
	 */
	public static <K, V> ImmutableMap<K, V> createImmutableMap(Map<K, V> orgMap) {
		return ECollectionMapUtil.createImmutableMap(orgMap);
	}

	/**
	 * 创建 可变 Map ，具体实现是 UnifiedMap (HashMap 的替代)
	 * @param size
	 * @return
	 * @param <K>
	 * @param <V>
	 */
	public static <K, V> Map<K, V> createUnifiedMap(int size) {
		return ECollectionMapUtil.createUnifiedMap(size);
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
		return ECollectionMapUtil.createUnifiedMap(key, value);
	}

	/**
	 * 创建 可变 SortedMap ，具体实现是 TreeSortedMap (TreeMap 的替代)
	 * @param comparator
	 * @return
	 * @param <K>
	 * @param <V>
	 */
	public static <K, V> SortedMap<K, V> createTreeSortedMap(Comparator<K> comparator) {
		return ECollectionMapUtil.createTreeSortedMap(comparator);
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
		return ECollectionMapUtil.createTreeSortedMap(comparator, key, value);
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
	public static <K, V> Map<K, V> removeKeys(Map<K, V> originalMap, Predicate<Map.Entry<K, V>> predicate) {
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

	/**
	 * 创建 Set ,使用 Eclipse Collection UnifiedSet 替代 HashSet
	 * @return
	 * @param <T>
	 */
	public static <T> Set<T> createUnifiedSet() {
		return ECollectionSetUtil.createUnifiedSet();
	}

	/**
	 * 创建 Set ,使用 Eclipse Collection UnifiedSet 替代 HashSet
	 * @param elements
	 * @return
	 * @param <T>
	 */
	@SafeVarargs
	public static <T> Set<T> createMutSet(T... elements) {
		return ECollectionSetUtil.createMutableSet(elements);
	}

	/**
	 * 创建 可变 SortedSet ，具体实现是 TreeSortedSet (TreeSet 的替代)
	 * @return
	 * @param <T>
	 */
	public static <T> SortedSet<T> createTreeSortedSet() {
		return ECollectionSetUtil.createTreeSortedSet();
	}

	/**
	 * 创建 可变 SortedSet ，具体实现是 TreeSortedSet (TreeSet 的替代)
	 * @param iterable 所有实现 Iterable 的集合
	 * @return
	 * @param <T>
	 */
	public static <T> SortedSet<T> createTreeSortedSet(Iterable<T> iterable) {
		return ECollectionSetUtil.createTreeSortedSet(iterable);
	}

	/**
	 * 创建 可变 SortedSet ，具体实现是 TreeSortedSet (TreeSet 的替代)
	 * @return
	 * @param <T>
	 */
	public static <T> SortedSet<T> createTreeSortedSet(Comparator<T> comparator) {
		return ECollectionSetUtil.createTreeSortedSet(comparator);
	}

	/**
	 * ImmutableSet
	 * @param elements
	 * @return
	 * @param <T>
	 */
	@SafeVarargs
	public static <T> ImmutableSet<T> createImmutableSet(T... elements) {
		return ECollectionSetUtil.createImmutableSet(elements);
	}

}
