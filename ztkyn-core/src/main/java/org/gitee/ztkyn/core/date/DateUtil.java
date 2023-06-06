package org.gitee.ztkyn.core.date;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author richard
 * @version 1.0.0
 * @date 2023-04-06 11:10
 * @description 时间处理工具
 */
public class DateUtil {

	private static final Logger logger = LoggerFactory.getLogger(DateUtil.class);

	private static final ZoneId zone = ZoneId.systemDefault();

	public static final Locale locale = Locale.getDefault();

	public static Date toDate(LocalDateTime localDateTime) {
		return Date.from(localDateTime.atZone(zone).toInstant());
	}

	public static Date toDate(LocalDate localDate) {
		return Date.from(localDate.atStartOfDay().atZone(zone).toInstant());
	}

	public static Date toDate(String dateStr, String dateFormatter) {
		return toDate(LocalDate.parse(dateStr, DateTimeFormatter.ofPattern(dateFormatter, locale)));
	}

	public static Date toDate(long timestamp) {
		return new Date(timestamp);
	}

	public static LocalDateTime toLocalDateTime(Date date) {
		return date.toInstant().atZone(zone).toLocalDateTime();
	}

	public static LocalDateTime toLocalDateTime(String dateStr, String dateFormatter) {
		return LocalDateTime.parse(dateStr, DateTimeFormatter.ofPattern(dateFormatter, locale));
	}

	public static LocalDateTime toLocalDateTime(long timestamp) {
		return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), zone);
	}

	public static LocalDate toLocalDate(Date date) {
		return date.toInstant().atZone(zone).toLocalDate();
	}

	public static LocalDate toLocalDate(String dateStr, String dateFormatter) {
		return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern(dateFormatter, locale));
	}

	public static LocalDate toLocalDate(long timestamp) {
		return Instant.ofEpochMilli(timestamp).atZone(zone).toLocalDate();
	}

	public static String formatDate(Date date, String dateFormatter) {
		return format(toLocalDate(date), dateFormatter);
	}

	public static String formatDateTime(Date date, String dateFormatter) {
		return format(toLocalDateTime(date), dateFormatter);
	}

	public static String format(LocalDate localDate, String dateFormatter) {
		return localDate.format(DateTimeFormatter.ofPattern(dateFormatter, locale));
	}

	public static String format(LocalDateTime localDateTime, String dateFormatter) {
		return localDateTime.format(DateTimeFormatter.ofPattern(dateFormatter, locale));
	}

	public static String formatDate(long timestamp, String dateFormatter) {
		return format(toLocalDate(timestamp), dateFormatter);
	}

	public static String formatDateTime(long timestamp, String dateFormatter) {
		return format(toLocalDateTime(timestamp), dateFormatter);
	}

	public static long getTime(Date date) {
		return date.getTime();
	}

	public static long getTime(LocalDate localDate) {
		return Timestamp.valueOf(localDate.atStartOfDay()).getTime();
	}

	public static long getTime(LocalDateTime localDateTime) {
		return Timestamp.valueOf(localDateTime).getTime();
	}

	public static long getTimeFromDateStr(String dateStr, String dateFormatter) {
		return getTime(toLocalDate(dateStr, dateFormatter));
	}

	public static long getTimeFromDateTimeStr(String dateStr, String dateFormatter) {
		return getTime(toLocalDateTime(dateStr, dateFormatter));
	}

}
