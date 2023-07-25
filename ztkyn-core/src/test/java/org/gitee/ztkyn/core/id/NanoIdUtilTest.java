package org.gitee.ztkyn.core.id;

import java.util.StringJoiner;

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
		int i = 0;
		for (int j = 0; j < 100; j++) {
			StringJoiner stringJoiner = new StringJoiner("\n");
			// System.out.println(number2String(j,2));
			stringJoiner.add("git add 29990eb.xtmp." + number2String(j, 2) + "*")
				.add("git commit -m " + j)
				.add("git pull")
				.add("git push")
				// 等待时间 11S
				.add("ping -n 11 127.0.0.1 > nul");
			System.out.println(stringJoiner);
		}
	}

	public String number2String(int i, int len) {
		switch (len) {
			case 2:
				if (i < 10) {
					return "0" + i;
				}
				else
					return String.valueOf(i);
			default:
				return String.valueOf(i);
		}
	}

}