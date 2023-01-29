package org.gitee.ztkyn.web.common.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author whty
 * @version 1.0
 * @description
 * @date 2023/1/18 15:22
 */
public class ForbiddenException extends RuntimeException {

	private static final Logger logger = LoggerFactory.getLogger(ForbiddenException.class);

	public ForbiddenException(String message) {
		super(message);
	}

}
