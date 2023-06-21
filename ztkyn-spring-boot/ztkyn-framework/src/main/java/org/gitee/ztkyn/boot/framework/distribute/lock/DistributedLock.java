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

	boolean releaseLock(String key);

}
