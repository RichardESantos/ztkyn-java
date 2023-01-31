package org.gitee.ztkyn.core.string;

import java.io.File;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author whty
 * @version 1.0
 */
public class StringUtil {

	private static final Logger logger = LoggerFactory.getLogger(StringUtil.class);

	private static final Pattern humpPattern = Pattern.compile("[A-Z]");

	/**
	 * 默认文件路径分隔符
	 */
	public static final String fileSeparator = File.separator;

	/**
	 * 默认文件路径分隔符（On UNIX systems, this character is ':'; on Microsoft Windows systems it
	 * is ';'.）
	 */
	public static final String pathSeparator = File.pathSeparator;

	/**
	 * 常见分隔符 :
	 */
	public static final String colonSeparator = ":";

	/**
	 * 驼峰转下划线,最后转为大写
	 * @param str
	 * @return
	 */
	public static String humpToLine(String str) {
		Matcher matcher = humpPattern.matcher(str);
		StringBuffer sb = new StringBuffer();
		while (matcher.find()) {
			matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
		}
		matcher.appendTail(sb);
		return sb.toString().toLowerCase();
	}

	/**
	 * 字符串拼接
	 * @param args
	 * @return
	 */
	public static String concat(CharSequence splitChar, String... args) {
		StringJoiner joiner = new StringJoiner(splitChar);
		for (String arg : args) {
			joiner.add(arg);
		}
		return joiner.toString();
	}

	/**
	 * 去掉字符串中的空格
	 * @param charSequence
	 * @return
	 */
	public static String strim(CharSequence charSequence) {
		StringBuilder buffer = new StringBuilder();
		for (int i = 0; i < charSequence.length(); i++) {
			char c = charSequence.charAt(i);
			if (!Character.isWhitespace(c)) {
				buffer.append(c);
			}
		}
		return buffer.toString();
	}

	/**
	 * 数字拼接成字符串
	 * @param splitChar
	 * @param args
	 * @return
	 */
	public static String concat(CharSequence splitChar, Integer... args) {
		StringJoiner joiner = new StringJoiner(splitChar);
		for (Integer arg : args) {
			joiner.add(String.valueOf(arg));
		}
		return joiner.toString();
	}

	/**
	 * 字符串 不能为空
	 * <p>
	 * Checks if a CharSequence is not empty (""), not null and not whitespace only.
	 * </p>
	 * <p>
	 * Whitespace is defined by {@link Character#isWhitespace(char)}.
	 * </p>
	 * <pre>
	 * StringUtils.isNotBlank(null)      = false
	 * StringUtils.isNotBlank("")        = false
	 * StringUtils.isNotBlank(" ")       = false
	 * StringUtils.isNotBlank("bob")     = true
	 * StringUtils.isNotBlank("  bob  ") = true
	 * </pre>
	 * @param cs
	 * @return
	 */
	public static boolean isNotBlank(final CharSequence cs) {
		return !isBlank(cs);
	}

	/**
	 * 复制自 org.apache.commons.lang3.StringUtils#isBlank(java.lang.CharSequence)
	 * <p>
	 * Checks if a CharSequence is empty (""), null or whitespace only.
	 * </p>
	 * <p>
	 * Whitespace is defined by {@link Character#isWhitespace(char)}.
	 * </p>
	 * <pre>
	 * StringUtils.isBlank(null)      = true
	 * StringUtils.isBlank("")        = true
	 * StringUtils.isBlank(" ")       = true
	 * StringUtils.isBlank("bob")     = false
	 * StringUtils.isBlank("  bob  ") = false
	 * </pre>
	 * @param cs the CharSequence to check, may be null
	 * @return {@code true} if the CharSequence is null, empty or whitespace only
	 * @since 2.0
	 * @since 3.0 Changed signature from isBlank(String) to isBlank(CharSequence)
	 */
	public static boolean isBlank(final CharSequence cs) {
		final int strLen = length(cs);
		if (strLen == 0) {
			return true;
		}
		for (int i = 0; i < strLen; i++) {
			if (!Character.isWhitespace(cs.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 复制自 org.apache.commons.lang3.StringUtils#length(java.lang.CharSequence)
	 * @param cs
	 * @return
	 */
	public static int length(final CharSequence cs) {
		return cs == null ? 0 : cs.length();
	}

}
