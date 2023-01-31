package org.gitee.ztkyn.common.base;

import java.util.List;
import java.util.regex.Pattern;

import org.gitee.ztkyn.core.regex.RegexUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author whty
 * @version 1.0
 * @description 正则表达式工具类
 * @date 2023/1/16 9:43
 */
public class ZtkynRegexUtil {

	private static final Logger logger = LoggerFactory.getLogger(ZtkynRegexUtil.class);

	public static boolean isMatch(String regex, CharSequence charSequence) {
		return RegexUtil.isMatch(regex, charSequence);
	}

	public static boolean isMatch(Pattern pattern, CharSequence charSequence) {
		return RegexUtil.isMatch(pattern, charSequence);
	}

	public static List<String> findAll(String regex, CharSequence charSequence) {
		return RegexUtil.findAll(regex, charSequence);
	}

	public static List<String> findAll(Pattern pattern, CharSequence charSequence) {
		return RegexUtil.findAll(pattern, charSequence);
	}

	/**
	 * 替换全部匹配到的内容
	 * @param regex
	 * @param charSequence
	 * @param replacement
	 * @return
	 */
	public static String replaceAll(String regex, CharSequence charSequence, String replacement) {
		return RegexUtil.replaceAll(regex, charSequence, replacement);
	}

	/**
	 * 替换全部匹配到的内容
	 * @param pattern
	 * @param charSequence
	 * @param replacement
	 * @return
	 */
	public static String replaceAll(Pattern pattern, CharSequence charSequence, String replacement) {
		return RegexUtil.replaceAll(pattern, charSequence, replacement);
	}

	/**
	 * 替换匹配到的第一个内容
	 * @param regex
	 * @param charSequence
	 * @param replacement
	 * @return
	 */
	public static String replaceFirst(String regex, CharSequence charSequence, String replacement) {
		return RegexUtil.replaceFirst(regex, charSequence, replacement);
	}

	/**
	 * 替换匹配到的第一个内容
	 * @param pattern
	 * @param charSequence
	 * @param replacement
	 * @return
	 */
	public static String replaceFirst(Pattern pattern, CharSequence charSequence, String replacement) {
		return RegexUtil.replaceFirst(pattern, charSequence, replacement);
	}

}
