package org.gitee.ztkyn.web.common.config.ttl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

import com.alibaba.ttl.threadpool.TtlExecutors;
import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * ExecutorService已经被ttl处理过了，可以直接注入到系统中使用
 *
 * @author w.dehi
 */
@RequiredArgsConstructor
public class TtlThreadPoolAutoConfiguration {

	private final ThreadPoolTaskExecutor threadPoolTaskExecutor;

	@Bean
	public ExecutorService ttlThreadPool() {
		ThreadPoolExecutor threadPoolExecutor = threadPoolTaskExecutor.getThreadPoolExecutor();
		return TtlExecutors.getTtlExecutorService(threadPoolExecutor);
	}

}
