package org.gitee.ztkyn.core.exception;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

import org.gitee.ztkyn.core.function.DataProcessHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author whty
 * @version 1.0
 * @description 数据断言工具类
 * @date 2022/11/7 11:27
 */
public class AssertUtil {

	private static final Logger logger = LoggerFactory.getLogger(AssertUtil.class);

	/**
	 * 判断对象 ！=null
	 * @param obj
	 * @param errorMsg
	 */
	public static void objectNotNull(Object obj, String errorMsg) {
		DataProcessHandler.of(obj, Objects::nonNull).ifFalseThrow(new RuntimeException(errorMsg));
	}

	/**
	 * 判断对象 == null
	 * @param obj
	 * @param errorMsg
	 */
	public static void objectIsNull(Object obj, String errorMsg) {
		DataProcessHandler.of(obj, Objects::isNull).ifFalseThrow(new RuntimeException(errorMsg));
	}

	/**
	 * 判断字符串不能为null ，且内容不能为空
	 * @param str
	 * @param errorMsg
	 */
	public static void strNotBlank(String str, String errorMsg) {
		DataProcessHandler.of(str, StringUtil::isNotBlank).ifFalseThrow(new RuntimeException(errorMsg));
	}

	/**
	 * 判断字符串为null ，或内容为空
	 * @param str
	 * @param errorMsg
	 */
	public static void strIsBlank(String str, String errorMsg) {
		DataProcessHandler.of(str, StringUtil::isBlank).ifFalseThrow(new RuntimeException(errorMsg));
	}

	/**
	 * 判断集合不能为null ，且内容不能为空
	 * @param collection
	 * @param errorMsg
	 */
	public static <T> void collectionNotBlank(Collection<T> collection, String errorMsg) {
		DataProcessHandler.of(collection, CollectionUtil::isNotBlank).ifFalseThrow(new RuntimeException(errorMsg));
	}

	/**
	 * 判断集合为null ，或内容为空
	 * @param collection
	 * @param errorMsg
	 * @param <T>
	 */
	public static <T> void collectionIsBlank(Collection<T> collection, String errorMsg) {
		DataProcessHandler.of(collection, CollectionUtil::isBlank).ifFalseThrow(new RuntimeException(errorMsg));
	}

	/**
	 * 判断map不能为null ，且内容不能为空
	 * @param map
	 * @param errorMsg
	 */
	public static <K, V> void collectionNotBlank(Map<K, V> map, String errorMsg) {
		DataProcessHandler.of(map, CollectionUtil::isNotBlank).ifFalseThrow(new RuntimeException(errorMsg));
	}

	/**
	 * 判断集合为null ，或内容为空
	 * @param map
	 * @param errorMsg
	 * @param <K>
	 * @param <V>
	 */
	public static <K, V> void collectionIsBlank(Map<K, V> map, String errorMsg) {
		DataProcessHandler.of(map, CollectionUtil::isBlank).ifFalseThrow(new RuntimeException(errorMsg));
	}

}
