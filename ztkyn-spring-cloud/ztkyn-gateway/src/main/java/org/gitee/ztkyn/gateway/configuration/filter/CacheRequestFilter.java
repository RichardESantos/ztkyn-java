package org.gitee.ztkyn.gateway.configuration.filter;

import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * 设置全局过滤，构建可重复读request body
 */
public class CacheRequestFilter implements WebFilter, Ordered {

	@Override
	public int getOrder() {
		return Integer.MIN_VALUE + 1;
	}

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
		// GET DELETE 不过滤
		HttpMethod method = exchange.getRequest().getMethod();
		if (method == null || method == HttpMethod.GET || method == HttpMethod.DELETE) {
			return chain.filter(exchange);
		}
		return ServerWebExchangeUtils.cacheRequestBodyAndRequest(exchange, (serverHttpRequest) -> {
			if (serverHttpRequest == exchange.getRequest()) {
				return chain.filter(exchange);
			}
			return chain.filter(exchange.mutate().request(serverHttpRequest).build());
		});
	}

}