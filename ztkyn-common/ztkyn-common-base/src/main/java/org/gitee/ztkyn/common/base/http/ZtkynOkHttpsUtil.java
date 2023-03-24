package org.gitee.ztkyn.common.base.http;

import java.util.Collections;
import java.util.Map;

import cn.zhxu.okhttps.OkHttps;
import cn.zhxu.okhttps.SHttpTask;
import org.gitee.ztkyn.core.function.DataProcessHandler;
import org.gitee.ztkyn.core.function.PredicateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author whty
 * @version 1.0
 * @description
 * @date 2023/3/23 11:14
 */
public class ZtkynOkHttpsUtil {

	private static final Logger logger = LoggerFactory.getLogger(ZtkynOkHttpsUtil.class);

	public static String get(String url) {
		return get(url, Collections.emptyMap(), Collections.emptyMap());
	}

	public static String get(String url, Map<String, Object> paramsMap, Map<String, String> headerMap) {
		SHttpTask httpTask = OkHttps.sync(url);
		DataProcessHandler.of(paramsMap, PredicateUtil.mapNotBlank()).ifTrue(httpTask::setBodyPara);
		DataProcessHandler.of(headerMap, PredicateUtil.mapNotBlank()).ifTrue(httpTask::addHeader);
		return httpTask.get().getBody().toString();
	}

	public static String post(String url, Map<String, Object> paramsMap) {
		return post(url, paramsMap, Collections.emptyMap());
	}

	public static String post(String url, Map<String, Object> paramsMap, Map<String, String> headerMap) {
		SHttpTask httpTask = OkHttps.sync(url);
		DataProcessHandler.of(paramsMap, PredicateUtil.mapNotBlank()).ifTrue(httpTask::setBodyPara)
				.ifFalse(() -> httpTask.setBodyPara(Collections.emptyMap()));
		DataProcessHandler.of(headerMap, PredicateUtil.mapNotBlank()).ifTrue(httpTask::addHeader);
		return httpTask.post().getBody().toString();
	}

	public static String postJson(String url, Map<String, Object> paramsMap) {
		return postJson(url, paramsMap, Collections.emptyMap());
	}

	public static String postJson(String url, Map<String, Object> paramsMap, Map<String, String> headerMap) {
		SHttpTask httpTask = OkHttps.sync(url).bodyType(OkHttps.JSON);
		DataProcessHandler.of(paramsMap, PredicateUtil.mapNotBlank()).ifTrue(httpTask::setBodyPara)
				.ifFalse(() -> httpTask.setBodyPara(Collections.emptyMap()));
		DataProcessHandler.of(headerMap, PredicateUtil.mapNotBlank()).ifTrue(httpTask::addHeader);
		return httpTask.post().getBody().toString();
	}

}
