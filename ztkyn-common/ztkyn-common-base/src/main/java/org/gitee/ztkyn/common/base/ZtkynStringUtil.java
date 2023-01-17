package org.gitee.ztkyn.common.base;

import com.google.common.base.Strings;
import org.gitee.ztkyn.core.string.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author whty
 * @version 1.0
 * @description 字符串工具类
 * @date 2023/1/16 9:38
 */
public class ZtkynStringUtil {
	private static final Logger logger = LoggerFactory.getLogger(ZtkynStringUtil.class);

	public static boolean isNotBlank(final CharSequence charSequence) {
		return StringUtil.isNotBlank(charSequence);
	}

	public static boolean isBlank(final CharSequence charSequence) {
		return StringUtil.isBlank(charSequence);
	}

}
