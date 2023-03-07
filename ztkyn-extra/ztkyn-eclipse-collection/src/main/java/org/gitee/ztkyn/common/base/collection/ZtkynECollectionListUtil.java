package org.gitee.ztkyn.common.base.collection;

import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.impl.list.mutable.FastList;
import org.eclipse.collections.impl.list.primitive.IntInterval;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author whty
 * @version 1.0
 * @description eclipse Collection List 集合工具类
 * @date 2023/3/7 9:13
 */
public class ZtkynECollectionListUtil {

	private static final Logger logger = LoggerFactory.getLogger(ZtkynECollectionListUtil.class);

	/**
	 * 创建 可变 list ，具体实现是 FastList (ArrayList 的替代)
	 * @return
	 * @param <T>
	 */
	public static <T> FastList<T> createFastList() {
		return new FastList<>();
	}

	/**
	 * 创建 可变 list ，具体实现是 FastList (ArrayList 的替代)
	 * @param elements
	 * @return
	 * @param <T>
	 */
	@SafeVarargs
	public static <T> MutableList<T> createMutList(T... elements) {
		return Lists.mutable.with(elements);
	}

	/**
	 * 创建 不可变 list ，一旦创建不允许更改
	 * @param elements
	 * @return
	 * @param <T>
	 */
	@SafeVarargs
	public static <T> ImmutableList<T> createImmutList(T... elements) {
		return Lists.immutable.with(elements);
	}

	/**
	 * 原始 int 集合，不需要 包装类 Integer
	 * @param start
	 * @param end
	 * @return
	 */
	public static IntInterval createIntList(int start, int end) {
		return IntInterval.fromTo(start, end);
	}

	/**
	 * 原始 int 集合，不需要 包装类 Integer
	 * @param start
	 * @param end
	 * @param step
	 * @return
	 */
	public static IntInterval createIntList(int start, int end, int step) {
		return IntInterval.fromToBy(start, end, step);
	}

}
