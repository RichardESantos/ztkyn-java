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
import org.eclipse.collections.api.factory.SortedMaps;
import org.eclipse.collections.api.factory.SortedSets;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.map.ImmutableMap;
import org.eclipse.collections.api.map.sorted.ImmutableSortedMap;
import org.eclipse.collections.api.set.ImmutableSet;
import org.eclipse.collections.api.set.sorted.ImmutableSortedSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author richard
 * @version 1.0.0
 * @date 2023-05-04 18:21
 * @description ECollectionUtil
 */
public class ECollectionUtil {

	private static final Logger logger = LoggerFactory.getLogger(ECollectionUtil.class);

	/**
	 * 不可变集合
	 */
	public static class Immutable {

		/**
		 * 创建不可变 list,上层接口不是List
		 * @param elements
		 * @param <T>
		 * @return
		 */
		@SafeVarargs
		public static <T> ImmutableList<T> newList(T... elements) {
			return Lists.immutable.with(elements);
		}

		/**
		 * 创建不可变 list,上层接口不是List
		 * @param iterable
		 * @param <T>
		 * @return
		 */
		public static <T> ImmutableList<T> newList(Iterable<? extends T> iterable) {
			return Lists.immutable.ofAll(iterable);
		}

		/**
		 * 不可变 set 集合,上层接口不是Set
		 * @param elements
		 * @param <T>
		 * @return
		 */
		@SafeVarargs
		public static <T> ImmutableSet<T> newSet(T... elements) {
			return Sets.immutable.with(elements);
		}

		/**
		 * 不可变 set 集合,上层接口不是Set
		 * @param iterable
		 * @param <T>
		 * @return
		 */
		public static <T> ImmutableSet<T> newSet(Iterable<? extends T> iterable) {
			return Sets.immutable.ofAll(iterable);
		}

		/**
		 * 不可变 set 集合,上层接口不是Set
		 * @param elements
		 * @param <T>
		 * @return
		 */
		@SafeVarargs
		public static <T> ImmutableSortedSet<T> newSortedSet(T... elements) {
			return SortedSets.immutable.with(elements);
		}

		/**
		 * 不可变 set 集合,上层接口不是Set
		 * @param iterable
		 * @param <T>
		 * @return
		 */
		public static <T> ImmutableSortedSet<T> newSortedSet(Iterable<? extends T> iterable) {
			return SortedSets.immutable.ofAll(iterable);
		}

		/**
		 * 创建不可变 map,上层接口不是Map
		 * @param key
		 * @param value
		 * @param <K>
		 * @param <V>
		 * @return
		 */
		public static <K, V> ImmutableMap<K, V> newMap(K key, V value) {
			return Maps.immutable.with(key, value);
		}

		/**
		 * 创建不可变 map,上层接口不是Map
		 * @param orgMap
		 * @param <K>
		 * @param <V>
		 * @return
		 */
		public static <K, V> ImmutableMap<K, V> newMap(Map<K, V> orgMap) {
			return Maps.immutable.withAll(orgMap);
		}

		/**
		 * 创建不可变 map,上层接口不是Map
		 * @param key
		 * @param value
		 * @param <K>
		 * @param <V>
		 * @return
		 */
		public static <K, V> ImmutableSortedMap<K, V> newSortedMap(K key, V value) {
			return SortedMaps.immutable.with(key, value);
		}

		/**
		 * 创建不可变 map,上层接口不是Map
		 * @param orgMap
		 * @param <K>
		 * @param <V>
		 * @return
		 */
		public static <K, V> ImmutableSortedMap<K, V> newSortedMap(SortedMap<K, V> orgMap) {
			return SortedMaps.immutable.ofSortedMap(orgMap);
		}

	}

	public static class MutableList {

		/**
		 * 创建 List ,使用 Eclipse Collection FastList 替代 ArrayList
		 * @param <T>
		 * @return
		 */
		public static <T> List<T> newList() {
			return Lists.mutable.empty();
		}

		/**
		 * 创建 List ,使用 Eclipse Collection FastList 替代 ArrayList
		 * @param <T>
		 * @return
		 */
		public static <T> List<T> newList(int size) {
			return Lists.mutable.ofInitialCapacity(size);
		}

		/**
		 * 创建 List ,使用 Eclipse Collection FastList 替代 ArrayList
		 * @param elements
		 * @param <T>
		 * @return
		 */
		@SafeVarargs
		public static <T> List<T> newList(T... elements) {
			return Lists.mutable.with(elements);
		}

		/**
		 * 创建 List ,使用 Eclipse Collection FastList 替代 ArrayList
		 * @param iterable
		 * @param <T>
		 * @return
		 */
		public static <T> List<T> newList(Iterable<? extends T> iterable) {
			return Lists.mutable.ofAll(iterable);
		}

	}

	public static class MutableSet {

		/**
		 * 创建 Set ,使用 Eclipse Collection UnifiedSet 替代 HashSet
		 * @param <T>
		 * @return
		 */
		public static <T> Set<T> newSet() {
			return Sets.mutable.empty();
		}

		/**
		 * 创建 Set ,使用 Eclipse Collection UnifiedSet 替代 HashSet
		 * @param <T>
		 * @return
		 */
		public static <T> Set<T> newSet(int size) {
			return Sets.mutable.ofInitialCapacity(size);
		}

		/**
		 * 创建 Set ,使用 Eclipse Collection UnifiedSet 替代 HashSet
		 * @param elements
		 * @param <T>
		 * @return
		 */
		@SafeVarargs
		public static <T> Set<T> newSet(T... elements) {
			return Sets.mutable.with(elements);
		}

		/**
		 * 创建 Set ,使用 Eclipse Collection UnifiedSet 替代 HashSet
		 * @param iterable
		 * @param <T>
		 * @return
		 */
		public static <T> Set<T> newSet(Iterable<? extends T> iterable) {
			return Sets.mutable.ofAll(iterable);
		}

		/**
		 * 创建 可变 SortedSet ，具体实现是 TreeSortedSet (TreeSet 的替代)
		 * @param <T>
		 * @return
		 */
		public static <T> SortedSet<T> newSortedSet() {
			return SortedSets.mutable.empty();
		}

		/**
		 * 创建 可变 SortedSet ，具体实现是 TreeSortedSet (TreeSet 的替代)
		 * @param iterable 所有实现 Iterable 的集合
		 * @param <T>
		 * @return
		 */
		public static <T> SortedSet<T> newSortedSet(Iterable<T> iterable) {
			return SortedSets.mutable.ofAll(iterable);
		}

		/**
		 * 创建 可变 SortedSet ，具体实现是 TreeSortedSet (TreeSet 的替代)
		 * @param <T>
		 * @return
		 */
		public static <T> SortedSet<T> newSortedSet(Comparator<T> comparator) {
			return SortedSets.mutable.of(comparator);
		}

		/**
		 * 创建 可变 SortedSet ，具体实现是 TreeSortedSet (TreeSet 的替代)
		 * @param <T>
		 * @return
		 */
		public static <T> SortedSet<T> newSortedSet(Comparator<T> comparator, Iterable<T> iterable) {
			return SortedSets.mutable.ofAll(comparator, iterable);
		}

	}

	public static class MutableMap {

		/**
		 * 创建 可变 Map ，具体实现是 UnifiedMap (HashMap 的替代)
		 * @param <K>
		 * @param <V>
		 * @return
		 */
		public static <K, V> Map<K, V> newMap() {
			return Maps.mutable.empty();
		}

		/**
		 * 创建 可变 Map ，具体实现是 UnifiedMap (HashMap 的替代)
		 * @param size
		 * @param <K>
		 * @param <V>
		 * @return
		 */
		public static <K, V> Map<K, V> newMap(int size) {
			return Maps.mutable.ofInitialCapacity(size);
		}

		/**
		 * 创建 可变 Map ，具体实现是 UnifiedMap (HashMap 的替代)
		 * @param key
		 * @param value
		 * @param <K>
		 * @param <V>
		 * @return
		 */
		public static <K, V> Map<K, V> newMap(K key, V value) {
			return Maps.mutable.of(key, value);
		}

		/**
		 * 创建 可变 Map ，具体实现是 UnifiedMap (HashMap 的替代)
		 * @param map
		 * @param <K>
		 * @param <V>
		 * @return
		 */
		public static <K, V> Map<K, V> newMap(Map<K, V> map) {
			return Maps.mutable.ofMap(map);
		}

		/**
		 * 创建 可变 SortedMap ，具体实现是 TreeSortedMap (TreeMap 的替代)
		 * @param comparator
		 * @param <K>
		 * @param <V>
		 * @return
		 */
		public static <K, V> SortedMap<K, V> newSortedMap(Comparator<K> comparator) {
			return SortedMaps.mutable.of(comparator);
		}

		/**
		 * 创建 可变 SortedMap ，具体实现是 TreeSortedMap (TreeMap 的替代)
		 * @param <V>
		 * @return
		 */
		public static <V> SortedMap<String, V> stringSortedMap() {
			return SortedMaps.mutable.of(String::compareTo);
		}

		/**
		 * 创建 可变 SortedMap ，具体实现是 TreeSortedMap (TreeMap 的替代)
		 * @param <V>
		 * @return
		 */
		public static <V> SortedMap<Integer, V> integerSortedMap() {
			return SortedMaps.mutable.of(Integer::compareTo);
		}

		/**
		 * 创建 可变 SortedMap ，具体实现是 TreeSortedMap (TreeMap 的替代)
		 * @param comparator
		 * @param key
		 * @param value
		 * @param <K>
		 * @param <V>
		 * @return
		 */
		public static <K, V> SortedMap<K, V> newSortedMap(Comparator<K> comparator, K key, V value) {
			return SortedMaps.mutable.of(comparator, key, value);
		}

	}

}
