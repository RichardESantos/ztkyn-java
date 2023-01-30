package org.gitee.ztkyn.web.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.gitee.ztkyn.web.common.config.cache.ZtkynCacheConfiguration;
import org.gitee.ztkyn.web.common.config.cache.ZtkynCacheConfiguration.CaffeineLoadCacheConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * @author whty
 * @version 1.0
 * @description // 在这里加载 loadingCache 缓存配置
 * @date 2023/1/30 13:23
 */
@Component
public class CaffeineLoadingCachePostProcessor implements BeanPostProcessor {

	private static final Logger logger = LoggerFactory.getLogger(CaffeineLoadingCachePostProcessor.class);

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		if (bean instanceof ZtkynCacheConfiguration configuration) {
			System.out.println();
			List<CaffeineLoadCacheConfig> caffeineLoadCacheList = configuration.getCaffeineLoadCacheList();
			if (Objects.isNull(caffeineLoadCacheList)) {
				caffeineLoadCacheList = new ArrayList<>();
			}
			caffeineLoadCacheList.add(getNewConfig());
			configuration.setCaffeineLoadCacheList(caffeineLoadCacheList);
		}
		return bean;
	}

	/**
	 * 模拟自动加载缓存的过程，根据实际需求进行重写
	 * @return
	 */
	private CaffeineLoadCacheConfig getNewConfig() {
		CaffeineLoadCacheConfig config = new CaffeineLoadCacheConfig() {
			@Override
			public Object getValue(Object key) {
				return null;
			}
		};
		config.setName("测试loadingCache加载能力");
		config.setMaxSize(100);
		return config;
	}

}
