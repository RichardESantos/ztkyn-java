package org.gitee.ztkyn.common.base;

import java.security.SecureRandom;

import cn.hutool.core.lang.id.NanoId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author whty
 * @date 2023-04-19 11:38
 * @description ZtkynNanoIdUtil
 * @version 1.0.0
 */
public class ZtkynNanoIdUtil extends NanoId {

	private static final Logger logger = LoggerFactory.getLogger(ZtkynNanoIdUtil.class);

	private static final char[] alphabetForNumber = "0123456789".toCharArray();

	private static final SecureRandom secureRandom = new SecureRandom();

	private static final int LONG_SIZE = 18;

	public static Long nextLong() {
		return Long.valueOf(NanoId.randomNanoId(secureRandom, alphabetForNumber, LONG_SIZE));
	}

}
