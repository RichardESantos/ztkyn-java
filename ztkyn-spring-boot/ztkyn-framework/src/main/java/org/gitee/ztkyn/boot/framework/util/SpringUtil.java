package org.gitee.ztkyn.boot.framework.util;

import lombok.Getter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
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

	@Getter
	private static ApplicationContext applicationContext; // Spring应用上下文环境

	private static DefaultListableBeanFactory beanFactory; // Spring 默认容器

	/**
	 * 构造器方式自动注入
	 * @param beanFactory
	 */
	public SpringUtil(DefaultListableBeanFactory beanFactory) {
		SpringUtil.beanFactory = beanFactory;
	}

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		SpringUtil.applicationContext = applicationContext;
	}

	@SuppressWarnings("unchecked")
	public static <T> T getBean(String name) throws BeansException {
		return (T) applicationContext.getBean(name);
	}

	@SuppressWarnings("unchecked")
	public static <T> T getBean(Class<T> clz) throws BeansException {
		return applicationContext.getBean(clz);
	}

	public static <T> T getBean(String name, Class<T> clz) throws BeansException {
		return applicationContext.getBean(name, clz);
	}

	/**
	 * 手动注册bean
	 * @param name
	 * @param t
	 * @param <T>
	 */
	public static <T> void registerBean(String name, final T t) {
		beanFactory.registerSingleton(name, t);
	}

	/**
	 * 手动注册bean
	 * @param t
	 * @param <T>
	 */
	public static <T> void registerBean(final T t) {
		beanFactory.registerSingleton(t.getClass().getCanonicalName(), t);
	}

	/**
	 * 手动注册bean
	 * @param name
	 * @param clz
	 * @param <T>
	 */
	public static <T> void registerBean(String name, Class<T> clz) {
		beanFactory.registerBeanDefinition(name, BeanDefinitionBuilder.genericBeanDefinition(clz).getBeanDefinition());
	}

	/**
	 * 手动注册bean
	 * @param clz
	 * @param <T>
	 */
	public static <T> void registerBean(Class<T> clz) {
		beanFactory.registerBeanDefinition(clz.getCanonicalName(),
				BeanDefinitionBuilder.genericBeanDefinition(clz).getBeanDefinition());
	}

}
