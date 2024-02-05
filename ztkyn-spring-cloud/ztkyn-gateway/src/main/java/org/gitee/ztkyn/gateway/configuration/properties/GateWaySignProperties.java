package org.gitee.ztkyn.gateway.configuration.properties;

import com.alibaba.nacos.common.utils.ConcurrentHashSet;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Set;

import static org.gitee.ztkyn.gateway.configuration.context.GateWayConstants.PATH_MATCH;

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
	 * 接口有效时间，防止超时重放(1s)
	 */
	private long validTime = 1000;

	/**
	 * 验证是否需要进行签名
	 * @param url
	 * @return
	 */
	public boolean checkSign(String url) {
		return isEnable() && !isChecked(url);
	}

	/**
	 * 校验路径规则
	 * @param url
	 * @return
	 */
	private boolean isChecked(String url) {
		return ignoreUrls.stream().anyMatch(s -> PATH_MATCH.match(s, url));
	}

}
