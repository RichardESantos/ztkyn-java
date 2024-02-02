package org.gitee.ztkyn.gateway.configuration.filter;

import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.crypto.digest.Digester;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.gitee.ztkyn.gateway.configuration.context.GateWayConstants;
import org.gitee.ztkyn.gateway.configuration.properties.GateWaySignProperties;
import org.gitee.ztkyn.web.utils.RequestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static org.gitee.ztkyn.core.json.JsonObjectUtil.jsonConfig;
import static org.gitee.ztkyn.core.json.JsonObjectUtil.sortFullJson;
import static org.gitee.ztkyn.web.exception.RequestDeniedException.PARAM_VERIFY_FAIL;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.CACHED_REQUEST_BODY_ATTR;

/**
 * 接口签名
 */
public class PreSignatureFilter implements WebFilter, Ordered {

	private static final Logger logger = LoggerFactory.getLogger(PreSignatureFilter.class);

	private final GateWaySignProperties gateWaySignProperties;

	private static final Digester digester = DigestUtil.digester("sm3");

	public PreSignatureFilter(GateWaySignProperties gateWaySignProperties) {
		this.gateWaySignProperties = gateWaySignProperties;
	}

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
		ServerHttpRequest request = exchange.getRequest();
		String reqPath = request.getURI().getPath();
		HttpHeaders headers = request.getHeaders();
		MediaType contentType = headers.getContentType();
		MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
		if (Objects.nonNull(contentType) && MediaType.MULTIPART_FORM_DATA.isCompatibleWith(contentType)) {
			return chain.filter(exchange);
		}
		else if (gateWaySignProperties.checkSign(reqPath)) {
			logger.debug("进行接口签名处理");
			if (Objects.nonNull(contentType) && MediaType.APPLICATION_JSON.isCompatibleWith(contentType)) {
				DataBuffer dataBuffer = exchange.getAttributeOrDefault(CACHED_REQUEST_BODY_ATTR,
						new DefaultDataBufferFactory().allocateBuffer(0));
				String content = dataBuffer.toString(StandardCharsets.UTF_8);
				return checkSignKey(exchange, chain, JSONUtil.parseObj(content));
			}
			else if (Objects.nonNull(contentType)
					&& MediaType.APPLICATION_FORM_URLENCODED.isCompatibleWith(contentType)) {
				return request.getBody().doOnNext(dataBuffer -> {
					queryParams.addAll(RequestUtil.parseFromUrl(dataBuffer.toString(StandardCharsets.UTF_8)));
				})
					.then(Mono.defer(() -> checkSignKey(exchange, chain,
							JSONUtil.parseObj(RequestUtil.transferMulti(queryParams), jsonConfig))));
			}
			else {
				queryParams.addAll(request.getQueryParams());
				if (!queryParams.isEmpty()) {
					return checkSignKey(exchange, chain,
							JSONUtil.parseObj(RequestUtil.transferMulti(queryParams), jsonConfig));
				}

			}
		}
		return chain.filter(exchange);
	}

	private static Mono<Void> checkSignKey(ServerWebExchange exchange, WebFilterChain chain, JSONObject jsonObject) {
		Object signKey = jsonObject.remove(GateWayConstants.SIGN_SIGN_KEY);
		if (Objects.nonNull(signKey)) {
			JSONObject newJsonObject = sortFullJson(jsonObject);
			String paramJson = newJsonObject.toString();
			if (Objects.equals(signKey, digester.digestHex(paramJson))) {
				return chain.filter(exchange);
			}
		}
		throw PARAM_VERIFY_FAIL;
	}

	@Override
	public int getOrder() {
		return Integer.MAX_VALUE;
	}

}
