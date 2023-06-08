package org.gitee.ztkyn.core.function;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;

import org.gitee.ztkyn.core.colleciton.CollectionUtil;
import org.gitee.ztkyn.core.string.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PredicateUtil {

	private static final Logger logger = LoggerFactory.getLogger(PredicateUtil.class);

	public static Predicate<String> strNotBlank = StringUtil::isNotBlank;

	public static <T> Predicate<T> notNull() {
		return Objects::nonNull;
	}

	public static <T> Predicate<List<T>> listNotBlank() {
		return CollectionUtil::notBlank;
	}

	public static <T> Predicate<List<T>> listIsBlank() {
		return CollectionUtil::isBlank;
	}

	public static <T> Predicate<Set<T>> setNotBlank() {
		return CollectionUtil::notBlank;
	}

	public static <T> Predicate<Set<T>> setIsBlank() {
		return CollectionUtil::isBlank;
	}

	public static <K, V, T extends Map<K, V>> Predicate<Map<K, V>> mapNotBlank() {
		return CollectionUtil::notBlank;
	}

	public static <K, V, T extends Map<K, V>> Predicate<Map<K, V>> mapIsBlank() {
		return CollectionUtil::isBlank;
	}

}
