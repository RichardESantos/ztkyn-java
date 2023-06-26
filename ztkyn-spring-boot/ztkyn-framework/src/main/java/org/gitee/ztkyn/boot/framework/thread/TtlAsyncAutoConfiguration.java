package org.gitee.ztkyn.boot.framework.thread;

import java.util.Objects;
import java.util.concurrent.Executor;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.alibaba.ttl.threadpool.TtlExecutors;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.gitee.ztkyn.core.string.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.gitee.ztkyn.boot.framework.thread.ThreadPoolTaskExecutorBuilder.buildTaskExecutor;

/**
 * 让异步调用的线程池被ttl代理
 *
 * @author richard
 */
@EnableAsync
@Configuration
@EnableConfigurationProperties(ThreadPoolConfiguration.class)
@ConditionalOnProperty(prefix = "ztkyn.thread.thread-pool.async", name = "enable", havingValue = "true",
		matchIfMissing = false)
public class TtlAsyncAutoConfiguration implements AsyncConfigurer {

	private static final Logger logger = LoggerFactory.getLogger(TtlAsyncAutoConfiguration.class);

	@Resource
	private ThreadPoolConfiguration threadPoolConfiguration;

	/**
	 * 定义线程池 使用{@link java.util.concurrent.LinkedBlockingQueue}(FIFO）队列，是一个用于并发环境下的阻塞队列集合类
	 * ThreadPoolTaskExecutor不是完全被IOC容器管理的bean,可以在方法上加上@Bean注解交给容器管理,这样可以将taskExecutor.initialize()方法调用去掉，容器会自动调用
	 * <p>
	 * </p>
	 * 只能供@Async使用，并没有往容器中注册ThreadPoolTaskExecutor-bean
	 * @return
	 */
	@Bean("asyncTaskExecutor")
	@Override
	public Executor getAsyncExecutor() {
		ThreadPoolConfiguration.ThreadPoolProperties asyncThreadPoolProperties = threadPoolConfiguration.getAsync();
		String threadPoolNamePrefix = "Async-ThreadPool-";
		ThreadPoolTaskExecutor taskExecutor = buildTaskExecutor(asyncThreadPoolProperties, threadPoolNamePrefix);
		// 初始化
		// taskExecutor.initialize();
		logger.info("【{}】初始化成功", threadPoolNamePrefix);
		return TtlExecutors.getTtlExecutor(taskExecutor);
	}

	/**
	 * 异步方法执行的过程中抛出的异常捕获
	 * @return
	 */
	@Override
	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
		return (throwable, method, objects) -> {
			String msg = StringUtil.emptyStr;
			if (ArrayUtils.isNotEmpty(objects) && objects.length > 0) {
				msg = StringUtils.join(msg, "参数是：");
				for (int i = 0; i < objects.length; i++) {
					msg = StringUtils.join(msg, objects[i], StringUtil.newLine);
				}
			}
			if (Objects.nonNull(throwable)) {
				msg = StringUtils.join(msg, throwable.getMessage());
			}
			logger.error("{}{}", method.getDeclaringClass(), msg);
		};
	}

}