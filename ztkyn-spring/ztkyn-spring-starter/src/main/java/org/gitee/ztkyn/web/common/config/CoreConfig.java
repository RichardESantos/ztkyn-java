package org.gitee.ztkyn.web.common.config;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import jakarta.annotation.Resource;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.gitee.ztkyn.core.function.DataProcessHandler;
import org.gitee.ztkyn.core.function.PredicateUtil;
import org.gitee.ztkyn.web.common.config.cache.CacheConstants;
import org.gitee.ztkyn.web.common.config.cache.ZtkynCacheConfiguration;
import org.gitee.ztkyn.web.common.config.cache.ZtkynCacheConfiguration.CaffeineCacheConfig;
import org.gitee.ztkyn.web.common.config.cache.ZtkynCacheConfiguration.CaffeineLoadCacheConfig;
import org.hibernate.validator.HibernateValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.gitee.ztkyn.web.common.config.cache.CacheConstants.DEFAULT_CACHE_SIZE;
import static org.gitee.ztkyn.web.common.config.cache.CacheConstants.DEFAULT_EXPIRES;

/**
 * @author whty
 * @version 1.0
 * @description
 * @date 2023/1/29 16:28
 */
@EnableCaching
@Configuration
public class CoreConfig {

	private static final Logger logger = LoggerFactory.getLogger(CoreConfig.class);

	@Resource
	private ZtkynCacheConfiguration ztkynCacheConfiguration;

	/**
	 * 确保在单机环境中Spring cache 使用 CaffeineCache，减少缓存的网络开销 针对于 需要共享的缓存，使用缓存 工具类直接操作，不委托给spring
	 * cache
	 * <p>
	 * Caffeine配置说明： initialCapacity=[integer]: 初始的缓存空间大小 maximumSize=[long]: 缓存的最大条数
	 * maximumWeight=[long]: 缓存的最大权重 expireAfterAccess=[duration]: 最后一次写入或访问后经过固定时间过期
	 * expireAfterWrite=[duration]: 最后一次写入后经过固定时间过期 refreshAfterWrite=[duration]:
	 * 创建缓存或者最近一次更新缓存后经过固定的时间间隔，刷新缓存 weakKeys: 打开key的弱引用 weakValues：打开value的弱引用
	 * softValues：打开value的软引用 recordStats：开发统计功能 注意：
	 * expireAfterWrite和expireAfterAccess同事存在时，以expireAfterWrite为准。
	 * maximumSize和maximumWeight不可以同时使用 weakValues和softValues不可以同时使用
	 * <p>
	 * CacheManager 定义了创建、配置、获取、管理和控制多个唯一命名的 Cache。这些 Cache 存在于 CacheManager 的上下文中。
	 * SimpleCacheManager只能使用Cache和LoadingCache，异步缓存将无法支持。
	 */
	@Bean
	public CacheManager cacheManager() {
		SimpleCacheManager cacheManager = new SimpleCacheManager();
		List<CaffeineCache> list = new ArrayList<>();
		// 添加默认缓存
		list.add(new CaffeineCache(CacheConstants.DEFAULT_CACHE, generateCache(
				new CaffeineCacheConfig(CacheConstants.DEFAULT_CACHE, DEFAULT_CACHE_SIZE, DEFAULT_EXPIRES))));

		// 添加通过配置文件添加的缓存
		DataProcessHandler.of(ztkynCacheConfiguration.getCaffeineList(), PredicateUtil.listNotBlank())
				.ifTrue(caffeineCacheConfigs -> {
					caffeineCacheConfigs.forEach(caffeineCacheConfig -> list
							.add(new CaffeineCache(caffeineCacheConfig.getName(), generateCache(caffeineCacheConfig))));
				});

		// 添加通过编程手动导入的缓存
		// 考虑后期手动扩展，不需要修改到底层包
		// 参考 org.gitee.ztkyn.web.config.CaffeineLoadingCachePostProcessor 中自定义的缓存
		DataProcessHandler.of(ztkynCacheConfiguration.getCaffeineLoadCacheList(), PredicateUtil.listNotBlank())
				.ifTrue(caffeineCacheConfigs -> {
					caffeineCacheConfigs.forEach(caffeineCacheConfig -> list
							.add(new CaffeineCache(caffeineCacheConfig.getName(), generateCache(caffeineCacheConfig))));
				});

		cacheManager.setCaches(list);
		return cacheManager;
	}

	/**
	 * 生成cache
	 * <p>
	 * 在实际开发中如何配置淘汰策略最优呢，根据我的经验常用的还是以大小淘汰为主
	 * <p>
	 * 实践1
	 * <p>
	 * 配置：设置 maxSize、refreshAfterWrite，不设置 expireAfterWrite/expireAfterAccess
	 * <p>
	 * 优缺点：因为设置expireAfterWrite当缓存过期时会同步加锁获取缓存，所以设置expireAfterWrite时性能较好，但是某些时候会取旧数据,适合允许取到旧数据的场景
	 * <p>
	 * 实践2 配置：设置 maxSize、expireAfterWrite/expireAfterAccess，不设置 refreshAfterWrite
	 * <p>
	 * 优缺点：与上面相反，数据一致性好，不会获取到旧数据，但是性能没那么好（对比起来），适合获取数据时不耗时的场景
	 * @param config 配置信息
	 * @return cache
	 */
	private Cache<Object, Object> generateCache(CaffeineCacheConfig config) {
		// 创建缓存
		Caffeine<Object, Object> builder = Caffeine.newBuilder();
		if (config.getMaxSize() > 0) {
			builder.maximumSize(config.getMaxSize());
		}
		if (config.getExpireDuration() > 0) {
			builder.expireAfterWrite(config.getExpireDuration(), TimeUnit.SECONDS);
		}
		Cache<Object, Object> cache = builder.build();
		logger.info("通过配置文件方式加载缓存【{}】成功", config.getName());
		return cache;
	}

	private Cache<Object, Object> generateCache(CaffeineLoadCacheConfig config) {
		// 创建缓存
		Cache<Object, Object> cache;
		Caffeine<Object, Object> builder = Caffeine.newBuilder();
		if (config.getMaxSize() > 0) {
			builder.maximumSize(config.getMaxSize());
		}
		if (config.getExpireDuration() > 0) {
			builder.expireAfterWrite(config.getExpireDuration(), TimeUnit.SECONDS);
		}
		if (config.getRefreshDuration() > 0) {
			// 创建缓存或者最近一次更新缓存后经过指定时间间隔，刷新缓存；refreshAfterWrite仅支持LoadingCache
			builder.refreshAfterWrite(config.getRefreshDuration(), TimeUnit.SECONDS);
			// 创建LoadingCache，需要传入CacheLoader
			cache = builder.build(new CacheLoader<>() {
				@Override
				public @Nullable Object load(Object key) throws Exception {
					return config.getValue(key);
				}
			});
		}
		else {
			// 创建普通Cache
			cache = builder.build();
		}
		logger.info("通过代码方式加载缓存【{}】成功", config.getName());
		return cache;
	}

	/**
	 * Spring Validation默认会校验完所有字段，然后才抛出异常。可以通过一些简单的配置，开启Fail Fast模式，一旦校验失败就立即返回。
	 * @return
	 */
	@Bean
	public Validator validator() {
		ValidatorFactory validatorFactory = Validation.byProvider(HibernateValidator.class).configure().failFast(true)
				.buildValidatorFactory();
		return validatorFactory.getValidator();
	}

}
