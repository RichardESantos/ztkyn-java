package org.gitee.ztkyn.web.exception;

import org.gitee.ztkyn.core.exception.DataVerifyFailException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 请求被拒绝
 */
public class RequestDeniedException extends RuntimeException {

	private static final Logger logger = LoggerFactory.getLogger(DataVerifyFailException.class);

	public static final RequestDeniedException PARAM_VERIFY_FAIL = new RequestDeniedException("参数校验失败");

	public RequestDeniedException(String message) {
		super(message);
	}

	public RequestDeniedException(String message, Throwable cause) {
		super(message, cause);
	}

	public RequestDeniedException(Throwable cause) {
		super(cause);
	}

	public static RequestDeniedException of(Throwable cause) {
		return new RequestDeniedException("请求被拒绝", cause);
	}

}
