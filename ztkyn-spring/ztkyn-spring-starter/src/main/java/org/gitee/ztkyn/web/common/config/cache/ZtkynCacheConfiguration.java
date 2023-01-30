package org.gitee.ztkyn.web.common.config.cache;

import java.util.List;
import java.util.function.Function;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author whty
 * @version 1.0
 * @description
 * @date 2023/1/30 9:59
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "ztkyn.cache")
public class ZtkynCacheConfiguration {

	private static final Logger logger = LoggerFactory.getLogger(ZtkynCacheConfiguration.class);

	private List<CaffeineCacheConfig> caffeineList;

	/**
	 * 需要在代码手动添加，因为涉及到缓存自动加载
	 */
	private List<CaffeineLoadCacheConfig> caffeineLoadCacheList;

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class CaffeineCacheConfig {

		/**
		 * 缓存名称
		 */
		private String name;

		/**
		 * 默认最大容量，大于0生效
		 */
		private int maxSize = 100;

		/**
		 * 缓存过期时间（秒），大于0生效
		 */
		private int expireDuration = -1;

	}

	/**
	 * LoadingCache是一种自动加载的缓存。其和普通缓存不同的地方在于，当缓存不存在/缓存已过期时，
	 * 若调用get()方法，则会自动调用CacheLoader.load()方法加载最新值。调用getAll()方法将遍历所有的key调用get()，
	 * 除非实现了CacheLoader.loadAll()方法。使用LoadingCache时，需要指定CacheLoader，并实现其中的load()方法供缓存缺失时自动加载。
	 *
	 * 在多线程情况下，当两个线程同时调用get()，则后一线程将被阻塞，直至前一线程更新缓存完成。
	 */
	@EqualsAndHashCode(callSuper = true)
	@Data
	public static abstract class CaffeineLoadCacheConfig extends CaffeineCacheConfig {

		/**
		 * 缓存刷新时间（秒），大于0生效，且表示这是一个LoadingCache，否则表示是一个普通Cache
		 */
		private int refreshDuration = -1;

		/**
		 * 获取特定缓存值
		 * @param key key
		 * @return 缓存值
		 */
		public abstract Object getValue(Object key);

	}

}
