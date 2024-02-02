package org.gitee.ztkyn.gateway.configuration.context;

import org.springframework.util.AntPathMatcher;

import java.util.ArrayList;
import java.util.List;

/**
 * 常用的一些常量
 */
public class GateWayConstants {

	/**
	 * 客户端ID KEY
	 */
	public static final String SIGN_API_KEY = "ApiKey";

	/**
	 * 客户端秘钥 KEY
	 */
	public static final String SIGN_SECRET_KEY = "ZtkynSecretKey";

	/**
	 * 随机字符串 KEY
	 */
	public static final String SIGN_NONCE_KEY = "Nonce";

	/**
	 * 时间戳 KEY
	 */
	public static final String SIGN_TIMESTAMP_KEY = "Timestamp";

	/**
	 * 签名类型 KEY
	 */
	public static final String SIGN_SIGN_TYPE_KEY = "SignType";

	/**
	 * 签名结果 KEY
	 */
	public static final String SIGN_SIGN_KEY = "SignKey";

	public static final AntPathMatcher PATH_MATCH = new AntPathMatcher();

	/**
	 * 默认忽略加密与签名的接口
	 */
	public static List<String> ignoreUrls = new ArrayList<>() {
		{
			add("/");
			add("/crypto");
			add("/error");
			add("/favicon.ico");
			add("/actuator/**");
			add("/**/v3/api-docs/**");
			add("/**/swagger-resources/**");
			add("/webjars/**");
			add("/doc.html");
			add("/swagger-ui.html");
		}
	};

}
