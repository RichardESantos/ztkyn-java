package org.gitee.ztkyn.core.exception;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.gitee.ztkyn.core.colleciton.CollectionUtil;
import org.gitee.ztkyn.core.function.DataProcessHandler;
import org.gitee.ztkyn.core.string.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author richard
 * @version 1.0
 */
public class AssertUtil {

	private static final Logger logger = LoggerFactory.getLogger(AssertUtil.class);

	/**
	 * 判断对象 ！=null
	 * @param obj
	 * @param errorMsg
	 */
	public static void objectNotNull(Object obj, String errorMsg) {
		DataProcessHandler.of(obj, Objects::nonNull).ifFalseThrow(new DataVerifyFailException(errorMsg));
	}

	/**
	 * 判断对象 == null
	 * @param obj
	 * @param errorMsg
	 */
	public static void objectIsNull(Object obj, String errorMsg) {
		DataProcessHandler.of(obj, Objects::isNull).ifFalseThrow(new DataVerifyFailException(errorMsg));
	}

	/**
	 * 判断字符串不能为null ，且内容不能为空
	 * @param str
	 * @param errorMsg
	 */
	public static void notBlank(String str, String errorMsg) {
		DataProcessHandler.of(str, StringUtil::isNotBlank).ifFalseThrow(new DataVerifyFailException(errorMsg));
	}

	/**
	 * 判断字符串为null ，或内容为空
	 * @param str
	 * @param errorMsg
	 */
	public static void isBlank(String str, String errorMsg) {
		DataProcessHandler.of(str, StringUtil::isBlank).ifFalseThrow(new DataVerifyFailException(errorMsg));
	}

	/**
	 * 判断map不能为null ，且内容不能为空
	 * @param list
	 * @param errorMsg
	 */
	public static <T> void notBlank(List<T> list, String errorMsg) {
		DataProcessHandler.of(list, CollectionUtil::notBlank).ifFalseThrow(new DataVerifyFailException(errorMsg));
	}

	/**
	 * 判断集合为null ，或内容为空
	 * @param list
	 * @param errorMsg
	 */
	public static <T> void isBlank(List<T> list, String errorMsg) {
		DataProcessHandler.of(list, CollectionUtil::isBlank).ifFalseThrow(new DataVerifyFailException(errorMsg));
	}

	/**
	 * 判断map不能为null ，且内容不能为空
	 * @param set
	 * @param errorMsg
	 */
	public static <T> void notBlank(Set<T> set, String errorMsg) {
		DataProcessHandler.of(set, CollectionUtil::notBlank).ifFalseThrow(new DataVerifyFailException(errorMsg));
	}

	/**
	 * 判断集合为null ，或内容为空
	 * @param set
	 * @param errorMsg
	 */
	public static <T> void isBlank(Set<T> set, String errorMsg) {
		DataProcessHandler.of(set, CollectionUtil::isBlank).ifFalseThrow(new DataVerifyFailException(errorMsg));
	}

	/**
	 * 判断map不能为null ，且内容不能为空
	 * @param map
	 * @param errorMsg
	 */
	public static <K, V> void notBlank(Map<K, V> map, String errorMsg) {
		DataProcessHandler.of(map, CollectionUtil::notBlank).ifFalseThrow(new DataVerifyFailException(errorMsg));
	}

	/**
	 * 判断集合为null ，或内容为空
	 * @param map
	 * @param errorMsg
	 * @param <K>
	 * @param <V>
	 */
	public static <K, V> void isBlank(Map<K, V> map, String errorMsg) {
		DataProcessHandler.of(map, CollectionUtil::isBlank).ifFalseThrow(new DataVerifyFailException(errorMsg));
	}

	/**
	 * 判断传进来的值是否是期望的值
	 * @param value
	 * @param expectValue
	 * @param errorMsg
	 * @param <T>
	 */
	public static <T> void expect(T value, T expectValue, String errorMsg) {
		if (!Objects.equals(value, expectValue)) {
			throw new DataVerifyFailException(errorMsg);
		}
	}

}
