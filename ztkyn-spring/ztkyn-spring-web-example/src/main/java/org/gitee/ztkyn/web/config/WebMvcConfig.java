package org.gitee.ztkyn.web.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author whty
 * @version 1.0.0
 * @date 2023-05-12 13:40
 * @description WebMvcConfig
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {

	private static final Logger logger = LoggerFactory.getLogger(WebMvcConfig.class);

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// 将所有F:/resources/目录下的资源,访问时都映射到/res/** 路径下
		// registry.addResourceHandler("/res/**").addResourceLocations("file:/home/res/");
	}

}
