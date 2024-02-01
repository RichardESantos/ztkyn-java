package org.gitee.ztkyn.gateway.configuration;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.gitee.ztkyn.gateway.configuration.context.GateWayConstants;
import org.gitee.ztkyn.gateway.configuration.filter.*;
import org.gitee.ztkyn.gateway.configuration.properties.GateWayCryptoKeyProperties;
import org.gitee.ztkyn.gateway.configuration.properties.GateWayCryptoProperties;
import org.gitee.ztkyn.gateway.configuration.properties.GateWaySignProperties;
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

	@Bean
	public FilterChainProxy filterChainProxy() {
		GateWayCryptoProperties crypto = ztkynGateWayProperties.getCrypto();
		GateWayCryptoKeyProperties cryptoKey = ztkynGateWayProperties.getCryptoKey();
		GateWaySignProperties sign = ztkynGateWayProperties.getSign();
		// 初始化默认不需要进行 加密 + 验签 的接口
		crypto.getIgnoreUrls().addAll(GateWayConstants.ignoreUrls);
		sign.getIgnoreUrls().addAll(GateWayConstants.ignoreUrls);
		return new FilterChainProxy(Arrays.asList(new PreCryptoFilter(crypto, cryptoKey), new CacheRequestFilter(),
				new PreSignatureFilter(sign)));
	}

	@Bean
	public ZtKynExampleFilter testFilter() {
		return new ZtKynExampleFilter();
	}

}
