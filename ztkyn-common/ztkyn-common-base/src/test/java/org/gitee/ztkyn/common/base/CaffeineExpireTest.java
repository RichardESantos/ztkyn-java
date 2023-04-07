package org.gitee.ztkyn.common.base;

import java.util.concurrent.TimeUnit;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Scheduler;
import com.github.benmanes.caffeine.cache.Weigher;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 驱逐策略在创建缓存的时候进行指定。常用的有基于容量的驱逐和基于时间的驱逐。
 *
 * 基于容量的驱逐需要指定缓存容量的最大值，当缓存容量达到最大时，Caffeine将使用LRU策略对缓存进行淘汰；基于时间的驱逐策略如字面意思，可以设置在最后访问/写入一个缓存经过指定时间后，自动进行淘汰。
 *
 * 驱逐策略可以组合使用，任意驱逐策略生效后，该缓存条目即被驱逐。
 *
 * LRU 最近最少使用，淘汰最长时间没有被使用的页面。 LFU 最不经常使用，淘汰一段时间内使用次数最少的页面 FIFO 先进先出 Caffeine有4种缓存淘汰设置
 *
 * 大小 （LFU算法进行淘汰） 权重 （大小与权重 只能二选一） 时间 引用 （不常用，本文不介绍）
 *
 * @author whty
 * @version 1.0
 */
public class CaffeineExpireTest {

	private static final Logger logger = LoggerFactory.getLogger(CaffeineExpireTest.class);

	/**
	 * 缓存大小淘汰
	 */
	@Test
	public void maximumSizeTest() throws InterruptedException {
		Cache<Integer, Integer> cache = Caffeine.newBuilder()
			// 超过10个后会使用W-TinyLFU算法进行淘汰
			.maximumSize(10)
			.evictionListener((key, val, removalCause) -> {
				logger.info("淘汰缓存：key:{} val:{}", key, val);
			})
			.build();

		for (int i = 1; i < 20; i++) {
			cache.put(i, i);
		}
		Thread.sleep(500);// 缓存淘汰是异步的

		// 打印还没被淘汰的缓存
		System.out.println(cache.asMap());
	}

	/**
	 * 权重淘汰
	 */
	@Test
	public void maximumWeightTest() throws InterruptedException {
		Cache<Integer, Integer> cache = Caffeine.newBuilder()
			// 限制总权重，若所有缓存的权重加起来>总权重就会淘汰权重小的缓存
			.maximumWeight(100)
			.weigher((Weigher<Integer, Integer>) (key, value) -> key)
			.evictionListener((key, val, removalCause) -> {
				logger.info("淘汰缓存：key:{} val:{}", key, val);
			})
			.build();

		// 总权重其实是=所有缓存的权重加起来
		int maximumWeight = 0;
		for (int i = 1; i < 20; i++) {
			cache.put(i, i);
			maximumWeight += i;
		}
		System.out.println("总权重=" + maximumWeight);
		Thread.sleep(500);// 缓存淘汰是异步的

		// 打印还没被淘汰的缓存
		System.out.println(cache.asMap());
	}

	/**
	 * 访问后到期（每次访问都会重置时间，也就是说如果一直被访问就不会被淘汰）
	 */
	@Test
	public void expireAfterAccessTest() throws InterruptedException {
		Cache<Integer, Integer> cache = Caffeine.newBuilder()
			.expireAfterAccess(1, TimeUnit.SECONDS)
			// 可以指定调度程序来及时删除过期缓存项，而不是等待Caffeine触发定期维护
			// 若不设置scheduler，则缓存会在下一次调用get的时候才会被动删除
			.scheduler(Scheduler.systemScheduler())
			.evictionListener((key, val, removalCause) -> {
				logger.info("淘汰缓存：key:{} val:{}", key, val);

			})
			.build();
		cache.put(1, 2);
		System.out.println(cache.getIfPresent(1));
		Thread.sleep(3000);
		System.out.println(cache.getIfPresent(1));// null
	}

	/**
	 * 写入后到期
	 */
	@Test
	public void expireAfterWriteTest() throws InterruptedException {
		Cache<Integer, Integer> cache = Caffeine.newBuilder()
			.expireAfterWrite(1, TimeUnit.SECONDS)
			// 可以指定调度程序来及时删除过期缓存项，而不是等待Caffeine触发定期维护
			// 若不设置scheduler，则缓存会在下一次调用get的时候才会被动删除
			.scheduler(Scheduler.systemScheduler())
			.evictionListener((key, val, removalCause) -> {
				logger.info("淘汰缓存：key:{} val:{}", key, val);
			})
			.build();
		cache.put(1, 2);
		Thread.sleep(3000);
		System.out.println(cache.getIfPresent(1));// null
	}

}
