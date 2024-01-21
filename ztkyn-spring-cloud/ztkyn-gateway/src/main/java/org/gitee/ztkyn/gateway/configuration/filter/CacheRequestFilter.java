package org.gitee.ztkyn.gateway.configuration.filter;

import org.gitee.ztkyn.gateway.configuration.context.GatewayContext;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

public class CacheRequestFilter implements WebFilter, Ordered {
    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        GatewayContext gatewayContext = new GatewayContext();
        HttpHeaders headers = request.getHeaders();
        MediaType contentType = headers.getContentType();

        gatewayContext.setRequestHeaders(headers);
        gatewayContext.getAllRequestData().addAll(request.getQueryParams());

        if (headers.getContentLength() > 0) {
            if (MediaType.APPLICATION_JSON.equals(contentType) || MediaType.APPLICATION_JSON_UTF8.equals(contentType)) {
                return readBody(exchange, chain, gatewayContext);
            }
            if (MediaType.APPLICATION_FORM_URLENCODED.equals(contentType)) {
                return readFormData(exchange, chain, gatewayContext);
            }
        }
        return chain.filter(exchange);
    }

    private Mono<Void> readFormData(ServerWebExchange exchange, WebFilterChain chain, GatewayContext gatewayContext) {
        //保存网关上下文到交换机
        exchange.getAttributes().put(GatewayContext.CACHE_GATEWAY_CONTEXT, gatewayContext);
        return chain.filter(exchange);
    }

    private Mono<Void> readBody(ServerWebExchange exchange, WebFilterChain chain, GatewayContext gatewayContext) {
        //保存网关上下文到交换机
        exchange.getAttributes().put(GatewayContext.CACHE_GATEWAY_CONTEXT, gatewayContext);
        return chain.filter(exchange);
    }
}
