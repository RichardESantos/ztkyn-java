package org.gitee.ztkyn.core.colleciton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author whty
 * @version 1.0
 * @description
 * @date 2023/3/6 16:07
 */
public class ListUtil {

	private static final Logger logger = LoggerFactory.getLogger(ListUtil.class);

	/**
	 * 生成 ArrayList
	 * @param element
	 * @return
	 * @param <T>
	 */
	public static <T> ArrayList<T> newArrayList(T element) {
		return new ArrayList<>() {
			{
				add(element);
			}
		};
	}

	/**
	 * 生成 ArrayList
	 * @param elements
	 * @return
	 * @param <T>
	 */
	@SafeVarargs
	public static <T> ArrayList<T> newArrayList(T... elements) {
		return new ArrayList<>(Arrays.asList(elements));
	}

	/**
	 * list 切片,以指定步长 length 切片
	 * @param list
	 * @param length
	 * @return
	 * @param <T>
	 */
	public static <T> List<List<T>> partition(List<T> list, int length) {
		List<List<T>> partitionList = new ArrayList<>();
		int size = list.size();
		for (int i = 0; i < size; i++) {
			if (i + length > size) {
				partitionList.add(list.subList(i, size));
			}
			else {
				partitionList.add(list.subList(i, length));
			}
		}
		return partitionList;
	}

}
