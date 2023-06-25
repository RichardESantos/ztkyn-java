package org.gitee.ztkyn.boot.framework.distribute.lock;

import java.lang.reflect.Method;
import java.util.Arrays;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import jakarta.annotation.Resource;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author richard
 * @date 2023-06-21 16:28
 * @description DistributedLockAspect
 * @version 1.0.0
 */
@Aspect
@Configuration
@ConditionalOnBean(DistributedLock.class)
public class DistributedLockAspect {

	private static final Logger logger = LoggerFactory.getLogger(DistributedLockAspect.class);

	@Resource
	private DistributedLock distributedLock;

	@Pointcut("@annotation(org.gitee.ztkyn.boot.framework.distribute.lock.RedisLock)")
	private void lockPoint() {

	}

	@Around("lockPoint()")
	public Object around(ProceedingJoinPoint pjp) throws Throwable {
		// 当前线程名
		String threadName = Thread.currentThread().getName();
		logger.info("线程{}------进入分布式锁aop------", threadName);
		Method method = ((MethodSignature) pjp.getSignature()).getMethod();
		RedisLock redisLock = method.getAnnotation(RedisLock.class);
		String key = redisLock.value();
		if (!StringUtils.hasLength(key)) {
			Object[] args = pjp.getArgs();
			key = Arrays.toString(args);
		}
		logger.info("线程{} 锁的key={}", threadName, key);
		int retryTimes = redisLock.action().equals(RedisLock.LockFailAction.CONTINUE) ? redisLock.retryTimes() : 0;
		boolean lock = distributedLock.lock(key, redisLock.keepMills(), retryTimes, redisLock.sleepMills());
		if (!lock) {
			logger.info("线程{} lockRedisKey设置为空，不加锁", threadName);
			return pjp.proceed();
		}
		// 得到锁,执行方法，释放锁
		logger.info("线程{} 获取锁成功", threadName);
		try {
			return pjp.proceed();
		}
		catch (Exception e) {
			logger.error("execute locked method occured an exception", e);
		}
		finally {
			logger.info("key={}对应的锁被持有,线程{}", key, threadName);
			boolean releaseResult = distributedLock.releaseLock(key);
			logger.info("release lock : " + key + (releaseResult ? " success" : " failed"));
		}
		return null;
	}

}
