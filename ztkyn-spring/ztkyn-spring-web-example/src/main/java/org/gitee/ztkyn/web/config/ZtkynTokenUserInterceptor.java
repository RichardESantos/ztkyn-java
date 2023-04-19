package org.gitee.ztkyn.web.config;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import cn.dev33.satoken.fun.SaParamFunction;
import cn.dev33.satoken.interceptor.SaInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author whty
 * @version 1.0.0
 * @date 2023-04-07 14:16
 * @description DWUserInterceptor
 */
public class ZtkynTokenUserInterceptor extends SaInterceptor implements HandlerInterceptor {

	private static final Logger logger = LoggerFactory.getLogger(ZtkynTokenUserInterceptor.class);

	/**
	 * 创建一个 Sa-Token 综合拦截器，默认带有注解鉴权能力
	 * @param auth 认证函数，每次请求执行
	 */
	public ZtkynTokenUserInterceptor(SaParamFunction<Object> auth) {
		super(auth);
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		return super.preHandle(request, response, handler);
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		super.postHandle(request, response, handler, modelAndView);
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		super.afterCompletion(request, response, handler, ex);
	}

}
