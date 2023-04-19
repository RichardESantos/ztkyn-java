package org.gitee.ztkyn.core.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author whty
 * @version 1.0
 * @description 文件操作异常
 * @date 2023/2/21 10:52
 */
public class FileOperationException extends RuntimeException {

	private static final Logger logger = LoggerFactory.getLogger(FileOperationException.class);

	public FileOperationException(String message) {
		super(message);
	}

	public FileOperationException(String message, Throwable cause) {
		super(message, cause);
	}

	public FileOperationException(Throwable cause) {
		super(cause);
	}

}
