package org.gitee.ztkyn.core.colleciton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author whty
 * @version 1.0
 */
public class CollectionUtil {

	private static final Logger logger = LoggerFactory.getLogger(CollectionUtil.class);

	/**
	 * 判断集合不能为空
	 * @param collection
	 * @param <T>
	 * @return
	 */
	public static <T> boolean isNotBlank(Collection<T> collection) {
		return Objects.nonNull(collection) && !collection.isEmpty();
	}

	/**
	 * 判断集合是否为空
	 * @param collection
	 * @param <T>
	 * @return
	 */
	public static <T> boolean isBlank(Collection<T> collection) {
		return !isNotBlank(collection);
	}

	/**
	 * 比较两个 集合 中的元素是否完全一致（数量，内容同时一致），支持两个集合不是同类型
	 * @param first
	 * @param second
	 * @param <T>
	 * @return
	 */
	public static <T> boolean isIdentical(Collection<T> first, Collection<T> second) {
		if (isNotBlank(first) && isNotBlank(second)) {
			if (first.size() != second.size())
				return false;
			return first.containsAll(second) && second.containsAll(first);
		}
		else {
			return first == null && second == null;
		}
	}

}
