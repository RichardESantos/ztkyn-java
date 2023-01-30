package org.gitee.ztkyn.web.common.config;

import java.util.HashMap;
import java.util.Map;

import cn.hutool.core.util.RandomUtil;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.customizers.GlobalOpenApiCustomizer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
		return new OpenAPI().info(new Info().title("XXX用户系统API").version("1.0")

				.description("Knife4j集成springdoc-openapi示例").termsOfService("http://doc.xiaominfo.com")
				.license(new License().name("Apache 2.0").url("http://doc.xiaominfo.com")));
	}

}