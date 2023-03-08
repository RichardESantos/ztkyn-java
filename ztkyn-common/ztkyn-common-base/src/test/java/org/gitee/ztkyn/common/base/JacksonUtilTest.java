package org.gitee.ztkyn.common.base;

import java.util.List;

import org.gitee.ztkyn.common.base.collection.ZtkynListUtil;
import org.junit.jupiter.api.Test;

/**
 * @author whty
 * @version 1.0
 * @description
 * @date 2023/3/8 13:42
 */
class JacksonUtilTest {

	@Test
	void json2DeepMapExample() {
		JacksonUtil.json2DeepMapExample();
	}

	@Test
	void testEclipseCollection() {
		List<JacksonExampleDomain> stringList = ZtkynListUtil.createFastList();
		for (int i = 0; i < 10; i++) {
			stringList.add(new JacksonExampleDomain().setName("data" + i));
		}
		String json = JacksonUtil.obj2String(stringList);
		System.out.println(json);
		List<JacksonExampleDomain> target = JacksonUtil.json2List(json, JacksonExampleDomain.class);
		System.out.println(target);
	}

}