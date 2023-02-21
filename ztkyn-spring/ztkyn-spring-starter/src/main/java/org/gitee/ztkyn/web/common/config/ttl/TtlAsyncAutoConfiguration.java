package org.gitee.ztkyn.web.common.config.ttl;

import java.util.concurrent.Executor;

import com.alibaba.ttl.threadpool.TtlExecutors;
import jakarta.annotation.Resource;

import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * 让异步调用的线程池被ttl代理
 *
 * @author w.dehai
 */
public class TtlAsyncAutoConfiguration implements AsyncConfigurer {

	@Lazy
	@Resource
	private ThreadPoolTaskExecutor threadPoolTaskExecutor;

	@Override
	public Executor getAsyncExecutor() {
		return TtlExecutors.getTtlExecutor(threadPoolTaskExecutor);
	}

}