package org.gitee.ztkyn.common.base;

import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import com.github.benmanes.caffeine.cache.AsyncLoadingCache;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 总结 上述一些策略在创建时都可以进行自由组合，一般情况下有两种方法 设置 maxSize、refreshAfterWrite，不设置
 * expireAfterWrite/expireAfterAccess
 * 设置expireAfterWrite当缓存过期时会同步加锁获取缓存，所以设置expireAfterWrite时性能较好，但是某些时候会取旧数据,适合允许取到旧数据的场景 设置
 * maxSize、expireAfterWrite/expireAfterAccess，不设置 refreshAfterWrite
 * 数据一致性好，不会获取到旧数据，但是性能没那么好（对比起来），适合获取数据时不耗时的场景
 *
 * @author whty
 * @version 1.0
 */
public class CaffeineCreateTest {

	private static final Logger logger = LoggerFactory.getLogger(CaffeineCreateTest.class);

	/**
	 * 手动创建 最普通的一种缓存，无需指定加载方式，需要手动调用put()进行加载。需要注意的是put()方法对于已存在的key将进行覆盖，
	 * 这点和Map的表现是一致的。在获取缓存值时，如果想要在缓存值不存在时，原子地将值写入缓存，则可以调用get(key, k ->
	 * value)方法，该方法将避免写入竞争。调用invalidate()方法，将手动移除缓存。 在多线程情况下，当使用get(key, k ->
	 * value)时，如果有另一个线程同时调用本方法进行竞争，则后一线程会被阻塞，直到前一线程更新缓存完成；
	 * 而若另一线程调用getIfPresent()方法，则会立即返回null，不会被阻塞。
	 */
	@Test
	public void manualCreate() {
		Cache<Object, Object> cache = Caffeine.newBuilder()
			// 初始数量
			.initialCapacity(10)
			// 最大条数
			.maximumSize(10)
			// expireAfterWrite和expireAfterAccess同时存在时，以expireAfterWrite为准
			// 最后一次写操作后经过指定时间过期
			.expireAfterWrite(1, TimeUnit.SECONDS)
			// 最后一次读或写操作后经过指定时间过期
			.expireAfterAccess(1, TimeUnit.SECONDS)
			// 监听缓存被移除
			.removalListener((key, val, removalCause) -> {
			})
			// 记录命中
			.recordStats()
			.build();

		cache.put("1", "张三");
		// 张三
		System.out.println(cache.getIfPresent("1"));
		// 存储的是默认值
		System.out.println(cache.get("2", o -> "默认值"));
	}

	/**
	 * LoadingCache是一种自动加载的缓存。其和普通缓存不同的地方在于，当缓存不存在/缓存已过期时，若调用get()方法，
	 * 则会自动调用CacheLoader.load()方法加载最新值。调用getAll()方法将遍历所有的key调用get()，
	 * 除非实现了CacheLoader.loadAll()方法。使用LoadingCache时，需要指定CacheLoader，并实现其中的load()方法供缓存缺失时自动加载。
	 * 在多线程情况下，当两个线程同时调用get()，则后一线程将被阻塞，直至前一线程更新缓存完成。
	 */
	@Test
	public void loadingCacheTest() {
		LoadingCache<String, String> loadingCache = Caffeine.newBuilder()
			// 创建缓存或者最近一次更新缓存后经过指定时间间隔，刷新缓存；refreshAfterWrite仅支持LoadingCache
			.refreshAfterWrite(10, TimeUnit.SECONDS)
			.expireAfterWrite(10, TimeUnit.SECONDS)
			.expireAfterAccess(10, TimeUnit.SECONDS)
			.maximumSize(10)
			// 根据key查询数据库里面的值，这里是个lamba表达式
			.build(key -> new Date().toString());
	}

	/**
	 * AsyncCache是Cache的一个变体，其响应结果均为CompletableFuture，通过这种方式，AsyncCache对异步编程模式进行了适配。
	 * 默认情况下，缓存计算使用ForkJoinPool.commonPool()作为线程池，如果想要指定线程池，则可以覆盖并实现Caffeine.executor(Executor)方法。
	 * synchronous()提供了阻塞直到异步缓存生成完毕的能力，它将以Cache进行返回。 在多线程情况下，当两个线程同时调用get(key, k ->
	 * value)，则会返回同一个CompletableFuture对象。由于返回结果本身不进行阻塞，可以根据业务设计自行选择阻塞等待或者非阻塞。
	 */
	@Test
	public void asyncLoadingCacheTest() {
		AsyncLoadingCache<String, String> asyncLoadingCache = Caffeine.newBuilder()
			// 创建缓存或者最近一次更新缓存后经过指定时间间隔刷新缓存；仅支持LoadingCache
			.refreshAfterWrite(1, TimeUnit.SECONDS)
			.expireAfterWrite(1, TimeUnit.SECONDS)
			.expireAfterAccess(1, TimeUnit.SECONDS)
			.maximumSize(10)
			// 根据key查询数据库里面的值
			.buildAsync(key -> {
				Thread.sleep(1000);
				return new Date().toString();
			});

		// 异步缓存返回的是CompletableFuture
		CompletableFuture<String> future = asyncLoadingCache.get("1");
		future.thenAccept(System.out::println);
	}

	/**
	 * refreshAfterWrite()表示x秒后自动刷新缓存的策略可以配合淘汰策略使用，注意的是刷新机制只支持LoadingCache和AsyncLoadingCache
	 */
	private static int NUM = 0;

	@Test
	public void refreshAfterWriteTest() throws InterruptedException {
		LoadingCache<Integer, Integer> cache = Caffeine.newBuilder()
			.refreshAfterWrite(1, TimeUnit.SECONDS)
			// 模拟获取数据，每次获取就自增1
			.build(integer -> ++NUM);

		// 获取ID=1的值，由于缓存里还没有，所以会自动放入缓存
		System.out.println(cache.get(1));// 1

		// 延迟2秒后，理论上自动刷新缓存后取到的值是2
		// 但其实不是，值还是1，因为refreshAfterWrite并不是设置了n秒后重新获取就会自动刷新
		// 而是x秒后&&第二次调用getIfPresent的时候才会被动刷新
		Thread.sleep(2000);
		System.out.println(cache.getIfPresent(1));// 1

		// 此时才会刷新缓存，而第一次拿到的还是旧值
		System.out.println(cache.getIfPresent(1));// 2
	}

	/**
	 * 统计
	 */
	public void recordStatsTest() {
		LoadingCache<String, String> cache = Caffeine.newBuilder()
			// 创建缓存或者最近一次更新缓存后经过指定时间间隔，刷新缓存；refreshAfterWrite仅支持LoadingCache
			.refreshAfterWrite(1, TimeUnit.SECONDS)
			.expireAfterWrite(1, TimeUnit.SECONDS)
			.expireAfterAccess(1, TimeUnit.SECONDS)
			.maximumSize(10)
			// 开启记录缓存命中率等信息
			.recordStats()
			// 根据key查询数据库里面的值
			.build(key -> {
				Thread.sleep(1000);
				return new Date().toString();
			});

		cache.put("1", "shawn");
		cache.get("1");

		/*
		 * hitCount :命中的次数 missCount:未命中次数 requestCount:请求次数 hitRate:命中率 missRate:丢失率
		 * loadSuccessCount:成功加载新值的次数 loadExceptionCount:失败加载新值的次数 totalLoadCount:总条数
		 * loadExceptionRate:失败加载新值的比率 totalLoadTime:全部加载时间 evictionCount:丢失的条数
		 */
		System.out.println(cache.stats());
	}

}
