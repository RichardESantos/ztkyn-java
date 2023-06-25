package org.gitee.ztkyn.boot.framework.util;

import java.text.MessageFormat;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import me.ahoo.cosid.IdGenerator;
import me.ahoo.cosid.provider.IdGeneratorProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author richard
 * @date 2023-06-25 10:48
 * @description CosIdHelper
 * @version 1.0.0
 */
public class CosIdHelper {

	private static final Logger logger = LoggerFactory.getLogger(CosIdHelper.class);

	private final IdGeneratorProvider generatorProvider;

	private final IdGenerator sharedIdGenerator;

	public CosIdHelper(IdGeneratorProvider generatorProvider) {
		this.generatorProvider = generatorProvider;
		this.sharedIdGenerator = generatorProvider.getShare();
	}

	/**
	 * 获取下一个ID
	 * @return
	 */
	public long nextId() {
		return sharedIdGenerator.generate();
	}

	/**
	 * 获取下一个ID，返回String
	 * @return
	 */
	public String nextIdStr() {
		return sharedIdGenerator.generateAsString();
	}

	/**
	 * 获取下一个ID
	 * @param name
	 * @return
	 */
	public Long nextId(String name) {
		AtomicLong ref = new AtomicLong();
		// 这里不根据 name 进行缓存，因为底层已经用 concurrentMap 进行缓存
		generatorProvider.get(name).ifPresentOrElse(idGenerator -> {
			ref.set(idGenerator.generate());
		}, throwException(name));
		return ref.get();
	}

	/**
	 * 获取下一个ID，返回String
	 * @return
	 */
	public String nextIdStr(String name) {
		AtomicReference<String> ref = new AtomicReference<>();
		// 这里不根据 name 进行缓存，因为底层已经用 concurrentMap 进行缓存
		generatorProvider.get(name).ifPresentOrElse(idGenerator -> {
			ref.set(idGenerator.generateAsString());
		}, throwException(name));
		return ref.get();
	}

	/**
	 * 通用异常处理
	 * @param name
	 * @return
	 */
	private static Runnable throwException(String name) {
		return () -> {
			throw new RuntimeException(MessageFormat.format("【{0}】号段分发器不存在", name));
		};
	}

}
