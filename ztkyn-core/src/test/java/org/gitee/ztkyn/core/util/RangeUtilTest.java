package org.gitee.ztkyn.core.util;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

class RangeUtilTest {

	@Test
	void rangeInt() {

		System.out.println(Arrays.toString(RangeUtil.rangeInt(1, 10)));
	}

	@Test
	void testRangeInt() {
		System.out.println(Arrays.toString(RangeUtil.rangeInt(1, 100, 3)));
	}

}