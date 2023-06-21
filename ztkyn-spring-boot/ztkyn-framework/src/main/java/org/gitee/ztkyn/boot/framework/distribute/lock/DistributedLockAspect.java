package org.gitee.ztkyn.boot.framework.distribute.lock;

import java.lang.reflect.Method;
import java.util.Arrays;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
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
@ConditionalOnClass(DistributedLock.class)
public class DistributedLockAspect {

	private static final Logger logger = LoggerFactory.getLogger(DistributedLockAspect.class);

	@Resource
	private DistributedLock distributedLock;

	@Pointcut("@annotation(org.gitee.ztkyn.boot.framework.distribute.lock.RedisLock)")
	private void lockPoint() {

	}

	@Around("lockPoint()")
	public Object around(ProceedingJoinPoint pjp) throws Throwable {
		Method method = ((MethodSignature) pjp.getSignature()).getMethod();
		RedisLock redisLock = method.getAnnotation(RedisLock.class);
		String key = redisLock.value();
		if (!StringUtils.hasLength(key)) {
			Object[] args = pjp.getArgs();
			key = Arrays.toString(args);
		}
		int retryTimes = redisLock.action().equals(RedisLock.LockFailAction.CONTINUE) ? redisLock.retryTimes() : 0;
		boolean lock = distributedLock.lock(key, redisLock.keepMills(), retryTimes, redisLock.sleepMills());
		if (!lock) {
			logger.debug("get lock failed : " + key);
			return null;
		}

		// 得到锁,执行方法，释放锁
		logger.debug("get lock success : " + key);
		try {
			return pjp.proceed();
		}
		catch (Exception e) {
			logger.error("execute locked method occured an exception", e);
		}
		finally {
			boolean releaseResult = distributedLock.releaseLock(key);
			logger.debug("release lock : " + key + (releaseResult ? " success" : " failed"));
		}
		return null;
	}

}
