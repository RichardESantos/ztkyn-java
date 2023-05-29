package org.gitee.ztkyn.core.colleciton;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;

import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.factory.Maps;
import org.eclipse.collections.api.factory.Sets;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.map.ImmutableMap;
import org.eclipse.collections.api.set.ImmutableSet;
import org.eclipse.collections.impl.list.mutable.FastList;
import org.eclipse.collections.impl.map.mutable.UnifiedMap;
import org.eclipse.collections.impl.map.sorted.mutable.TreeSortedMap;
import org.eclipse.collections.impl.set.mutable.UnifiedSet;
import org.eclipse.collections.impl.set.sorted.mutable.TreeSortedSet;
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
		return new FastList<>();
	}

	/**
	 * 创建 List ,使用 Eclipse Collection FastList 替代 ArrayList
	 * @param elements
	 * @return
	 * @param <T>
	 */
	@SafeVarargs
	public static <T> List<T> createFastList(T... elements) {
		return Lists.mutable.with(elements);
	}

	/**
	 * 创建不可变 list
	 * @param elements
	 * @return
	 * @param <T>
	 */
	@SafeVarargs
	public static <T> ImmutableList<T> createImmutableList(T... elements) {
		return Lists.immutable.with(elements);
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
		return Maps.immutable.with(key, value);
	}

	/**
	 * 创建不可变 map
	 * @param orgMap
	 * @return
	 * @param <K>
	 * @param <V>
	 */
	public static <K, V> ImmutableMap<K, V> createImmutableMap(Map<K, V> orgMap) {
		return Maps.immutable.withAll(orgMap);
	}

	/**
	 * 创建 可变 Map ，具体实现是 UnifiedMap (HashMap 的替代)
	 * @param size
	 * @return
	 * @param <K>
	 * @param <V>
	 */
	public static <K, V> Map<K, V> createUnifiedMap(int size) {
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
	public static <K, V> Map<K, V> createUnifiedMap(K key, V value) {
		return UnifiedMap.newWithKeysValues(key, value);
	}

	/**
	 * 创建 可变 SortedMap ，具体实现是 TreeSortedMap (TreeMap 的替代)
	 * @param comparator
	 * @return
	 * @param <K>
	 * @param <V>
	 */
	public static <K, V> SortedMap<K, V> createTreeSortedMap(Comparator<K> comparator) {
		return TreeSortedMap.newMap(comparator);
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
		return TreeSortedMap.newMapWith(comparator, key, value);
	}

	/**
	 * 创建 Set ,使用 Eclipse Collection UnifiedSet 替代 HashSet
	 * @return
	 * @param <T>
	 */
	public static <T> Set<T> createUnifiedSet() {
		return new UnifiedSet<>();
	}

	/**
	 * 创建 Set ,使用 Eclipse Collection UnifiedSet 替代 HashSet
	 * @param elements
	 * @return
	 * @param <T>
	 */
	@SafeVarargs
	public static <T> Set<T> createMutSet(T... elements) {
		return Sets.mutable.with(elements);
	}

	/**
	 * 创建 可变 SortedSet ，具体实现是 TreeSortedSet (TreeSet 的替代)
	 * @return
	 * @param <T>
	 */
	public static <T> SortedSet<T> createTreeSortedSet() {
		return new TreeSortedSet<>();
	}

	/**
	 * 创建 可变 SortedSet ，具体实现是 TreeSortedSet (TreeSet 的替代)
	 * @param iterable 所有实现 Iterable 的集合
	 * @return
	 * @param <T>
	 */
	public static <T> SortedSet<T> createTreeSortedSet(Iterable<T> iterable) {
		return new TreeSortedSet<>(iterable);
	}

	/**
	 * 创建 可变 SortedSet ，具体实现是 TreeSortedSet (TreeSet 的替代)
	 * @return
	 * @param <T>
	 */
	public static <T> SortedSet<T> createTreeSortedSet(Comparator<T> comparator) {
		return new TreeSortedSet<>(comparator);
	}

	/**
	 * 不可变 set 集合
	 * @param elements
	 * @return
	 * @param <T>
	 */
	@SafeVarargs
	public static <T> ImmutableSet<T> createImmutableSet(T... elements) {
		return Sets.immutable.with(elements);
	}

}
