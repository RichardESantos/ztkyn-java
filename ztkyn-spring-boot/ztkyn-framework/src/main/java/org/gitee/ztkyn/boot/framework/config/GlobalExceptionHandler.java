package org.gitee.ztkyn.boot.framework.config;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.validation.ConstraintViolationException;
import org.gitee.ztkyn.boot.framework.domain.ResponseResult;
import org.gitee.ztkyn.boot.framework.exception.ZtkynException;
import org.gitee.ztkyn.core.string.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author richard
 * @version 1.0
 */
@RestControllerAdvice(annotations = { RestController.class, Controller.class })
public class GlobalExceptionHandler {

	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	/**
	 * 捕获 {@code BusinessException} 异常
	 */
	@ExceptionHandler({ ZtkynException.class })
	public ResponseResult<?> handleBusinessException(ZtkynException ex) {
		logger.error(ex.getMessage(), ex.getCause());
		return ResponseResult.failed(ex.getMessage());
	}

	/**
	 * {@code @RequestBody} 参数校验不通过时抛出的异常处理
	 */
	@ExceptionHandler({ MethodArgumentNotValidException.class })
	public ResponseResult<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
		BindingResult bindingResult = ex.getBindingResult();
		StringBuilder sb = new StringBuilder("校验失败:");
		for (FieldError fieldError : bindingResult.getFieldErrors()) {
			sb.append(fieldError.getField()).append("：").append(fieldError.getDefaultMessage()).append(", ");
		}
		String msg = sb.toString();
		if (StringUtil.isNotBlank(msg)) {
			return ResponseResult.failed(ResponseResult.ResultEnum.VALIDATE_FAILED, msg);
		}
		return ResponseResult.failed(ResponseResult.ResultEnum.VALIDATE_FAILED);
	}

	/**
	 * {@code @PathVariable} 和 {@code @RequestParam} 参数校验不通过时抛出的异常处理
	 */
	@ExceptionHandler({ ConstraintViolationException.class })
	public ResponseResult<?> handleConstraintViolationException(ConstraintViolationException ex) {
		if (StringUtil.isNotBlank(ex.getMessage())) {
			return ResponseResult.failed(ResponseResult.ResultEnum.VALIDATE_FAILED, ex.getMessage());
		}
		return ResponseResult.failed(ResponseResult.ResultEnum.VALIDATE_FAILED);
	}

	/**
	 * 顶级异常捕获并统一处理，当其他异常无法处理时候选择使用
	 */
	@ExceptionHandler({ Exception.class })
	public ResponseResult<?> handle(Exception ex) {
		logger.error("服务器异常", ex);
		return ResponseResult.failed(ex.getMessage());
	}

}
