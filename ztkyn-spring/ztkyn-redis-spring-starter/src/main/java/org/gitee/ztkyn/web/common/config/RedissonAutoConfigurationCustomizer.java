package org.gitee.ztkyn.web.common.config;

import org.redisson.config.Config;

/**
 * @author whty
 * @version 1.0
 * @description
 * @date 2023/3/13 9:15
 */
@FunctionalInterface
public interface RedissonAutoConfigurationCustomizer {

	void customize(final Config configuration);

}
