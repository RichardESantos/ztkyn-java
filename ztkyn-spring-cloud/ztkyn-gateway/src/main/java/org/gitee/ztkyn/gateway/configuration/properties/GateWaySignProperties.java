package org.gitee.ztkyn.gateway.configuration.properties;

import com.alibaba.nacos.common.utils.ConcurrentHashSet;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Set;

@Getter
@Setter
@Accessors(chain = true)
public class GateWaySignProperties {

	/**
	 * 是否开启数字签名
	 */
	private boolean enable = true;

	/**
	 * 忽略签名的的接口
	 */
	private Set<String> ignoreUrls = new ConcurrentHashSet<>();

	/**
	 * 验证是否需要进行签名
	 * @param url
	 * @return
	 */
	public boolean checkSign(String url) {
		return isEnable() && !ignoreUrls.contains(url);
	}

}
