package org.gitee.ztkyn.boot.framework.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author richard
 * @date 2023-06-21 10:54
 * @description ZtkynConfiguration
 * @version 1.0.0
 */
@Getter
@Setter
@NoArgsConstructor
@ConfigurationProperties(prefix = "ztkyn.props")
public class ZtkynProperties {

	private static final Logger logger = LoggerFactory.getLogger(ZtkynProperties.class);

	private RedisConf redis;

	/**
	 * 知识点：配置类的内部类必须为静态类，否则会存在 nullException Redis 相关配置
	 */
	@Getter
	@Setter
	public static class RedisConf {

		// 是否启动，默认不启用
		private boolean enable = false;

	}

}
