package org.gitee.ztkyn.common.base.http;

import java.util.Collections;

import org.junit.jupiter.api.Test;

/**
 * @author whty
 * @version 1.0
 * @description
 * @date 2023/3/23 11:36
 */
class ZtkynOkHttpsUtilTest {

	@Test
	void get() {
	}

	@Test
	void testGet() {
	}

	@Test
	void post() {
	}

	@Test
	void postJson() {
		String url = "http://localhost:8400/bluekg-graph/api/ontology/page/ontology";
		String json = ZtkynOkHttpsUtil.postJson(url, Collections.emptyMap());
		System.out.println(json);
	}

}