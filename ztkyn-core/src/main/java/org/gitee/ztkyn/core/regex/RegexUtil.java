package org.gitee.ztkyn.core.regex;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author whty
 * @version 1.0
 * @description
 * @date 2023/1/31 13:48
 */
public class RegexUtil {

	private static final Logger logger = LoggerFactory.getLogger(RegexUtil.class);

	/**
	 * 字符串是否完全匹配
	 * @param regex
	 * @param charSequence
	 * @return
	 */
	public static boolean isMatch(String regex, CharSequence charSequence) {
		return isMatch(Pattern.compile(regex), charSequence);
	}

	/**
	 * 字符串是否完全匹配
	 * @param pattern
	 * @param charSequence
	 * @return
	 */
	public static boolean isMatch(Pattern pattern, CharSequence charSequence) {
		return pattern.matcher(charSequence).matches();
	}

	/**
	 * 获取所有匹配到内容
	 * @param regex
	 * @param charSequence
	 * @return
	 */
	public static List<String> findAll(String regex, CharSequence charSequence) {
		return findAll(Pattern.compile(regex), charSequence);
	}

	/**
	 * 获取所有匹配到内容
	 * @param pattern
	 * @param charSequence
	 * @return
	 */
	public static List<String> findAll(Pattern pattern, CharSequence charSequence) {
		List<String> result = new ArrayList<>();
		Matcher matcher = pattern.matcher(charSequence);
		while (matcher.find()) {
			result.add(matcher.group());
		}
		return result;
	}

	/**
	 * 替换全部匹配到的内容
	 *
	 * 替换字符串 replacement 中的反斜杠（ \ ）和美元符号（ $ ）可能会导致结果与被视为一般替换字符串时的结果不同; 见Matcher.replaceAll
	 * 。 如果需要，使用Matcher.quoteReplacement(java.lang.String)来抑制这些字符的特殊含义。
	 * @param regex
	 * @param charSequence
	 * @param replacement
	 * @return
	 */
	public static String replaceAll(String regex, CharSequence charSequence, String replacement) {
		return replaceAll(Pattern.compile(regex), charSequence, replacement);
	}

	/**
	 * 替换全部匹配到的内容
	 *
	 * 替换字符串 replacement 中的反斜杠（ \ ）和美元符号（ $ ）可能会导致结果与被视为一般替换字符串时的结果不同; 见Matcher.replaceAll
	 * 。 如果需要，使用Matcher.quoteReplacement(java.lang.String)来抑制这些字符的特殊含义。
	 * @param pattern
	 * @param charSequence
	 * @param replacement
	 * @return
	 */
	public static String replaceAll(Pattern pattern, CharSequence charSequence, String replacement) {
		return pattern.matcher(charSequence).replaceAll(replacement);
	}

	/**
	 * 替换匹配到的第一个内容
	 *
	 * 替换字符串 replacement 中的反斜杠（ \ ）和美元符号（ $ ）可能会导致结果与被视为一般替换字符串时的结果不同; 见Matcher.replaceAll
	 * 。 如果需要，使用Matcher.quoteReplacement(java.lang.String)来抑制这些字符的特殊含义。
	 * @param regex
	 * @param charSequence
	 * @param replacement
	 * @return
	 */
	public static String replaceFirst(String regex, CharSequence charSequence, String replacement) {
		return replaceFirst(Pattern.compile(regex), charSequence, replacement);
	}

	/**
	 * 替换匹配到的第一个内容
	 *
	 * 替换字符串 replacement 中的反斜杠（ \ ）和美元符号（ $ ）可能会导致结果与被视为一般替换字符串时的结果不同; 见Matcher.replaceAll
	 * 。 如果需要，使用Matcher.quoteReplacement(java.lang.String)来抑制这些字符的特殊含义。
	 * @param pattern
	 * @param charSequence
	 * @param replacement
	 * @return
	 */
	public static String replaceFirst(Pattern pattern, CharSequence charSequence, String replacement) {
		return pattern.matcher(charSequence).replaceFirst(replacement);
	}

}
