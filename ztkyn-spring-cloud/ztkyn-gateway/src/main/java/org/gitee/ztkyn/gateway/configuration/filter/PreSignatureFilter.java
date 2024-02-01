package org.gitee.ztkyn.gateway.configuration.filter;

import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.crypto.digest.Digester;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONConfig;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
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

import static org.gitee.ztkyn.web.exception.RequestDeniedException.PARAM_VERIFY_FAIL;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.CACHED_REQUEST_BODY_ATTR;

/**
 * 接口签名
 */
public class PreSignatureFilter implements WebFilter, Ordered {

	private static final Logger logger = LoggerFactory.getLogger(PreSignatureFilter.class);

	private final GateWaySignProperties gateWaySignProperties;

	private static final Digester digester = DigestUtil.digester("sm3");

	private static final JSONConfig jsonConfig = new JSONConfig().setNatureKeyComparator().setIgnoreNullValue(true);

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
				return checkSignKey(exchange, chain, JSONUtil.parseObj(content));
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
				return checkSignKey(exchange, chain, JSONUtil.parseObj(listMap, jsonConfig));
			}
		}
		return chain.filter(exchange);
	}

	private static Mono<Void> checkSignKey(ServerWebExchange exchange, WebFilterChain chain, JSONObject jsonObject) {
		Object signKey = jsonObject.remove(GateWayConstants.SIGN_SIGN_KEY);
		if (Objects.nonNull(signKey)) {
			JSONObject newJsonObject = sortFullJson(jsonObject);
			if (Objects.equals(signKey, digester.digestHex(newJsonObject.toString()))) {
				return chain.filter(exchange);
			}
		}
		throw PARAM_VERIFY_FAIL;
	}

	@Override
	public int getOrder() {
		return Integer.MAX_VALUE;
	}

	private static JSONObject sortFullJson(JSONObject jsonObject) {
		JSONObject newValue = new JSONObject(jsonConfig);
		jsonObject.entrySet().forEach(stringObjectEntry -> {
			Object entryValue = stringObjectEntry.getValue();
			if (entryValue instanceof JSONObject) {
				stringObjectEntry.setValue(sortFullJson((JSONObject) entryValue));
			}
			else if (entryValue instanceof JSONArray) {
				stringObjectEntry.setValue(sortFullJson((JSONArray) entryValue));
			}
		});
		jsonObject.entrySet()
			.stream()
			.sorted((o1, o2) -> o1.getKey().compareToIgnoreCase(o2.getKey()))
			.toList()
			.forEach(stringObjectEntry -> newValue.set(stringObjectEntry.getKey(), stringObjectEntry.getValue()));
		return newValue;
	}

	private static JSONArray sortFullJson(JSONArray jsonArray) {
		JSONArray newValue = new JSONArray();
		jsonArray.forEach(object -> {
			if (object instanceof JSONObject) {
				newValue.add(sortFullJson((JSONObject) object));
			}
			else if (object instanceof JSONArray) {
				newValue.add(sortFullJson((JSONArray) object));
			}
			else {
				newValue.add(object);
			}
		});
		return newValue;
	}

}
