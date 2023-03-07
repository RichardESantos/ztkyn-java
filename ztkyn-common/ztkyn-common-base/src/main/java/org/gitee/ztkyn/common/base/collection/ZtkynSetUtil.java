package org.gitee.ztkyn.common.base.collection;

import java.util.Comparator;
import java.util.Set;
import java.util.SortedSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author whty
 * @version 1.0
 * @description
 * @date 2023/3/7 10:52
 */
public class ZtkynSetUtil {

	private static final Logger logger = LoggerFactory.getLogger(ZtkynSetUtil.class);

	/**
	 * 创建 Set ,使用 Eclipse Collection UnifiedSet 替代 HashSet
	 * @return
	 * @param <T>
	 */
	public static <T> Set<T> createUnifiedSet() {
		return ZtkynECollectionSetUtil.createUnifiedSet();
	}

	/**
	 * 创建 Set ,使用 Eclipse Collection UnifiedSet 替代 HashSet
	 * @param elements
	 * @return
	 * @param <T>
	 */
	@SafeVarargs
	public static <T> Set<T> createMutSet(T... elements) {
		return ZtkynECollectionSetUtil.createMutableSet(elements);
	}

	/**
	 * 创建 可变 SortedSet ，具体实现是 TreeSortedSet (TreeSet 的替代)
	 * @return
	 * @param <T>
	 */
	public static <T> SortedSet<T> createTreeSortedSet() {
		return ZtkynECollectionSetUtil.createTreeSortedSet();
	}

	/**
	 * 创建 可变 SortedSet ，具体实现是 TreeSortedSet (TreeSet 的替代)
	 * @param iterable 所有实现 Iterable 的集合
	 * @return
	 * @param <T>
	 */
	public static <T> SortedSet<T> createTreeSortedSet(Iterable<T> iterable) {
		return ZtkynECollectionSetUtil.createTreeSortedSet(iterable);
	}

	/**
	 * 创建 可变 SortedSet ，具体实现是 TreeSortedSet (TreeSet 的替代)
	 * @return
	 * @param <T>
	 */
	public static <T> SortedSet<T> createTreeSortedSet(Comparator<T> comparator) {
		return ZtkynECollectionSetUtil.createTreeSortedSet(comparator);
	}

}
