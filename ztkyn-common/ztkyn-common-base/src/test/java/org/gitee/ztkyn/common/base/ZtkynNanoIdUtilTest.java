package org.gitee.ztkyn.common.base;

import org.junit.jupiter.api.Test;

class ZtkynNanoIdUtilTest {

	@Test
	void nextLong() {
		for (int i = 0; i < 100; i++) {
			System.out.println(System.currentTimeMillis());
			System.out.println(ZtkynNanoIdUtil.nextLong());
		}
	}

}