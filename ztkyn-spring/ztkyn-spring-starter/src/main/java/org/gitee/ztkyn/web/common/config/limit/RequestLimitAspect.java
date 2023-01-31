package org.gitee.ztkyn.web.common.config.limit;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Scheduler;
import com.google.common.util.concurrent.RateLimiter;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.gitee.ztkyn.common.base.ZtkynStringUtil;
import org.gitee.ztkyn.web.common.exception.RequestLimitException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Component;

import static org.gitee.ztkyn.common.base.ZtkynStringUtil.colonSeparator;

/**
 * @author whty
 * @version 1.0
 */
@Aspect
@Component
public class RequestLimitAspect {

	private static final Logger logger = LoggerFactory.getLogger(RequestLimitAspect.class);

	/**
	 * 不同的接口，不同的流量控制
	 */
	private final Cache<String, RateLimiter> limitMapCache = Caffeine.newBuilder()
			.expireAfterAccess(5, TimeUnit.SECONDS)
			// 可以指定调度程序来及时删除过期缓存项，而不是等待Caffeine触发定期维护
			// 若不设置scheduler，则缓存会在下一次调用get的时候才会被动删除
			.scheduler(Scheduler.systemScheduler()).evictionListener((key, val, removalCause) -> {
				logger.debug("淘汰缓存：key:{} val:{}", key, val);

			}).build();

	@Around("@annotation(RequestLimit)")
	public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();
		// 拿limit的注解
		RequestLimit limit = method.getAnnotation(RequestLimit.class);
		if (limit != null) {
			// key作用：不同的接口，不同的流量控制
			String key = limit.key();
			if (ZtkynStringUtil.isBlank(key)) {
				// key 默认为 类的全路径+方法名
				key = signature.getDeclaringTypeName() + colonSeparator + method.getName();
			}
			// 验证缓存是否有命中key
			RateLimiter rateLimiter = limitMapCache.getIfPresent(key);
			if (Objects.isNull(rateLimiter)) {
				// 创建令牌桶
				rateLimiter = RateLimiter.create(limit.permitsPerSecond());
				limitMapCache.put(key, rateLimiter);
				logger.debug("新建了令牌桶={}，容量={}", key, limit.permitsPerSecond());
			}
			// 拿令牌
			boolean acquire = rateLimiter.tryAcquire(limit.timeout(), limit.timeunit());
			// 拿不到命令，直接返回异常提示
			if (!acquire) {
				logger.debug("令牌桶={}，获取令牌失败", key);
				throw new RequestLimitException(limit.msg());
			}
		}
		return joinPoint.proceed();
	}

}
