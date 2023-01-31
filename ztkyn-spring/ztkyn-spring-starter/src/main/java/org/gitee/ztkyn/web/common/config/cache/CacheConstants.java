package org.gitee.ztkyn.web.common.config.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author whty
 * @version 1.0
 * @description 缓存常量 CacheConstants
 * @date 2023/1/30 8:57
 */
public class CacheConstants {

	private static final Logger logger = LoggerFactory.getLogger(CacheConstants.class);

	/**
	 * 默认过期时间（单位为S）
	 */
	public static final int DEFAULT_EXPIRES = 3 * 60;

	public static final int EXPIRES_5_MIN = 5 * 60;

	public static final int EXPIRES_10_MIN = 10 * 60;

	/**
	 * 缓存默认大小
	 */
	public static final int DEFAULT_CACHE_SIZE = 1000;

	/**
	 * 默认缓存名称
	 */
	public static final String DEFAULT_CACHE = "DEFAULT:CACHE";
}
