package org.gitee.ztkyn.boot.framework.distribute.lock;

/**
 * @author richard
 * @date 2023-06-21 14:42
 * @description DistributedLock
 * @version 1.0.0
 */
public interface DistributedLock {

	boolean lock(String key);

	boolean lock(String key, int retryTimes);

	boolean lock(String key, int retryTimes, long sleepMillis);

	boolean lock(String key, long expire);

	boolean lock(String key, long expire, int retryTimes);

	boolean lock(String key, long expire, int retryTimes, long sleepMillis);

	/**
	 * 锁是否被任意一个线程锁持有
	 * @param lockKey
	 * @return true-被锁 false-未被锁
	 */
	boolean isLocked(String lockKey);

	// lock.isHeldByCurrentThread()的作用是查询当前线程是否保持此锁定
	boolean isHeldByCurrentThread(String lockKey);

	boolean releaseLock(String key);

}
