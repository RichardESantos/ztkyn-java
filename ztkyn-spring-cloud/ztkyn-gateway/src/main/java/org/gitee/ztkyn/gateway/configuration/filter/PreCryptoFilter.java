package org.gitee.ztkyn.gateway.configuration.filter;

import org.gitee.ztkyn.gateway.configuration.properties.GateWayCryptoProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

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
		String reqPath = request.getURI().getPath();
		if (gateWayCryptoProperties.checkCrypto(reqPath)) {

		}

		return chain.filter(exchange);
	}

	@Override
	public int getOrder() {
		return Integer.MAX_VALUE - 1;
	}

}
