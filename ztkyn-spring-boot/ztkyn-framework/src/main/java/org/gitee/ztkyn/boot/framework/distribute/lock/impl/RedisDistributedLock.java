package org.gitee.ztkyn.boot.framework.distribute.lock.impl;

import java.util.concurrent.TimeUnit;

import org.gitee.ztkyn.boot.framework.distribute.lock.DistributedLock;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author richard
 * @date 2023-06-21 14:44
 * @description RedisDistributedLock.java Redis分布式锁的实现
 * @version 1.0.0
 */
public class RedisDistributedLock extends AbstractDistributedLock implements DistributedLock {

	private static final Logger logger = LoggerFactory.getLogger(RedisDistributedLock.class);

	private RedissonClient redisson;

	public RedisDistributedLock(RedissonClient redisson) {
		this.redisson = redisson;
	}

	@Override
	public boolean lock(String key, long expire, int retryTimes, long sleepMillis) {
		RLock lock = redisson.getLock(key);
		try {
			boolean result = lock.tryLock(expire, sleepMillis, TimeUnit.SECONDS);
			// 如果获取锁失败，按照传入的重试次数进行重试
			while ((!result) && retryTimes-- > 0) {
				try {
					logger.debug("lock failed, retrying..." + retryTimes);
					Thread.sleep(sleepMillis);
				}
				catch (InterruptedException e) {
					return false;
				}
			}
			return result;
		}
		catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean releaseLock(String key) {
		RLock lock = redisson.getLock(key);
		lock.unlock();
		return true;
	}

}
