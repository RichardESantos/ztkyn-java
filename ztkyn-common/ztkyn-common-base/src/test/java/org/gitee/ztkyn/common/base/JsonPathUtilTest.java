package org.gitee.ztkyn.common.base;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.gitee.ztkyn.common.base.collection.ECollectionUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JsonPathUtilTest {

	private String json;

	@BeforeEach
	void creatJson() {
		List<UserCol> fastList = ECollectionUtil.createFastList();
		for (int i = 0; i < 10; i++) {
			List<User> userList = ECollectionUtil.createFastList();
			for (int j = 0; j < 10; j++) {
				userList.add(new User().setName("name" + i + "_" + j).setNickName("nickName" + i + "_" + j));
			}
			fastList.add(new UserCol().setUserList(userList));
		}
		json = JacksonUtil.obj2StringPretty(fastList);
	}

	@Test
	void findNodeListValue() {
		List<String> value = JsonPathUtil.findNodeListValue(json, "$..name", String.class);
		System.out.println(value);
	}

	@Test
	void findNodeValue() {
	}

	@Test
	void updateNodeValue() {
	}

	@Getter
	@Setter
	@Accessors(chain = true)
	static class User {

		private String name;

		private String nickName;

	}

	@Getter
	@Setter
	@Accessors(chain = true)
	static class UserCol {

		private List<User> userList;

	}

}