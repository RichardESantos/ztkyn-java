package org.gitee.ztkyn.gateway.configuration.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

@ConfigurationProperties(prefix = "ztkyn.gateway")
@Getter
@Setter
@Accessors(chain = true)
public class ZtkynGateWayProperties {

	/**
	 * 接口签名配置
	 */
	@NestedConfigurationProperty
	private GateWaySignProperties sign = new GateWaySignProperties();

	/**
	 * 接口加密配置
	 */
	@NestedConfigurationProperty
	private GateWayCryptoProperties crypto = new GateWayCryptoProperties();

}
