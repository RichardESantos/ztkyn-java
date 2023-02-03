package org.gitee.ztkyn.common.base;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * transmittable-thread-local 测试类
 */
public class TTLTest {

	private static final Logger logger = LoggerFactory.getLogger(TTLTest.class);

	@Test
	public void testStream() {
		List<String> strings = new ArrayList<>();

		strings.parallelStream().forEach(s -> {
		});
	}

	@Test
	public void testToMap() {
		List<MapTest> mapTests = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			mapTests.add(new MapTest("key" + i));
		}
		Map<String, String> stringMap = mapTests.stream().collect(Collectors.toMap(MapTest::getKey, MapTest::getValue));
	}

	class MapTest {

		private String key;

		private String value;

		public MapTest(String key) {
			this.key = key;
		}

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

	}

}
