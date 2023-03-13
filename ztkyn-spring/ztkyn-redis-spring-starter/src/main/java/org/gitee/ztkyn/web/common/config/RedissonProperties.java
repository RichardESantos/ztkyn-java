package org.gitee.ztkyn.web.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author whty
 * @version 1.0
 * @description
 * @date 2023/3/13 9:16
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
