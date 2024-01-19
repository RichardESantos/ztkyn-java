package org.gitee.ztkyn.gateway.configuration.filter;

import org.gitee.ztkyn.gateway.configuration.properties.GateWaySignProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * 接口签名
 */
public class PreSignatureFilter implements WebFilter, Ordered {

	private static final Logger logger = LoggerFactory.getLogger(PreSignatureFilter.class);

	private final GateWaySignProperties gateWaySignProperties;

	public PreSignatureFilter(GateWaySignProperties gateWaySignProperties) {
		this.gateWaySignProperties = gateWaySignProperties;
	}

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
		logger.info("进行接口签名处理");
		ServerHttpRequest request = exchange.getRequest();
		String reqPath = request.getURI().getPath();
		if (gateWaySignProperties.checkSign(reqPath)) {

		}

		return chain.filter(exchange);
	}

	@Override
	public int getOrder() {
		return Integer.MAX_VALUE;
	}

}
