package org.gitee.ztkyn.core.colleciton;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author whty
 * @version 1.0
 * @description
 * @date 2023/3/6 16:14
 */
public class SetUtil {

	private static final Logger logger = LoggerFactory.getLogger(SetUtil.class);

	/**
	 * 生成 HashSet
	 * @param element
	 * @return
	 * @param <T>
	 */
	public static <T> HashSet<T> newHashSet(T element) {
		return new HashSet<>() {
			{
				add(element);
			}
		};
	}

	/**
	 * 生成 HashSet
	 * @param elements
	 * @return
	 * @param <T>
	 */
	@SafeVarargs
	public static <T> HashSet<T> newHashSet(T... elements) {
		return new HashSet<>(Arrays.asList(elements));
	}

	/**
	 * 生成 TreeSet, 使用自然排序 或者 T 实现的 Comparable 接口
	 * @param elements
	 * @return
	 * @param <T>
	 */
	@SafeVarargs
	public static <T> TreeSet<T> newTreeSet(T... elements) {
		return new TreeSet<>(Arrays.asList(elements));
	}

	/**
	 * 生成 TreeSet ,使用指定的 comparator
	 * @param comparator
	 * @param elements
	 * @return
	 * @param <T>
	 */
	@SafeVarargs
	public static <T> TreeSet<T> newTreeSet(Comparator<T> comparator, T... elements) {
		TreeSet<T> treeSet = new TreeSet<>(comparator);
		treeSet.addAll(Arrays.asList(elements));
		return treeSet;
	}

}
