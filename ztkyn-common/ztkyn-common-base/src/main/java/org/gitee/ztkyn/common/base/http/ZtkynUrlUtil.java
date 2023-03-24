package org.gitee.ztkyn.common.base.http;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringJoiner;

import lombok.Data;
import lombok.experimental.Accessors;
import org.gitee.ztkyn.common.base.ZtkynStringUtil;
import org.gitee.ztkyn.common.base.collection.ZtkynMapUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author whty
 * @version 1.0
 * @description url 解析工具
 * @date 2023/3/22 16:33
 */
public class ZtkynUrlUtil {

	private static final Logger logger = LoggerFactory.getLogger(ZtkynUrlUtil.class);

	public static UrlContent parseUrl(String requestUrl) {
		UrlContent urlContent = null;
		try {
			urlContent = new UrlContent().setUrl(new URL(requestUrl));
			// 从请求链接中解析参数
			getParamFromUrl(requestUrl, urlContent);
		}
		catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
		return urlContent;
	}

	public static String genPathUrl(String url, Map<String, Object> params) {
		url = ZtkynMapUtil.isNotBlank(params) ? url + "?" : url;
		StringJoiner joiner = new StringJoiner("&");
		for (Entry<String, Object> entry : params.entrySet()) {
			joiner.add(entry.getKey() + "=" + entry.getValue());
		}
		return url + joiner.toString();
	}

	/**
	 * 从请求链接中解析参数
	 * @param requestUrl
	 * @param urlContent
	 */
	private static void getParamFromUrl(String requestUrl, UrlContent urlContent) {
		int paramIndex = requestUrl.lastIndexOf("?");
		if (paramIndex > 0) {
			urlContent.setBaseUrl(requestUrl.substring(0, paramIndex));
			String paramStr = requestUrl.substring(paramIndex + 1);
			Map<String, Object> paramsMap = ZtkynMapUtil.createUnifiedMap(16);
			ZtkynStringUtil.splitToList(paramStr, '&', true).forEach(s -> {
				// 获取第一个 = 所在的位置
				int splitIndex = s.indexOf("=");
				String paramKey = s.substring(0, splitIndex);
				String paramValue = s.substring(splitIndex + 1);
				if (ZtkynStringUtil.isNotBlank(paramKey, paramValue)) {
					paramsMap.put(paramKey, paramValue);
				}
			});
			urlContent.setParamsMap(paramsMap);
		}
	}

	public static void main(String[] args) {
		String url = "https://mp.weixin.qq.com/mp/appmsgalbum?__biz=Mzg2OTA0Njk0OA==&action=getalbum&album_id=1352302538565189634&scene=173&from_msgid=2247533882&from_itemidx=1&count=3&nolastread=1#wechat_redirect";
		System.out.println(parseUrl(url).getParamsMap());
	}

	@Data
	@Accessors(chain = true)
	static class UrlContent {

		private URL url;

		private String baseUrl;

		private Map<String, Object> paramsMap;

	}

}
