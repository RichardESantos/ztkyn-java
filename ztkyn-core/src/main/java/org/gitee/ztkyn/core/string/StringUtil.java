package org.gitee.ztkyn.core.string;

import java.io.File;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author richard
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
	 * 空字符串
	 */
	public static final String emptyStr = "";

	/**
	 * 空内容字符串
	 */
	public static final String emptyContentStr = " ";

	/**
	 * TAB 制表符
	 */
	public static final String tabStr = "	";

	/**
	 * 冒号（:）：colon
	 */
	public static final String colonSeparator = ":";

	/**
	 * 分号（;）：semicolon
	 */
	public static final String semicolonSeparator = ";";

	/**
	 * 逗号（,）：comma
	 */
	public static final String commaSeparator = ",";

	/**
	 * 句号（.）：full stop/full point/period
	 */
	public static final String periodSeparator = ".";

	/**
	 * 长破折号（—）：dash
	 */
	public static final String dashSeparator = "—";

	/**
	 * 短破折号（-）：hyphen
	 */
	public static final String hyphenSeparator = "-";

	/**
	 * 省略号（…）：ellipsis
	 */
	public static final String ellipsisSeparator = "…";

	/**
	 * 斜线（/）：slash
	 */
	public static final String slashSeparator = "/";

	/**
	 * 下划线（_）：underscore。
	 */
	public static final String underscoreSeparator = "_";

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
	 * 去掉字符串中的空格
	 * @param charSequence
	 * @return
	 */
	public static String strip(CharSequence charSequence) {
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

	/**
	 * 将字符串转换成指定编码格式
	 * @param str
	 * @param charset
	 * @return
	 */
	public static String unicodeConvert(String str, Charset charset) {
		return new String(str.getBytes(charset), charset);

	}

	/**
	 * 字符串首字母大写
	 * @param cs
	 * @return
	 */
	public static String UpperFirst(final CharSequence cs) {
		final int strLen = length(cs);
		char c = cs.charAt(0);
		if (Character.isWhitespace(c))
			return cs.toString();
		else {
			return Character.toString(Character.toUpperCase(c)) + cs.subSequence(1, strLen);
		}
	}

	/**
	 * 字符串首字母小写
	 * @param cs
	 * @return
	 */
	public static String letterFirst(final CharSequence cs) {
		final int strLen = length(cs);
		if (strLen == 0)
			return cs.toString();
		char c = cs.charAt(0);
		if (Character.isWhitespace(c))
			return cs.toString();
		else {
			return Character.toString(Character.toLowerCase(c)) + cs.subSequence(1, strLen);
		}
	}

}
