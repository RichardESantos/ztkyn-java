package org.gitee.ztkyn.boot.framework.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author richard
 * @date 2023-06-21 08:56
 * @description ZtkynException
 * @version 1.0.0
 */
public abstract class ZtkynException extends RuntimeException {

	private static final Logger logger = LoggerFactory.getLogger(ZtkynException.class);

	public ZtkynException(String message) {
		super(message, null);
	}

	public ZtkynException(String message, Throwable cause) {
		super(message, cause);
	}

	public ZtkynException(Throwable cause) {
		super("系统错误", cause);
	}

}
