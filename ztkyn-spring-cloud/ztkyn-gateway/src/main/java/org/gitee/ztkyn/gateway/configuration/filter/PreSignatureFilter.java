package org.gitee.ztkyn.gateway.configuration.filter;

import org.gitee.ztkyn.core.json.JacksonUtil;
import org.gitee.ztkyn.gateway.configuration.context.GateWayConstants;
import org.gitee.ztkyn.gateway.configuration.properties.GateWaySignProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.CACHED_REQUEST_BODY_ATTR;

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
		ServerHttpRequest request = exchange.getRequest();
		String reqPath = request.getURI().getPath();
		HttpHeaders headers = request.getHeaders();
		MediaType contentType = headers.getContentType();
		MultiValueMap<String, String> queryParams = request.getQueryParams();
		if (gateWaySignProperties.checkSign(reqPath)) {
			logger.debug("进行接口签名处理");
			if (Objects.nonNull(contentType) && MediaType.APPLICATION_JSON.isCompatibleWith(contentType)) {
				DataBuffer dataBuffer = exchange.getAttributeOrDefault(CACHED_REQUEST_BODY_ATTR,
						new DefaultDataBufferFactory().allocateBuffer(0));
				String content = dataBuffer.toString(StandardCharsets.UTF_8);
				Map<String, Object> paramMap = JacksonUtil.json2Map(content, String.class, Object.class);
				Object signKey = paramMap.remove(GateWayConstants.SIGN_SIGN_KEY);
				System.out.println();
			}
			else if (Objects.nonNull(contentType)
					&& MediaType.APPLICATION_FORM_URLENCODED.isCompatibleWith(contentType)) {
			}
			else {
				Map<String, List<String>> listMap = queryParams.entrySet()
					.stream()
					.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (v1, v2) -> {
						v1.addAll(v2);
						return v1;
					}));
			}
		}

		return chain.filter(exchange);
	}

	@Override
	public int getOrder() {
		return Integer.MAX_VALUE;
	}

}
