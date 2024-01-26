package org.gitee.ztkyn.gateway.configuration.filter;

import jakarta.validation.constraints.NotNull;
import org.gitee.ztkyn.core.string.StringUtil;
import org.gitee.ztkyn.gateway.configuration.context.GatewayContext;
import org.gitee.ztkyn.gateway.configuration.properties.GateWayCryptoProperties;
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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.function.Supplier;

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
        URI requestURI = request.getURI();
        String reqPath = requestURI.getPath();
        String uriQuery = requestURI.getQuery();
        HttpHeaders headers = request.getHeaders();
        MediaType contentType = headers.getContentType();
        // 对于表单直接跳过处理
        if (Objects.nonNull(contentType) && MediaType.MULTIPART_FORM_DATA.isCompatibleWith(contentType)) {
            return chain.filter(exchange);
        }

        if (gateWayCryptoProperties.checkCrypto(reqPath)) {
            return cacheRequest(exchange, chain, headers, contentType, request, true);
        } else {
            return cacheRequest(exchange, chain, headers, contentType, request, false);
        }
    }

    /**
     * 缓存请求
     *
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
        GatewayContext gatewayContext = new GatewayContext();
        gatewayContext.setRequestHeaders(headers);
        if (headers.getContentLength() > 0) {
            if (MediaType.APPLICATION_JSON.equals(contentType) || MediaType.APPLICATION_JSON_UTF8.equals(contentType)) {
                return readBody(exchange, chain, gatewayContext, request, isCrypto);
            }
            if (MediaType.APPLICATION_FORM_URLENCODED.equals(contentType)) {
                return readFormData(exchange, chain, gatewayContext, request, contentType, isCrypto);
            }
        } else {
            // 解决 GET 请求 contentType == null 的问题
            // 通过 request.getURI().getQuery() 获取参数
            if (StringUtil.isNotBlank(request.getURI().getQuery())) {
                return readUrlEncodeParam(exchange, chain, gatewayContext, request, contentType, isCrypto);
            }
        }
        return chain.filter(exchange);
    }

    private Mono<Void> readUrlEncodeParam(ServerWebExchange exchange, WebFilterChain chain,
                                          GatewayContext gatewayContext, ServerHttpRequest request, MediaType contentType, boolean isCrypto) {
        return Mono.defer((Supplier<Mono<Void>>) () -> {
            // 只有加密的情况下才需要进行重新包装
            if (isCrypto) {
                // 对 连接后面的参数进行解密以及解析（?后面的内容全加密）


                // getQueryParams() 方法只处理查询参数（即URL中的参数）
                gatewayContext.getAllRequestData().addAll(request.getQueryParams());
                return reRequestDecorate(exchange, chain, gatewayContext, gatewayContext.getAllRequestData(), Charset.defaultCharset(), isCrypto);
            }
            return chain.filter(exchange);

        });
    }

    private static Mono<Void> reRequestDecorate(ServerWebExchange exchange, WebFilterChain chain,
                                                GatewayContext gatewayContext, MultiValueMap<String, String> allRequestData, Charset charset,
                                                boolean isCrypto) {
        // 组装表单数据
        StringJoiner formDataBodyBuilder = new StringJoiner("&");
        String entryKey;
        List<String> entryValue;
        // 重新包装表单数据 拼接成url形式
        for (Map.Entry<String, List<String>> entry : allRequestData.entrySet()) {
            entryKey = entry.getKey();
            entryValue = entry.getValue();
            for (String s : entryValue) {
                formDataBodyBuilder.add(entryKey + "=" + URLEncoder.encode(s, charset));
            }
        }
        String formDataBodyString = formDataBodyBuilder.toString();
        // 保存网关上下文到交换机,方便后续过滤器中使用，而不是再次进行包装请求
        exchange.getAttributes().put(GatewayContext.CACHE_GATEWAY_CONTEXT, gatewayContext);
        // 获取数据byte
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.putAll(exchange.getRequest().getHeaders());
        httpHeaders.remove(HttpHeaders.CONTENT_LENGTH);
        httpHeaders.setContentLength(formDataBodyString.getBytes(charset).length); // 如果内容长度不匹配
        // 用BodyInserter修改请求体
        BodyInserter<String, ReactiveHttpOutputMessage> bodyInserter = BodyInserters.fromValue(formDataBodyString);

        // 创建CachedBodyOutputMessage并且把请求param加入
        CachedBodyOutputMessage cachedBodyOutputMessage = new CachedBodyOutputMessage(exchange, httpHeaders);
        logger.debug("[GatewayContext]Rewrite Form Data :{}", formDataBodyBuilder);
        return bodyInserter.insert(cachedBodyOutputMessage, new BodyInserterContext()).then(Mono.defer(() -> {
            // 重新封装请求
            ServerHttpRequestDecorator decorator = new ServerHttpRequestDecorator(exchange.getRequest()) {
                @NotNull
                @Override
                public HttpHeaders getHeaders() {
                    return httpHeaders;
                }

                @NotNull
                @Override
                public Flux<DataBuffer> getBody() {
                    return cachedBodyOutputMessage.getBody();
                }
            };
            return chain.filter(exchange.mutate().request(decorator).build());
        }));
    }

    private Mono<Void> readFormData(ServerWebExchange exchange, WebFilterChain chain, GatewayContext gatewayContext,
                                    ServerHttpRequest request, MediaType contentType, boolean isCrypto) {
        return exchange.getFormData().doOnNext(multiValueMap -> {
            // 放入form表单数据
            gatewayContext.setFormData(multiValueMap);
            gatewayContext.getAllRequestData().addAll(multiValueMap);
            logger.debug("[GatewayContext]读取表单数据成功");
        }).then(Mono.defer((Supplier<Mono<Void>>) () -> {
            Charset charset = getCharset(contentType);
            MultiValueMap<String, String> formData = gatewayContext.getAllRequestData();
            // 表单数据为空直接返回
            if (CollectionUtils.isEmpty(formData)) {
                return chain.filter(exchange);
            }

            return reRequestDecorate(exchange, chain, gatewayContext, formData,  getCharset(contentType), isCrypto);
        }));
    }

    private static Charset getCharset(MediaType contentType) {
        Charset charset = Objects.isNull(contentType) ? StandardCharsets.UTF_8 : contentType.getCharset();
        return Objects.isNull(charset) ? StandardCharsets.UTF_8 : charset;
    }

    private Mono<Void> readBody(ServerWebExchange exchange, WebFilterChain chain, GatewayContext gatewayContext,
                                ServerHttpRequest request, boolean isCrypto) {
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
                DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
                DataBufferUtils.retain(buffer);
                return Mono.just(buffer);
            });

            // 保存网关上下文到交换机,方便后续过滤器中使用，而不是再次进行包装请求
            exchange.getAttributes().put(GatewayContext.CACHE_GATEWAY_CONTEXT, gatewayContext);
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
        return Integer.MAX_VALUE - 1;
    }

}
