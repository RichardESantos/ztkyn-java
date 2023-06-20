package org.gitee.ztkyn.boot.framework.config;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import org.gitee.ztkyn.boot.framework.domain.ResponseResult;
import org.gitee.ztkyn.core.json.JacksonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * @author whty
 * @version 1.0
 * @description controller 统一返回结构
 * @date 2023/1/29 14:07
 */
@ControllerAdvice(annotations = Controller.class)
public class ResponseAdvice implements ResponseBodyAdvice<Object> {

	private static final Logger logger = LoggerFactory.getLogger(ResponseAdvice.class);

	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		// 如果不需要进行封装的，可以添加一些校验手段，比如添加标记排除的注解
		return returnType.hasMethodAnnotation(ResponseBody.class);
	}

	@Override
	public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
			Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
			ServerHttpResponse response) {
		response.getHeaders().add("content-type", APPLICATION_JSON_VALUE);
		// 提供一定的灵活度，如果body已经被包装了，就不进行包装
		if (body instanceof ResponseResult) {
			return body;
		}
		else if (body instanceof CharSequence) {
			// Controller直接返回String的话，SpringBoot是直接返回，故我们需要手动转换成json
			return JacksonUtil.obj2String(ResponseResult.success(body));
		}
		return ResponseResult.success(body);
	}

}