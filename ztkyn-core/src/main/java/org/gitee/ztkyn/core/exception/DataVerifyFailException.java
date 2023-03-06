package org.gitee.ztkyn.core.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author whty
 * @version 1.0
 * @description 数据校验失败 exception
 * @date 2023/3/6 16:43
 */
public class DataVerifyFailException extends RuntimeException {

	private static final Logger logger = LoggerFactory.getLogger(DataVerifyFailException.class);

	public DataVerifyFailException(String message) {
		super(message);
	}

	public DataVerifyFailException(String message, Throwable cause) {
		super(message, cause);
	}

	public DataVerifyFailException(Throwable cause) {
		super(cause);
	}

	public static DataVerifyFailException nullException = new DataVerifyFailException("数据为null");

	public static DataVerifyFailException blankException = new DataVerifyFailException("数据为空");

}
