package org.gitee.ztkyn.gateway.configuration.filter;

import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import org.springframework.web.server.handler.FilteringWebHandler;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 包装自定义filter,进入到 WebFilterChain 参考 SecurityWebFilterChain ，便于控制自定义 filter 的执行的先后顺序
 */
public class FilterChainProxy implements WebFilter {

	private final List<WebFilter> webFilters;

	public FilterChainProxy(List<WebFilter> webFilters) {
		this.webFilters = webFilters;
	}

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
		FilteringWebHandler filteringWebHandler = new FilteringWebHandler(chain::filter, this.webFilters);
		return filteringWebHandler.handle(exchange);
	}

}
