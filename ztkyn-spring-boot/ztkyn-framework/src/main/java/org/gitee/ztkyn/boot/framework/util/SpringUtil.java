package org.gitee.ztkyn.boot.framework.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author whty
 * @version 1.0
 * @description
 * @date 2023/3/13 11:11
 */
@Component
public class SpringUtil implements ApplicationContextAware {

	private static final Logger logger = LoggerFactory.getLogger(SpringUtil.class);

	private static ApplicationContext applicationContext; // Spring应用上下文环境

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		SpringUtil.applicationContext = applicationContext;
	}

	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	@SuppressWarnings("unchecked")
	public static <T> T getBean(String name) throws BeansException {
		return (T) applicationContext.getBean(name);
	}

	@SuppressWarnings("unchecked")
	public static <T> T getBean(Class<T> clz) throws BeansException {
		return (T) applicationContext.getBean(clz);
	}

	public static <T> T getBean(String name, Class<T> clz) throws BeansException {
		return (T) applicationContext.getBean(name, clz);
	}

}
