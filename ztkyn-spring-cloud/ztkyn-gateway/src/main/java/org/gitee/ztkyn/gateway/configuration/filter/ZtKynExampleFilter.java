package org.gitee.ztkyn.gateway.configuration.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * 用来进行测试的filter，测试 FilterChainProxy 不会影响其他 不在 FilterChainProxy中filterChain 中的 filter
 * 的正常执行
 */
public class ZtKynExampleFilter implements WebFilter {

	private static final Logger logger = LoggerFactory.getLogger(ZtKynExampleFilter.class);

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
		logger.info("{} 正常执行", ZtKynExampleFilter.class.getName());
		return chain.filter(exchange);
	}

}
