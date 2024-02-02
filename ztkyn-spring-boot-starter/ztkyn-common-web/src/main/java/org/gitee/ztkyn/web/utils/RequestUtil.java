package org.gitee.ztkyn.web.utils;

import org.gitee.ztkyn.core.string.StringUtil;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RequestUtil {

	private static final Pattern QUERY_PATTERN = Pattern.compile("([^&=]+)(=?)([^&]+)?");

	/**
	 * 从url 解析出 参数
	 * @param url
	 * @return
	 */
	public static MultiValueMap<String, String> parseFromUrl(String url) {
		MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
		if (StringUtil.isNotBlank(url)) {
			Matcher matcher = QUERY_PATTERN.matcher(url);
			while (matcher.find()) {
				String name = decodeQueryParam(matcher.group(1));
				String eq = matcher.group(2);
				String value = matcher.group(3);
				value = (value != null ? decodeQueryParam(value) : (StringUtils.hasLength(eq) ? "" : null));
				queryParams.add(name, value);
			}
		}
		return queryParams;
	}

	private static String decodeQueryParam(String value) {
		return URLDecoder.decode(value, StandardCharsets.UTF_8);
	}

	/**
	 * 从url 解析出 参数
	 * @param url
	 * @return
	 */
	public static Map<String, Object> transfer(String url) {
		return transferMulti(parseFromUrl(url));
	}

	/**
	 * 结构变换
	 * @param queryParams
	 * @return
	 */
	public static Map<String, Object> transferMulti(MultiValueMap<String, String> queryParams) {
		Map<String, Object> transferMap = new HashMap<>(queryParams.size());
		queryParams.forEach((k, v) -> {
			int size = v.size();
			if (size == 1) {
				transferMap.put(k, v.get(0));
			}
			else if (size > 1) {
				transferMap.put(k, v);
			}
		});
		return transferMap;
	}

}
