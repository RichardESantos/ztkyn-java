package org.gitee.ztkyn.core.function;

/**
 * 二元判断条件
 *
 * @param <T>
 * @param <U>
 */
@FunctionalInterface
public interface BiPredicate<T, U> {

	boolean test(T t, U u);

}
