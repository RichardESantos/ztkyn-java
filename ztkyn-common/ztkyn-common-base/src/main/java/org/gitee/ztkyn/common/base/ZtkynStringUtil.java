package org.gitee.ztkyn.common.base;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

import org.gitee.ztkyn.common.base.collection.ZtkynListUtil;
import org.gitee.ztkyn.core.string.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author whty
 * @version 1.0
 */
public class ZtkynStringUtil {

	private static final Logger logger = LoggerFactory.getLogger(ZtkynStringUtil.class);

	/**
	 * 默认文件路径分隔符
	 */
	public static final String fileSeparator = StringUtil.fileSeparator;

	/**
	 * 默认文件路径分隔符（On UNIX systems, this character is ':'; on Microsoft Windows systems it
	 * is ';'.）
	 */
	public static final String pathSeparator = StringUtil.pathSeparator;

	/**
	 * 空字符串
	 */
	public static final String emptyStr = StringUtil.emptyStr;

	/**
	 * 空内容字符串
	 */
	public static final String emptyContentStr = StringUtil.emptyContentStr;

	/**
	 * TAB 制表符
	 */
	public static final String tabStr = StringUtil.tabStr;

	/**
	 * 冒号（:）：colon
	 */
	public static final String colonSeparator = StringUtil.colonSeparator;

	/**
	 * 分号（;）：semicolon
	 */
	public static final String semicolonSeparator = StringUtil.semicolonSeparator;

	/**
	 * 逗号（,）：comma
	 */
	public static final String commaSeparator = StringUtil.commaSeparator;

	/**
	 * 句号（.）：full stop/full point/period
	 */
	public static final String periodSeparator = StringUtil.periodSeparator;

	/**
	 * 长破折号（—）：dash
	 */
	public static final String dashSeparator = StringUtil.dashSeparator;

	/**
	 * 短破折号（-）：hyphen
	 */
	public static final String hyphenSeparator = StringUtil.hyphenSeparator;

	/**
	 * 省略号（…）：ellipsis
	 */
	public static final String ellipsisSeparator = StringUtil.ellipsisSeparator;

	/**
	 * 斜线（/）：slash
	 */
	public static final String slashSeparator = StringUtil.slashSeparator;

	/**
	 * 下划线（_）：underscore。
	 */
	public static final String underscoreSeparator = StringUtil.underscoreSeparator;

	public static boolean isNotBlank(final CharSequence charSequence) {
		return StringUtil.isNotBlank(charSequence);
	}

	public static boolean isBlank(final CharSequence charSequence) {
		return StringUtil.isBlank(charSequence);
	}

	/**
	 * 多个参数不能为空
	 * @param charSequence
	 * @param others
	 * @return
	 */
	public static boolean isNotBlank(final CharSequence charSequence, final CharSequence... others) {
		return isNotBlank(charSequence)
				&& (Objects.nonNull(others) && Arrays.stream(others).allMatch(ZtkynStringUtil::isNotBlank));
	}

	/**
	 * 字符串拼接工具
	 * @param splitChar
	 * @param args
	 * @return
	 */
	public static String concat(CharSequence splitChar, Object... args) {
		StringJoiner joiner = new StringJoiner(splitChar);
		for (Object arg : args) {
			joiner.add(String.valueOf(arg));
		}
		return joiner.toString();
	}

	/**
	 * 切割字符串，允许结果中包含空字符串
	 * @param charSequence
	 * @param splitChar
	 * @return
	 */
	public static List<String> splitToList(CharSequence charSequence, char splitChar) {
		return splitToList(charSequence, splitChar, false);
	}

	/**
	 * 切割字符串
	 * @param charSequence
	 * @param splitChar
	 * @param excludeEmpty true，不包含空字符串，false，包含空字符串
	 * @return
	 */
	public static List<String> splitToList(CharSequence charSequence, char splitChar, boolean excludeEmpty) {
		List<String> splitList = ZtkynListUtil.createFastList();
		int length = charSequence.length();
		StringBuilder buffer = new StringBuilder();
		for (int i = 0; i < length; i++) {
			char charAt = charSequence.charAt(i);
			// 匹配到了切割符号
			if (Objects.equals(charAt, splitChar)) {
				add(excludeEmpty, splitList, buffer.toString());
				buffer = new StringBuilder();
			}
			else {
				buffer.append(charAt);
			}
		}
		add(excludeEmpty, splitList, buffer.toString());
		return splitList;
	}

	private static void add(boolean excludeEmpty, List<String> splitList, String tmpStr) {
		if (isNotBlank(tmpStr)) {
			splitList.add(tmpStr);
		}
		else {
			if (!excludeEmpty) {
				splitList.add(emptyStr);
			}
		}
	}

}
