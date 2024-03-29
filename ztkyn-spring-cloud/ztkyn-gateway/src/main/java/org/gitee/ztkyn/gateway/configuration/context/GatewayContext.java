package org.gitee.ztkyn.gateway.configuration.context;

import lombok.Data;
import org.springframework.http.HttpHeaders;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * @author chenlei
 * @className GatewayContext
 * @description 网关上下文
 * @company CDFT
 * @date 2022-06-01 9:03
 */
@Data
public class GatewayContext {

	public static final String CACHE_GATEWAY_CONTEXT = "cacheGatewayContext";

	public static final String CACHE_GATEWAY_SM4_KEY = "cacheGatewaySM4Key";

	/**
	 * cache json body
	 */
	private String requestBody;

	/**
	 * cache Response Body
	 */
	private Object responseBody;

	/**
	 * request headers
	 */
	private HttpHeaders requestHeaders;

	/**
	 * cache form data
	 */
	private MultiValueMap<String, String> formData;

	/**
	 * cache all request data include:form data and query param
	 */
	private MultiValueMap<String, String> allRequestData = new LinkedMultiValueMap<>(0);

	private Integer contentLength = 0;

}
