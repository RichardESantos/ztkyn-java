package org.gitee.ztkyn.common.base.collection;

import java.util.Arrays;
import java.util.Comparator;

import org.eclipse.collections.api.factory.Sets;
import org.eclipse.collections.api.set.MutableSet;
import org.eclipse.collections.api.set.sorted.MutableSortedSet;
import org.eclipse.collections.impl.set.mutable.UnifiedSet;
import org.eclipse.collections.impl.set.sorted.mutable.TreeSortedSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author whty
 * @version 1.0
 * @description
 * @date 2023/3/7 10:53
 */
public class ZtkynECollectionSetUtil {

	private static final Logger logger = LoggerFactory.getLogger(ZtkynECollectionSetUtil.class);

	/**
	 * 创建 可变 Set ，具体实现是 UnifiedSet (HashSet 的替代)
	 * @return
	 * @param <T>
	 */
	public static <T> UnifiedSet<T> createUnifiedSet() {
		return new UnifiedSet<>();
	}

	/**
	 * 创建 可变 Set ，具体实现是 UnifiedSet (HashSet 的替代)
	 * @param elements
	 * @return
	 * @param <T>
	 */
	@SafeVarargs
	public static <T> MutableSet<T> createMutableSet(T... elements) {
		return Sets.mutable.with(elements);
	}

	/**
	 * 创建 可变 Set ，具体实现是 TreeSortedSet (TreeSet 的替代)
	 * @return
	 * @param <T>
	 */
	public static <T> TreeSortedSet<T> createTreeSortedSet() {
		return new TreeSortedSet<>();
	}

	/**
	 * 创建 可变 Set ，具体实现是 TreeSortedSet (TreeSet 的替代)
	 * @param iterable 所有实现 Iterable 的集合
	 * @return
	 * @param <T>
	 */
	public static <T> TreeSortedSet<T> createTreeSortedSet(Iterable<T> iterable) {
		return new TreeSortedSet<>(iterable);
	}

	/**
	 * 创建 可变 Set ，具体实现是 TreeSortedSet (TreeSet 的替代)
	 * @return
	 * @param <T>
	 */
	public static <T> TreeSortedSet<T> createTreeSortedSet(Comparator<T> comparator) {
		return new TreeSortedSet<>(comparator);
	}

	/**
	 * 创建 可变 Set ，具体实现是 TreeSortedSet (TreeSet 的替代)
	 * @param elements
	 * @return
	 * @param <T>
	 */
	@SafeVarargs
	public static <T> MutableSortedSet<T> createMutableSortedSet(Comparator<T> comparator, T... elements) {
		TreeSortedSet<T> sortedSet = new TreeSortedSet<>(comparator);
		sortedSet.addAll(Arrays.asList(elements));
		return sortedSet;
	}

}
