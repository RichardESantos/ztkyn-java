package org.gitee.ztkyn.gateway.configuration.properties;

import com.alibaba.nacos.common.utils.ConcurrentHashSet;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Set;

/**
 * 接口加密配置
 */
@Getter
@Setter
@Accessors(chain = true)
public class GateWayCryptoProperties {

	private boolean enable = false;

	/**
	 * 忽略加密的的接口
	 */
	private Set<String> ignoreUrls = new ConcurrentHashSet<>();

	/**
	 * 验证是否需要进行签名
	 * @param url
	 * @return
	 */
	public boolean checkCrypto(String url) {
		return isEnable() && !ignoreUrls.contains(url);
	}

}
