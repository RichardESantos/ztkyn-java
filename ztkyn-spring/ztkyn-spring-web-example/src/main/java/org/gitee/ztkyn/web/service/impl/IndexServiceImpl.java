package org.gitee.ztkyn.web.service.impl;

import cn.hutool.core.lang.id.NanoId;
import org.gitee.ztkyn.web.service.IndexService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import static org.gitee.ztkyn.web.common.config.cache.CacheConstants.DEFAULT_CACHE;

/**
 * @author whty
 * @version 1.0
 */
@Service
public class IndexServiceImpl implements IndexService {
	private static final Logger logger = LoggerFactory.getLogger(IndexServiceImpl.class);

	@Cacheable(DEFAULT_CACHE)
	@Override
	public String testCachePut(Integer requestId) {
		return NanoId.randomNanoId();
	}

	@CacheEvict(DEFAULT_CACHE)
	@Override
	public void testCacheEvict(Integer requestId) {

	}
}
