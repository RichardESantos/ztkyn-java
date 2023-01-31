package org.gitee.ztkyn.web.common.config;

import java.util.Objects;

import org.gitee.ztkyn.common.base.JacksonUtil;
import org.gitee.ztkyn.web.common.domain.ResponseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * @author whty
 * @version 1.0
 * @description RestController 统一返回结构
 * @date 2023/1/18 15:07
 */
@RestControllerAdvice(annotations = RestController.class)
public class RestResponseAdvice implements ResponseBodyAdvice<Object> {

	private static final Logger logger = LoggerFactory.getLogger(RestResponseAdvice.class);

	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		// 判断所在controller是否包含指定注解，或者方法包含指定注解
		RestResponse annotation = AnnotationUtils.findAnnotation(returnType.getDeclaringClass(), RestResponse.class);
		return returnType.hasMethodAnnotation(RestResponse.class) || Objects.nonNull(annotation);
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
