package org.gitee.ztkyn.boot.framework.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author richard
 * @date 2023-06-21 13:29
 * @description RedissonProperties
 * @version 1.0.0
 */
@ConfigurationProperties(prefix = "spring.redis.redisson")
public class RedissonProperties {

	private String config;

	private String file;

	public String getConfig() {
		return config;
	}

	public void setConfig(String config) {
		this.config = config;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

}
