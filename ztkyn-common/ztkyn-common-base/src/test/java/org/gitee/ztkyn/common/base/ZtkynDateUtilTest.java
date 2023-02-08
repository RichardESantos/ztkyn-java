package org.gitee.ztkyn.common.base;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

/**
 * @author whty
 * @version 1.0
 * @description
 * @date 2023/2/8 10:40
 */
class ZtkynDateUtilTest {

	@Test
	void parseDate() {

		LocalDateTime localDateTime = ZtkynDateUtil.parseDateTime("2019年");
		System.out.println(localDateTime.getYear());
		LocalDateTime localDateTime1 = ZtkynDateUtil.parseDateTime("2019年10月");
		System.out.println(localDateTime1.getYear());
	}

}