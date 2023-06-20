package org.gitee.ztkyn.core.id;

import org.junit.jupiter.api.Test;

class NanoIdUtilTest {

	@Test
	void randomInteger() {
		System.out.println(Integer.MAX_VALUE);
		System.out.println(NanoIdUtil.randomInteger());
		int result = NanoIdUtil.randomInteger();
		while (result >= 0) {
			result = NanoIdUtil.randomInteger();
		}
		System.out.println(result);
	}

	@Test
	void randomLong() {
		System.out.println(Long.MAX_VALUE);
		System.out.println(NanoIdUtil.randomLong());
	}

}