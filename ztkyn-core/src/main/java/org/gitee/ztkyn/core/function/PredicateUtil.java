package org.gitee.ztkyn.core.function;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;

import org.gitee.ztkyn.core.string.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PredicateUtil {

	private static final Logger logger = LoggerFactory.getLogger(PredicateUtil.class);

	public static Predicate<Object> objectNotNull = Objects::nonNull;

	public static Predicate<String> strNotBlank = StringUtil::isNotBlank;

	public static <T> Predicate<Collection<T>> collectionNotBlank() {
		return collection -> Objects.nonNull(collection) && !collection.isEmpty();
	}

	public static <T> Predicate<List<T>> listNotBlank() {
		return list -> Objects.nonNull(list) && !list.isEmpty();
	}

	public static <T> Predicate<Set<T>> setNotBlank() {
		return set -> Objects.nonNull(set) && !set.isEmpty();
	}

	public static <K, V, T extends Map<K, V>> Predicate<Map<K, V>> mapNotBlank() {
		return map -> Objects.nonNull(map) && !map.isEmpty();
	}

	public static <K, V, T extends Map<K, V>> Predicate<Map<K, V>> mapIsBlank() {
		return map -> Objects.isNull(map) || map.isEmpty();
	}

}
