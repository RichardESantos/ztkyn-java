package org.gitee.ztkyn.web.common.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author whty
 * @version 1.0
 */
public class RequestLimitException extends RuntimeException {

	private static final Logger logger = LoggerFactory.getLogger(RequestLimitException.class);

	public RequestLimitException(String message) {
		super(message);
	}

}
