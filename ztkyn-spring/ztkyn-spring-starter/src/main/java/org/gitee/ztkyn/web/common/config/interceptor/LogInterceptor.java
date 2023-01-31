package org.gitee.ztkyn.web.common.config.interceptor;

import cn.hutool.core.lang.id.NanoId;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.gitee.ztkyn.web.common.config.ComConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author whty
 * @version 1.0
 * @description
 * @date 2023/1/31 15:20
 */
public class LogInterceptor implements HandlerInterceptor {

	private static final Logger logger = LoggerFactory.getLogger(LogInterceptor.class);

	private static final char[] DEFAULT_ALPHABET = "0123456789".toCharArray();

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// 如果有上层调用就用上层的ID
		String traceId = request.getHeader(ComConstant.REQUEST_ID);
		if (traceId == null) {
			traceId = NanoId.randomNanoId(null, DEFAULT_ALPHABET, NanoId.DEFAULT_SIZE);
			response.addHeader(ComConstant.REQUEST_ID, traceId);
		}

		MDC.put(ComConstant.REQUEST_ID, traceId);
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// 调用结束后删除
		MDC.remove(ComConstant.REQUEST_ID);
	}

}
