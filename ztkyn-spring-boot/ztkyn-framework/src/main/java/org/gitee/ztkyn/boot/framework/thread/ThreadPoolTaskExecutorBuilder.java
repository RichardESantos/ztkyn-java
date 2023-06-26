package org.gitee.ztkyn.boot.framework.thread;

import java.util.Objects;
import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author richard
 * @date 2023-06-26 16:08
 * @description ThreadPoolTaskExecutorBuilder
 * @version 1.0.0
 */
public class ThreadPoolTaskExecutorBuilder {

	private static final Logger logger = LoggerFactory.getLogger(ThreadPoolTaskExecutorBuilder.class);

	public static ThreadPoolTaskExecutor buildTaskExecutor(
			ThreadPoolConfiguration.ThreadPoolProperties configurationGlobal, String threadPoolNamePrefix) {
		// Java虚拟机可用的处理器数
		int processors = Runtime.getRuntime().availableProcessors();
		// 定义线程池
		ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
		// 核心线程数
		taskExecutor.setCorePoolSize(Objects.nonNull(configurationGlobal.getCorePoolSize())
				? configurationGlobal.getCorePoolSize() : processors);
		// 线程池最大线程数,默认：40000
		taskExecutor.setMaxPoolSize(
				Objects.nonNull(configurationGlobal.getMaxPoolSize()) ? configurationGlobal.getMaxPoolSize() : 40000);
		// 线程队列最大线程数,默认：80000
		taskExecutor.setQueueCapacity(
				Objects.nonNull(configurationGlobal.getMaxPoolSize()) ? configurationGlobal.getMaxPoolSize() : 80000);
		// 线程名称前缀
		taskExecutor.setThreadNamePrefix(StringUtils.isNotEmpty(configurationGlobal.getThreadNamePrefix())
				? configurationGlobal.getThreadNamePrefix() : threadPoolNamePrefix);
		// 线程池中线程最大空闲时间，默认：60，单位：秒
		taskExecutor.setKeepAliveSeconds(configurationGlobal.getKeepAliveSeconds());
		// 核心线程是否允许超时，默认:false
		taskExecutor.setAllowCoreThreadTimeOut(configurationGlobal.isAllowCoreThreadTimeOut());
		// IOC容器关闭时是否阻塞等待剩余的任务执行完成，默认:false（必须设置setAwaitTerminationSeconds）
		taskExecutor.setWaitForTasksToCompleteOnShutdown(configurationGlobal.isWaitForTasksToCompleteOnShutdown());
		// 阻塞IOC容器关闭的时间，默认：10秒（必须设置setWaitForTasksToCompleteOnShutdown）
		taskExecutor.setAwaitTerminationSeconds(configurationGlobal.getAwaitTerminationSeconds());
		/**
		 * 拒绝策略，默认是AbortPolicy AbortPolicy：丢弃任务并抛出RejectedExecutionException异常
		 * DiscardPolicy：丢弃任务但不抛出异常 DiscardOldestPolicy：丢弃最旧的处理程序，然后重试，如果执行器关闭，这时丢弃任务
		 * CallerRunsPolicy：执行器执行任务失败，则在策略回调方法中执行任务，如果执行器关闭，这时丢弃任务
		 */
		taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
		return taskExecutor;
	}

}
