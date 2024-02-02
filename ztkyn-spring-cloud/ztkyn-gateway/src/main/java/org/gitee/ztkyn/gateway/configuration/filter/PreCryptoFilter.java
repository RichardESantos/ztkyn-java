package org.gitee.ztkyn.gateway.configuration.filter;

import cn.hutool.crypto.asymmetric.SM2;
import org.gitee.ztkyn.core.crypto.ZtkynSMUtil;
import org.gitee.ztkyn.core.string.StringUtil;
import org.gitee.ztkyn.gateway.configuration.context.GatewayContext;
import org.gitee.ztkyn.gateway.configuration.properties.GateWayCryptoKeyProperties;
import org.gitee.ztkyn.gateway.configuration.properties.GateWayCryptoProperties;
import org.gitee.ztkyn.web.utils.RequestUtil;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.factory.rewrite.CachedBodyOutputMessage;
import org.springframework.cloud.gateway.support.BodyInserterContext;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ReactiveHttpOutputMessage;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * 接口解密处理器
 */
public class PreCryptoFilter implements WebFilter, Ordered {

	private static final Logger logger = LoggerFactory.getLogger(PreCryptoFilter.class);

	private final GateWayCryptoProperties gateWayCryptoProperties;

	private final SM2 sm2Crypto;

	public PreCryptoFilter(GateWayCryptoProperties gateWayCryptoProperties,
			GateWayCryptoKeyProperties gateWayCryptoKeyProperties) {
		this.gateWayCryptoProperties = gateWayCryptoProperties;
		this.sm2Crypto = ZtkynSMUtil.genSM2(gateWayCryptoKeyProperties.getBackendPrivateKey(),
				gateWayCryptoKeyProperties.getBackendPublicKey());
	}

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
		ServerHttpRequest request = exchange.getRequest();
		// 请求路径
		URI requestURI = request.getURI();
		String reqPath = requestURI.getPath();
		HttpHeaders headers = request.getHeaders();
		MediaType contentType = headers.getContentType();
		// 对于表单直接跳过处理
		if (Objects.nonNull(contentType) && MediaType.MULTIPART_FORM_DATA.isCompatibleWith(contentType)) {
			return chain.filter(exchange);
		}

		if (gateWayCryptoProperties.checkCrypto(reqPath)) {
			logger.debug("进行接口加密处理");
			return cacheRequest(exchange, chain, headers, contentType, request, true);
		}
		else {
			return cacheRequest(exchange, chain, headers, contentType, request, false);
		}
	}

	/**
	 * 缓存请求
	 * @param exchange
	 * @param chain
	 * @param headers
	 * @param contentType
	 * @param request
	 * @param isCrypto
	 * @return
	 */
	private Mono<Void> cacheRequest(ServerWebExchange exchange, WebFilterChain chain, HttpHeaders headers,
			MediaType contentType, ServerHttpRequest request, boolean isCrypto) {
		if (headers.getContentLength() > 0) {
			if (MediaType.APPLICATION_JSON.isCompatibleWith(contentType)
					|| MediaType.APPLICATION_JSON_UTF8.isCompatibleWith(contentType)) {
				return readBody(exchange, chain, isCrypto);
			}
			if (MediaType.APPLICATION_FORM_URLENCODED.isCompatibleWith(contentType)) {
				return readFormData(exchange, chain, request, contentType, isCrypto);
			}
		}
		else {
			// 解决 GET 请求 contentType == null 的问题
			// 通过 request.getURI().getQuery() 获取参数
			if (StringUtil.isNotBlank(request.getURI().getQuery())) {
				return readUrlEncodeParam(exchange, chain, request, isCrypto);
			}
		}
		return chain.filter(exchange);
	}

	private Mono<Void> readBody(ServerWebExchange exchange, WebFilterChain chain, boolean isCrypto) {
		// 当body中没有缓存时，只会执行这一个拦截器， 原因是fileMap中的代码没有执行，所以需要在为空时构建一个空的缓存
		DefaultDataBufferFactory defaultDataBufferFactory = new DefaultDataBufferFactory();
		DefaultDataBuffer defaultDataBuffer = defaultDataBufferFactory.allocateBuffer(0);
		// 构建新数据流， 当body为空时，构建空流
		Flux<DataBuffer> bodyDataBuffer = exchange.getRequest().getBody().defaultIfEmpty(defaultDataBuffer);
		return DataBufferUtils.join(bodyDataBuffer).flatMap(dataBuffer -> {
			byte[] bytes = new byte[dataBuffer.readableByteCount()];
			dataBuffer.read(bytes);
			DataBufferUtils.release(dataBuffer);
			Flux<DataBuffer> cachedFlux = Flux.defer(() -> {
				DataBuffer buffer = exchange.getResponse()
					.bufferFactory()
					.wrap(isCrypto ? decodeBytes(new String(bytes, StandardCharsets.UTF_8), exchange) : bytes);
				DataBufferUtils.retain(buffer);
				return Mono.just(buffer);
			});

			/*
			 * repackage ServerHttpRequest 重新包装请求
			 */
			ServerHttpRequest mutatedRequest = new ServerHttpRequestDecorator(exchange.getRequest()) {
				@Override
				public Flux<DataBuffer> getBody() {
					return cachedFlux;
				}
			};
			return chain.filter(exchange.mutate().request(mutatedRequest).build());
		});
	}

	private byte[] decodeBytes(String cryptoContent, ServerWebExchange exchange) {
		// 保存网关上下文到交换机
		exchange.getAttributes()
			.put(GatewayContext.CACHE_GATEWAY_SM4_KEY, ZtkynSMUtil.getMixSM4Key(sm2Crypto, cryptoContent));
		return ZtkynSMUtil.mixDecodeHex(sm2Crypto, cryptoContent).getBytes(StandardCharsets.UTF_8);
	}

	private Mono<Void> readUrlEncodeParam(ServerWebExchange exchange, WebFilterChain chain, ServerHttpRequest request,
			boolean isCrypto) {
		return Mono.defer((Supplier<Mono<Void>>) () -> {
			// 只有加密的情况下才需要进行重新包装
			if (isCrypto) {
				// 对 连接后面的参数进行解密以及解析（?后面的内容全加密）
				URI requestURI = request.getURI();
				byte[] bytes = decodeBytes(requestURI.getQuery(), exchange);
				// 获取数据byte
				HttpHeaders httpHeaders = new HttpHeaders();
				httpHeaders.putAll(exchange.getRequest().getHeaders());
				httpHeaders.remove(HttpHeaders.CONTENT_LENGTH);
				httpHeaders.setContentLength(bytes.length); // 如果内容长度不匹配
				String decodeQuery = new String(bytes, StandardCharsets.UTF_8);
				// 用BodyInserter修改请求体
				BodyInserter<String, ReactiveHttpOutputMessage> bodyInserter = BodyInserters.fromValue(decodeQuery);

				// 创建CachedBodyOutputMessage并且把请求param加入
				CachedBodyOutputMessage cachedBodyOutputMessage = new CachedBodyOutputMessage(exchange, httpHeaders);
				logger.debug("[GatewayContext]Rewrite Form Data :{}", decodeQuery);
				return bodyInserter.insert(cachedBodyOutputMessage, new BodyInserterContext()).then(Mono.defer(() -> {

					// 重新封装请求
					ServerHttpRequestDecorator decorator = new ServerHttpRequestDecorator(exchange.getRequest()) {

						@Override
						public HttpHeaders getHeaders() {
							return httpHeaders;
						}

						@NotNull
						@Override
						public MultiValueMap<String, String> getQueryParams() {
							return RequestUtil.parseFromUrl(decodeQuery);
						}
					};
					return chain.filter(exchange.mutate().request(decorator).build());
				}));
			}
			return chain.filter(exchange);

		});
	}

	private Mono<Void> readFormData(ServerWebExchange exchange, WebFilterChain chain, ServerHttpRequest request,
			MediaType contentType, boolean isCrypto) {
		// 当body中没有缓存时，只会执行这一个拦截器， 原因是fileMap中的代码没有执行，所以需要在为空时构建一个空的缓存
		DefaultDataBufferFactory defaultDataBufferFactory = new DefaultDataBufferFactory();
		DefaultDataBuffer defaultDataBuffer = defaultDataBufferFactory.allocateBuffer(0);
		// 构建新数据流， 当body为空时，构建空流
		Flux<DataBuffer> bodyDataBuffer = exchange.getRequest().getBody().defaultIfEmpty(defaultDataBuffer);
		return DataBufferUtils.join(bodyDataBuffer).flatMap(dataBuffer -> {
			byte[] bytes = new byte[dataBuffer.readableByteCount()];
			dataBuffer.read(bytes);
			DataBufferUtils.release(dataBuffer);
			Flux<DataBuffer> cachedFlux = Flux.defer(() -> {
				DataBuffer buffer = exchange.getResponse()
					.bufferFactory()
					.wrap(isCrypto ? decodeBytes(new String(bytes, StandardCharsets.UTF_8), exchange) : bytes);
				DataBufferUtils.retain(buffer);
				return Mono.just(buffer);
			});

			/*
			 * repackage ServerHttpRequest 重新包装请求
			 */
			ServerHttpRequest mutatedRequest = new ServerHttpRequestDecorator(exchange.getRequest()) {
				@Override
				public Flux<DataBuffer> getBody() {
					return cachedFlux;
				}
			};
			return chain.filter(exchange.mutate().request(mutatedRequest).build());
		});
	}

	@Override
	public int getOrder() {
		return Integer.MIN_VALUE;
	}

}
