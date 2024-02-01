package org.gitee.ztkyn.web.configuration;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.gitee.ztkyn.web.beans.R;
import org.gitee.ztkyn.web.constants.ResultCode;
import org.gitee.ztkyn.web.exception.RequestDeniedException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

/**
 * @author chenlei
 * @className GlobalExceptionHandler
 * @description 全局统一异常处理器 T/O/D/O 待梳理测试
 * @company CDFT
 * @date 2022-05-27 11:39
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

	private static final String ERROR_DETAIL = "error_detail";

	private static final String BAD_CREDENTIALS = "Bad credentials";

	private static final String BAD_CLIENT_CREDENTIALS = "Bad client credentials";

	private static final String USER_IS_DISABLED = "User is disabled";

	private static final String USER_ACCOUNT_IS_LOCKED = "User account is locked";

	// private static ThrowableAnalyzer throwableAnalyzer = new ThrowableAnalyzer();

	/**
	 * OAuth2异常处理器 无效令牌异常处理器
	 * @param ex
	 * @param request
	 * @param response
	 * @return
	 */
	// @ExceptionHandler({OAuth2Exception.class, InvalidTokenException.class})
	// public static R<String> oauth2Exception(Exception ex, HttpServletRequest request,
	// HttpServletResponse response) {
	// R<String> r = resolveException(ex, request.getRequestURI());
	// r.put(ERROR_DETAIL, r.getMessage());
	// response.setStatus(r.getHttpStatus());
	// return r;
	// }

	/**
	 * 除上面以外的其他异常
	 * @param ex
	 * @param request
	 * @param response
	 * @return R<String>
	 */
	@ExceptionHandler({ Exception.class })
	public static R<String> exception(Exception ex, HttpServletRequest request, HttpServletResponse response) {
		// Sentinel接收异常
		// Tracer.trace(ex);
		R<String> r = resolveException(ex, request.getRequestURI());
		response.setStatus(r.getHttpStatus());
		return r;
	}

	/**
	 * 解析各类异常
	 * @param e
	 * @param path
	 * @return R<String>
	 */
	public static R resolveException(Exception e, String path) {
		HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		int code = ResultCode.ERROR.getCode();
		String message = "";
		log.error("resolveException,ex = {}", e.getMessage(), e);
		StackTraceElement[] stackTrace = e.getStackTrace();
		// 返回堆栈异常集合(堆栈从上到下每个异常),根据具体情况进行扩充
		// TODO
		// 默认
		R body = buildBody(code, message, path, httpStatus);
		log.error("全局异常处理——————>{}", body, e);
		return body;
	}

	/**
	 * 返回异常结果Body
	 * @param code code
	 * @param msg msg
	 * @param path path
	 * @param httpStatus httpStatus
	 * @return R<String>
	 */
	private static R buildBody(int code, String msg, String path, HttpStatus httpStatus) {
		if (isJsonObject(msg)) {
			ObjectMapper objectMapper = new ObjectMapper();
			try {
				R t = objectMapper.readValue(msg, R.class);
				msg = t.getMessage();
			}
			catch (JsonProcessingException ex) {
				// 解析错误，默认ResultCode.ERROR
				msg = ResultCode.ERROR.getMessage();
			}
		}
		else {
			if (CharSequenceUtil.isBlank(msg)) {
				// 没有传入消息体，默认使用枚举定义的消息体，枚举没找到默认的是ResultCode.ERROR
				msg = ResultCode.getResultEnum(code).getMessage();
			}
		}
		return R.error().setCode(code).setMessage(msg).setPath(path).setHttpStatus(httpStatus.value());
	}

	/**
	 * 判断字符串是否可以解析为对象
	 * @param content 字符串(可转对象时取对象体中的message)
	 * @return Boolean
	 */
	private static boolean isJsonObject(String content) {
		// 此处应该注意，不要使用StringUtils.isEmpty(),因为当content为"
		// "空格字符串时，JSONObject.parseObject可以解析成功，
		// 实际上，这是没有什么意义的。所以content应该是非空白字符串且不为空，判断是否是JSON数组也是相同的情况。
		if (CharSequenceUtil.isBlank(content)) {
			return false;
		}
		try {
			JSONUtil.isTypeJSON(content);
			return true;
		}
		catch (Exception e) {
			return false;
		}
	}

	/**
	 * 返回异常结果Body 自定义
	 * @param e e
	 * @param code code
	 * @param path path
	 * @param httpStatus httpStatus
	 * @param message 自定义的message
	 * @return R<String>
	 */
	private static R buildBodyWithMessage(Exception e, int code, String path, HttpStatus httpStatus, String message) {
		R r = R.error()
			.setCode(code)
			.setMessage(CharSequenceUtil.isBlank(message) ? "系统错误" : message)
			.setPath(path)
			.setHttpStatus(httpStatus.value());
		log.error("全局异常处理 e——————>{}", r, e);
		return r;
	}

	/**
	 * 非法参数异常 由{@link org.springframework.util.Assert}抛出
	 * @param e
	 * @return R<String>
	 */
	@ExceptionHandler(RequestDeniedException.class)
	@ResponseStatus(HttpStatus.OK)
	public R handleIllegalArgumentException(RequestDeniedException e) {
		log.error("请求被拒绝,ex = {}", e.getMessage(), e);
		return buildBody(ResultCode.ACCESS_DENIED.getCode(), "", "", HttpStatus.OK);
	}

	/**
	 * 非法参数异常 由{@link org.springframework.util.Assert}抛出
	 * @param e
	 * @return R<String>
	 */
	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseStatus(HttpStatus.OK)
	public R handleIllegalArgumentException(IllegalArgumentException e) {
		log.error("非法参数,ex = {}", e.getMessage(), e);
		return buildBody(ResultCode.VALID_FAIL.getCode(), "", "", HttpStatus.OK);
	}

	/**
	 * 处理请求参数格式验证异常
	 * @param e
	 * @param request
	 * @return R<String>
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public R<String> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e,
			HttpServletRequest request) {
		String message = e.getBindingResult()
			.getAllErrors()
			.stream()
			.map(DefaultMessageSourceResolvable::getDefaultMessage)
			.collect(Collectors.joining(";"));
		R r = buildBodyWithMessage(e, ResultCode.VALID_FAIL.getCode(), request.getRequestURI(), HttpStatus.BAD_REQUEST,
				message);
		log.error("MethodArgumentNotValidException: {}", e.getMessage(), e);
		return r;
	}

	/**
	 * 处理Get请求中 使用@Valid 验证路径中请求实体校验失败后抛出的异常
	 * @param e
	 * @param request
	 * @return R<String>
	 */
	@ExceptionHandler(BindException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public R<String> bindExceptionHandler(BindException e, HttpServletRequest request) {
		String message = e.getBindingResult()
			.getAllErrors()
			.stream()
			.map(DefaultMessageSourceResolvable::getDefaultMessage)
			.collect(Collectors.joining());
		R<String> r = buildBodyWithMessage(e, ResultCode.VALID_FAIL.getCode(), request.getRequestURI(),
				HttpStatus.BAD_REQUEST, message);
		log.error("MethodArgumentNotValidException:{}", e.getMessage(), e);
		return r;
	}

	/**
	 * 请求参数格式错误 @RequestParam上validate失败后抛出的异常
	 * @param e
	 * @param request
	 * @return R<String>
	 */
	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public R<String> constraintViolationExceptionHandler(ConstraintViolationException e, HttpServletRequest request) {
		String message = e.getConstraintViolations()
			.stream()
			.map(ConstraintViolation::getMessage)
			.collect(Collectors.joining(";"));
		R<String> r = buildBodyWithMessage(e, ResultCode.VALID_FAIL.getCode(), request.getRequestURI(),
				HttpStatus.BAD_REQUEST, message);
		log.error("MethodArgumentNotValidException:{}", e.getMessage(), e);
		return r;
	}

	/**
	 * 参数格式异常
	 * @param e
	 * @return R<String>
	 */
	@ExceptionHandler(HttpMessageNotReadableException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public R<String> httpMessageNotReadableExceptionHandler(HttpMessageNotReadableException e) {
		log.error("参数格式异常,ex = {}", e.getMessage(), e);
		return buildBody(ResultCode.VALID_FAIL.getCode(), "", "", HttpStatus.BAD_REQUEST);
	}

}
