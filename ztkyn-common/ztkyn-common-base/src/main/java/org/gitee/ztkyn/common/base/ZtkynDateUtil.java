package org.gitee.ztkyn.common.base;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Calendar;
import java.util.Date;

import com.github.sisyphsu.dateparser.DateParserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author whty
 * @version 1.0
 * @description
 * @date 2023/2/8 10:26
 */
public class ZtkynDateUtil {

	private static final Logger logger = LoggerFactory.getLogger(ZtkynDateUtil.class);

	static {
		DateParserUtils.registerStandardRule("^(?<year>\\d{4})年(?<month>\\d{1,2})月$");
		DateParserUtils.registerStandardRule("(?<year>\\d{4})年$");
		DateParserUtils.registerCustomizedRule("民国(\\d{3})年", (input, matcher, dt) -> {
			int offset = matcher.start(1);
			int i0 = input.charAt(offset) - '0';
			int i1 = input.charAt(offset + 1) - '0';
			int i2 = input.charAt(offset + 2) - '0';
			dt.setYear(i0 * 100 + i1 * 10 + i2 + 1911);
		});
	}

	public static Date parseDate(String dateStr) {
		return DateParserUtils.parseDate(dateStr);
	}

	public static LocalDateTime parseDateTime(String dateStr) {
		return DateParserUtils.parseDateTime(dateStr);
	}

	public static Calendar parseCalendar(String dateStr) {
		return DateParserUtils.parseCalendar(dateStr);
	}

	public static OffsetDateTime parseOffsetDateTime(String dateStr) {
		return DateParserUtils.parseOffsetDateTime(dateStr);
	}

}
