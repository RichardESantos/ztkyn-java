package org.gitee.ztkyn.core.util;

import java.security.SecureRandom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author richard
 * @date 2023-06-20 19:08
 * @description RandomUtil
 * @version 1.0.0
 */
public class RandomUtil {

	private static final Logger logger = LoggerFactory.getLogger(RandomUtil.class);

	private static final SecureRandom DEFAULT_RANDOM = new SecureRandom();

	public static int randomInt(int from, int end) {
		return DEFAULT_RANDOM.nextInt(from, end);
	}

}
