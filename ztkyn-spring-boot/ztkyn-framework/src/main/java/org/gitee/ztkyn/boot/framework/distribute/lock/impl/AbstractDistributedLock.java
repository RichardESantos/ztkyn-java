package org.gitee.ztkyn.boot.framework.distribute.lock.impl;

import org.gitee.ztkyn.boot.framework.distribute.lock.DistributedLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author richard
 * @date 2023-06-21 14:42
 * @description AbstractDistributedLock.java 抽象类，实现基本的方法，关键方法由子类去实现
 * @version 1.0.0
 */
public abstract class AbstractDistributedLock implements DistributedLock {

	private static final Logger logger = LoggerFactory.getLogger(AbstractDistributedLock.class);

	private static final long TIMEOUT_MILLIS = 30000;

	private static final int RETRY_TIMES = Integer.MAX_VALUE;

	private static final long SLEEP_MILLIS = 500;

	@Override
	public boolean lock(String key) {
		return lock(key, TIMEOUT_MILLIS, RETRY_TIMES, SLEEP_MILLIS);
	}

	@Override
	public boolean lock(String key, int retryTimes) {
		return lock(key, TIMEOUT_MILLIS, retryTimes, SLEEP_MILLIS);
	}

	@Override
	public boolean lock(String key, int retryTimes, long sleepMillis) {
		return lock(key, TIMEOUT_MILLIS, retryTimes, sleepMillis);
	}

	@Override
	public boolean lock(String key, long expire) {
		return lock(key, expire, RETRY_TIMES, SLEEP_MILLIS);
	}

	@Override
	public boolean lock(String key, long expire, int retryTimes) {
		return lock(key, expire, retryTimes, SLEEP_MILLIS);
	}

}
