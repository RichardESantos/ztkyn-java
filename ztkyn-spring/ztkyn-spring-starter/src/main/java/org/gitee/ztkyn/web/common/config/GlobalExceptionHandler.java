package org.gitee.ztkyn.web.common.config;

import jakarta.validation.ConstraintViolationException;
import org.gitee.ztkyn.common.base.ZtkynStringUtil;
import org.gitee.ztkyn.web.common.domain.ResponseResult;
import org.gitee.ztkyn.web.common.domain.ResponseResult.ResultEnum;
import org.gitee.ztkyn.web.common.exception.BusinessException;
import org.gitee.ztkyn.web.common.exception.ForbiddenException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author whty
 * @version 1.0
 * @description
 * @date 2023/1/29 13:59
 */
@RestControllerAdvice(annotations = { RestController.class, Controller.class })
public class GlobalExceptionHandler {

	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	/**
	 * 捕获 {@code BusinessException} 异常
	 */
	@ExceptionHandler({ BusinessException.class })
	public ResponseResult<?> handleBusinessException(BusinessException ex) {
		logger.error("业务异常", ex);
		return ResponseResult.failed(ex.getMessage());
	}

	/**
	 * 捕获 {@code ForbiddenException} 异常
	 */
	@ExceptionHandler({ ForbiddenException.class })
	public ResponseResult<?> handleForbiddenException(ForbiddenException ex) {
		return ResponseResult.failed(ResultEnum.FORBIDDEN);
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
		if (ZtkynStringUtil.isNotBlank(msg)) {
			return ResponseResult.failed(ResultEnum.VALIDATE_FAILED, msg);
		}
		return ResponseResult.failed(ResultEnum.VALIDATE_FAILED);
	}

	/**
	 * {@code @PathVariable} 和 {@code @RequestParam} 参数校验不通过时抛出的异常处理
	 */
	@ExceptionHandler({ ConstraintViolationException.class })
	public ResponseResult<?> handleConstraintViolationException(ConstraintViolationException ex) {
		if (ZtkynStringUtil.isNotBlank(ex.getMessage())) {
			return ResponseResult.failed(ResultEnum.VALIDATE_FAILED, ex.getMessage());
		}
		return ResponseResult.failed(ResultEnum.VALIDATE_FAILED);
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
