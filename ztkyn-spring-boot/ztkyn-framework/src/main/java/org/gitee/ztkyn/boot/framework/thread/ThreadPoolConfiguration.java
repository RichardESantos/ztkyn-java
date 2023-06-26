package org.gitee.ztkyn.boot.framework.thread;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author richard
 * @date 2023-06-26 15:53
 * @description ThreadPoolConfiguration
 * @version 1.0.0
 */
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@ConfigurationProperties(prefix = "ztkyn.thread.thread-pool")
public class ThreadPoolConfiguration {

	private static final Logger logger = LoggerFactory.getLogger(ThreadPoolConfiguration.class);

	/**
	 * 默认空配置
	 */
	@Getter
	private static final ThreadPoolProperties empty = new ThreadPoolProperties();

	/**
	 * 全局线程池配置
	 */
	private ThreadPoolProperties global;

	/**
	 * @Aysnc 任务使用的线程池配置
	 */
	private ThreadPoolProperties async;

	@Getter
	@Setter
	@NoArgsConstructor
	@Accessors(chain = true)
	public static class ThreadPoolProperties {

		/**
		 * 是否启动异步线程池，默认 false
		 */
		private boolean enable;

		/**
		 * 核心线程数,默认：Java虚拟机可用线程数
		 */
		private Integer corePoolSize;

		/**
		 * 线程池最大线程数,默认：40000
		 */
		private Integer maxPoolSize;

		/**
		 * 线程队列最大线程数,默认：80000
		 */
		private Integer queueCapacity;

		/**
		 * 自定义线程名前缀，默认：Async-ThreadPool-
		 */
		private String threadNamePrefix;

		/**
		 * 线程池中线程最大空闲时间，默认：60，单位：秒
		 */
		private Integer keepAliveSeconds = 60;

		/**
		 * 核心线程是否允许超时，默认false
		 */
		private boolean allowCoreThreadTimeOut;

		/**
		 * IOC容器关闭时是否阻塞等待剩余的任务执行完成，默认:false（必须设置setAwaitTerminationSeconds）
		 */
		private boolean waitForTasksToCompleteOnShutdown;

		/**
		 * 阻塞IOC容器关闭的时间，默认：10秒（必须设置setWaitForTasksToCompleteOnShutdown）
		 */
		private int awaitTerminationSeconds = 10;

	}

}
