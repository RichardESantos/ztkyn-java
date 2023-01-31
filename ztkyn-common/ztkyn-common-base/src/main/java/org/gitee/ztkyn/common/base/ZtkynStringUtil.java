package org.gitee.ztkyn.common.base;

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
	 * 常见分隔符 :
	 */
	public static final String colonSeparator = StringUtil.colonSeparator;

	public static boolean isNotBlank(final CharSequence charSequence) {
		return StringUtil.isNotBlank(charSequence);
	}

	public static boolean isBlank(final CharSequence charSequence) {
		return StringUtil.isBlank(charSequence);
	}

}
