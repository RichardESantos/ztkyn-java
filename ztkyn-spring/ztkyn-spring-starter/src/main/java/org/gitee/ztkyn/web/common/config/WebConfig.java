package org.gitee.ztkyn.web.common.config;

import org.gitee.ztkyn.web.common.config.interceptor.LogInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author whty
 * @version 1.0
 * @description
 * @date 2023/1/31 15:22
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

	private static final Logger logger = LoggerFactory.getLogger(WebConfig.class);

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new LogInterceptor());
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// 将所有F:/resources/目录下的资源,访问时都映射到/res/** 路径下
		registry.addResourceHandler("/res/**").addResourceLocations("file:F:/resources");
	}

}
