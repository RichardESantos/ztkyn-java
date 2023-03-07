package org.gitee.ztkyn.common.base.collection;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author whty
 * @version 1.0
 * @description
 * @date 2023/3/7 10:44
 */
public class ZtkynListUtil {

	private static final Logger logger = LoggerFactory.getLogger(ZtkynListUtil.class);

	/**
	 * 创建 List ,使用 Eclipse Collection FastList 替代 ArrayList
	 * @return
	 * @param <T>
	 */
	public static <T> List<T> createFastList() {
		return ZtkynECollectionListUtil.createFastList();
	}

	/**
	 * 创建 List ,使用 Eclipse Collection FastList 替代 ArrayList
	 * @param elements
	 * @return
	 * @param <T>
	 */
	@SafeVarargs
	public static <T> List<T> createFastList(T... elements) {
		return ZtkynECollectionListUtil.createMutList(elements);
	}

}
