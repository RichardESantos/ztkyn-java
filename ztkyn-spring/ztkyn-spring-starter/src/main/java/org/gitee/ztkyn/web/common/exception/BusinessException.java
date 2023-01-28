package org.gitee.ztkyn.web.common.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author whty
 * @version 1.0
 * @description
 * @date 2023/1/18 15:22
 */
public class BusinessException extends RuntimeException {
	private static final Logger logger = LoggerFactory.getLogger(BusinessException.class);

	public BusinessException(String message) {
		super(message);
	}
}
