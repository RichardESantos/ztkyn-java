package org.gitee.ztkyn.boot.framework.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.alibaba.ttl.threadpool.TtlExecutors;
import jakarta.annotation.Resource;
import org.gitee.ztkyn.core.function.DataFlexHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.gitee.ztkyn.boot.framework.thread.ThreadPoolTaskExecutorBuilder.buildTaskExecutor;

/**
 * ExecutorService已经被ttl处理过了，可以直接注入到系统中使用
 *
 * @author richard
 */
@EnableConfigurationProperties(ThreadPoolConfiguration.class)
@ConditionalOnProperty(prefix = "ztkyn.thread.thread-pool.global", name = "enable", havingValue = "true",
		matchIfMissing = true)
@Configuration
public class TtlThreadPoolAutoConfiguration {

	private static final Logger logger = LoggerFactory.getLogger(TtlThreadPoolAutoConfiguration.class);

	@Resource
	private ThreadPoolConfiguration threadPoolConfiguration;

	/**
	 * 自定义线程池
	 * @return
	 */
	@Bean
	public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
		ThreadPoolConfiguration.ThreadPoolProperties configurationGlobal = DataFlexHandler
			.notNull(threadPoolConfiguration.getGlobal())
			.convert(threadPoolProperties -> threadPoolProperties,
					threadPoolProperties -> ThreadPoolConfiguration.getEmpty());
		String threadPoolNamePrefix = "Global-ThreadPool-";

		ThreadPoolTaskExecutor taskExecutor = buildTaskExecutor(configurationGlobal, threadPoolNamePrefix);
		// 初始化
		// taskExecutor.initialize();
		logger.info("【{}】初始化成功", threadPoolNamePrefix);
		return taskExecutor;
	}

	@Bean
	public ExecutorService ttlThreadPool(ThreadPoolTaskExecutor threadPoolTaskExecutor) {
		ThreadPoolExecutor threadPoolExecutor = threadPoolTaskExecutor.getThreadPoolExecutor();
		return TtlExecutors.getTtlExecutorService(threadPoolExecutor);
	}

}
