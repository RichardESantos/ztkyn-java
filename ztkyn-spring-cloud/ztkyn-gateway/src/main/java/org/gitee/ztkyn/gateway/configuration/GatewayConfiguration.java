package org.gitee.ztkyn.gateway.configuration;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.gitee.ztkyn.gateway.configuration.filter.*;
import org.gitee.ztkyn.gateway.configuration.properties.ZtkynGateWayProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.WebFluxConfigurer;

import java.util.Arrays;

@Configuration
@EnableConfigurationProperties({ ZtkynGateWayProperties.class })
public class GatewayConfiguration implements WebFluxConfigurer {

	private static final Logger logger = LoggerFactory.getLogger(GatewayConfiguration.class);

	@Resource
	private ZtkynGateWayProperties ztkynGateWayProperties;

	@Resource
	private ApplicationContext applicationContext;

	@PostConstruct
	public void init() {
	}

	// @Bean
	// public CacheRequestFilter cacheRequestFilter() {
	// return new CacheRequestFilter();
	// }

	@Bean
	public FilterChainProxy filterChainProxy() {
		return new FilterChainProxy(Arrays.asList(new PreCryptoFilter(ztkynGateWayProperties.getCrypto()),
				new PreSignatureFilter(ztkynGateWayProperties.getSign())));
	}

	@Bean
	public ZtKynExampleFilter testFilter() {
		return new ZtKynExampleFilter();
	}

}
