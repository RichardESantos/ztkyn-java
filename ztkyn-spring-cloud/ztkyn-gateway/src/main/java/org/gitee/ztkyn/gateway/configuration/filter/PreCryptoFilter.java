package org.gitee.ztkyn.gateway.configuration.filter;

import org.gitee.ztkyn.gateway.configuration.context.GatewayContext;
import org.gitee.ztkyn.gateway.configuration.properties.GateWayCryptoProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * 接口解密处理器，同时对接口进行缓存
 */
public class PreCryptoFilter implements WebFilter, Ordered {

	private static final Logger logger = LoggerFactory.getLogger(PreCryptoFilter.class);

	private final GateWayCryptoProperties gateWayCryptoProperties;

	public PreCryptoFilter(GateWayCryptoProperties gateWayCryptoProperties) {
		this.gateWayCryptoProperties = gateWayCryptoProperties;
	}

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
		logger.info("进行接口加密处理");
		ServerHttpRequest request = exchange.getRequest();
		// 请求路径
		String reqPath = request.getURI().getPath();
		GatewayContext gatewayContext = new GatewayContext();
		HttpHeaders headers = request.getHeaders();
		MediaType contentType = headers.getContentType();

		gatewayContext.setRequestHeaders(headers);
		gatewayContext.getAllRequestData().addAll(request.getQueryParams());
		if (gateWayCryptoProperties.checkCrypto(reqPath)) {
			return cacheRequest(exchange, chain, headers, contentType, gatewayContext);
		}
		else {
			return cacheRequest(exchange, chain, headers, contentType, gatewayContext);
		}
	}

	/**
	 * 缓存请求
	 * @param exchange
	 * @param chain
	 * @param headers
	 * @param contentType
	 * @param gatewayContext
	 * @return
	 */
	private Mono<Void> cacheRequest(ServerWebExchange exchange, WebFilterChain chain, HttpHeaders headers,
			MediaType contentType, GatewayContext gatewayContext) {
		if (headers.getContentLength() > 0) {
			if (MediaType.APPLICATION_JSON.equals(contentType) || MediaType.APPLICATION_JSON_UTF8.equals(contentType)) {
				return readBody(exchange, chain, gatewayContext);
			}
			if (MediaType.APPLICATION_FORM_URLENCODED.equals(contentType)) {
				return readFormData(exchange, chain, gatewayContext);
			}
		}
		return chain.filter(exchange);
	}

	private Mono<Void> readFormData(ServerWebExchange exchange, WebFilterChain chain, GatewayContext gatewayContext) {
		// 保存网关上下文到交换机
		exchange.getAttributes().put(GatewayContext.CACHE_GATEWAY_CONTEXT, gatewayContext);
		return chain.filter(exchange);
	}

	private Mono<Void> readBody(ServerWebExchange exchange, WebFilterChain chain, GatewayContext gatewayContext) {
		// 保存网关上下文到交换机
		exchange.getAttributes().put(GatewayContext.CACHE_GATEWAY_CONTEXT, gatewayContext);
		return chain.filter(exchange);
	}

	@Override
	public int getOrder() {
		return Integer.MAX_VALUE - 1;
	}

}
