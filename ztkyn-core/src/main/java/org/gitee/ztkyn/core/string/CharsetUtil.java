package org.gitee.ztkyn.core.string;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author richard
 * @version 1.0
 * @description
 * @date 2023/2/20 14:22
 */
public class CharsetUtil {

	private static final Logger logger = LoggerFactory.getLogger(CharsetUtil.class);

	/**
	 * 系统默认编码，与OS相关
	 */
	public static final Charset defaultCharset = Charset.defaultCharset();

	public static final Charset UTF8 = StandardCharsets.UTF_8;

	public static final Charset ISO_8859_1 = StandardCharsets.ISO_8859_1;

	public static final Charset GBK = Charset.forName("GBK");

	public static final Charset GB2312 = Charset.forName("GB2312");

	public static final Charset GB18030 = Charset.forName("GB18030");

}
