package org.gitee.ztkyn.core.function;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.gitee.ztkyn.core.string.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author richard
 * @version 1.0
 */
public final class SomeObj<T> {

    private static final Logger logger = LoggerFactory.getLogger(SomeObj.class);

    private final T value;

    private final boolean judgeResult;

    private SomeObj(T value, boolean judgeResult) {
        this.value = value;
        this.judgeResult = judgeResult;
    }

    /**
     * 借用外部判断结果，可以与 value 无关
     *
     * @param t
     * @param judgeResult
     * @param <T>
     * @return
     */
    public static <T> SomeObj<T> of(T t, boolean judgeResult) {
        return new SomeObj<>(t, judgeResult);
    }

    /**
     * 使用针对于 value 本身的判断，
     *
     * @param t
     * @param predicate
     * @param <T>
     * @return
     */
    public static <T> SomeObj<T> of(T t, Predicate<T> predicate) {
        return new SomeObj<>(t, predicate.test(t));
    }

    /**
     * 简写方法
     *
     * @param t
     * @param <T>
     * @return
     */
    public static <T> SomeObj<T> notNull(T t) {
        return new SomeObj<>(t, Objects.nonNull(t));
    }

    /**
     * 简写方法
     *
     * @param t
     * @param <T>
     * @return
     */
    public static <T> SomeObj<T> isNull(T t) {
        return new SomeObj<>(t, Objects.isNull(t));
    }

    /**
     * 简写方法
     *
     * @param content
     * @return
     */
    public static SomeObj<String> notBlank(String content) {
        return new SomeObj<>(content, StringUtil.isNotBlank(content));
    }

    public static <E> SomeObj<List<E>> notBlank(List<E> list) {
        return new SomeObj<>(list, Objects.nonNull(list) && !list.isEmpty());
    }

    /**
     * 简写方法
     *
     * @param content
     * @return
     */
    public static SomeObj<String> isBlank(String content) {
        return new SomeObj<>(content, StringUtil.isBlank(content));
    }

    public SomeObj<T> ifTrue(Consumer<T> consumer) {
        if (judgeResult) {
            consumer.accept(value);
        }
        return this;
    }

    public SomeObj<T> ifTrue(Runnable runnable) {
        if (judgeResult) {
            runnable.run();
        }
        return this;
    }

    public void ifFalse(Consumer<T> consumer) {
        if (!judgeResult) {
            consumer.accept(value);
        }
    }

    public void ifFalse(Runnable runnable) {
        if (!judgeResult) {
            runnable.run();
        }
    }

    public void ifFalseThrow(RuntimeException exception) {
        ifFalse(value -> {
            throw exception;
        });
    }

    public T getValue() {
        return value;
    }

    public boolean isJudgeResult() {
        return judgeResult;
    }

}
