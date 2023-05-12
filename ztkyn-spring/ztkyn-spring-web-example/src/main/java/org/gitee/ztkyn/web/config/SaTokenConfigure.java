package org.gitee.ztkyn.web.config;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import cn.dev33.satoken.router.SaHttpMethod;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author whty
 * @version 1.0.0
 * @date 2023-04-07 10:39
 * @description SaTokenConfigure
 */
@Component
public class SaTokenConfigure implements WebMvcConfigurer {

	private static final Logger logger = LoggerFactory.getLogger(SaTokenConfigure.class);

	// 注册拦截器
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// 注册 Sa-Token 拦截器，校验规则为 StpUtil.checkLogin() 登录校验。
		registry.addInterceptor(new ZtkynTokenUserInterceptor(handle -> {
			// 根据请求类型匹配
			SaRouter.notMatch(SaHttpMethod.OPTIONS).check(r -> StpUtil.checkLogin());
			// 排除登录接口
			SaRouter.notMatch("/api/login").check(r -> StpUtil.checkLogin());
		})).addPathPatterns("/api/**");
	}

}
