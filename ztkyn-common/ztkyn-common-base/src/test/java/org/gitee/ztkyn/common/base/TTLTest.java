package org.gitee.ztkyn.common.base;

import java.util.ArrayList;
import java.util.List;

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

}
