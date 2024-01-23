package org.gitee.ztkyn.gateway.configuration.filter;

import org.gitee.ztkyn.gateway.configuration.properties.GateWaySignProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

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
		HttpHeaders headers = request.getHeaders();
		MediaType contentType = headers.getContentType();
		MultiValueMap<String, String> queryParams = request.getQueryParams();
		Flux<DataBuffer> body = request.getBody();
		if (gateWaySignProperties.checkSign(reqPath)) {
			if (Objects.nonNull(contentType) && MediaType.APPLICATION_JSON.equals(contentType)) {

			}
			else if (Objects.nonNull(contentType) && MediaType.APPLICATION_FORM_URLENCODED.equals(contentType)) {

			}
			else {

			}
		}

		return chain.filter(exchange);
	}

	@Override
	public int getOrder() {
		return Integer.MAX_VALUE;
	}

}
