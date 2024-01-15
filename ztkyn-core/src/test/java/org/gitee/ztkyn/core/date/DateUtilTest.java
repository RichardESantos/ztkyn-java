package org.gitee.ztkyn.core.date;

import lombok.extern.slf4j.Slf4j;
import org.gitee.ztkyn.core.colleciton.ECollectionUtil;
import org.junit.jupiter.api.Test;

import java.security.SecureRandom;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.SortedSet;

@Slf4j
class DateUtilTest {

	@Test
	void parseDate() {
		try {
			System.out.println(DateUtil.parseDate("null"));
		}
		catch (Exception e) {
			log.error("解析失败", e);
		}
	}

}