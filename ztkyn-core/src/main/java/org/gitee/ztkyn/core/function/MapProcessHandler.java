package org.gitee.ztkyn.core.function;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MapProcessHandler<K, V> {

	private static final Logger logger = LoggerFactory.getLogger(MapProcessHandler.class);

	private final Map<K, V> value;

	private boolean judgeResult = false;

	private MapProcessHandler(Map<K, V> value) {
		this.value = value;
	}

	public static <K, V> MapProcessHandler<K, V> of(Map<K, V> list, Predicate<Map<K, V>> predicate) {
		return new MapProcessHandler<>(list).judge(predicate);
	}

	private MapProcessHandler<K, V> judge(Predicate<Map<K, V>> predicate) {
		judgeResult = predicate.test(value);
		return this;
	}

	public MapProcessHandler<K, V> ifTrue(Consumer<Map<K, V>> consumer) {
		if (judgeResult) {
			consumer.accept(value);
		}
		return this;
	}

	public void ifFalse(Consumer<Map<K, V>> consumer) {
		if (!judgeResult) {
			consumer.accept(value);
		}
	}

	public void ifFalseThrow(RuntimeException exception) {
		ifFalse(value -> {
			throw exception;
		});
	}

}
