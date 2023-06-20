package org.gitee.ztkyn.boot.framework.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.gitee.ztkyn.core.util.RandomUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.customizers.GlobalOpenApiCustomizer;

@Configuration
public class SwaggerConfiguration {

	private static final Logger logger = LoggerFactory.getLogger(SwaggerConfiguration.class);

	/**
	 * 根据@Tag 上的排序，写入x-order
	 * @return the global open api customizer
	 */
	@Bean
	public GlobalOpenApiCustomizer orderGlobalOpenApiCustomizer() {
		return openApi -> {
			if (openApi.getTags() != null) {
				openApi.getTags().forEach(tag -> {
					Map<String, Object> map = new HashMap<>();
					map.put("x-order", RandomUtil.randomInt(0, 100));
					tag.setExtensions(map);
				});
			}
			if (openApi.getPaths() != null) {
				openApi.addExtension("x-test123", "333");
				openApi.getPaths().addExtension("x-abb", RandomUtil.randomInt(1, 100));
			}

		};
	}

	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI().info(new Info().title("Ztkyn-Spring-Boot")
			.version("1.0")
			.description("Ztkyn Spring Boot 项目示例")
			.termsOfService("https://gitee.com/Ztkyn/ztkyn-java")
			.license(new License().name("Apache 2.0").url("https://gitee.com/Ztkyn/ztkyn-java")));
	}

}
