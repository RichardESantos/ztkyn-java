package org.gitee.ztkyn.web.common.domain;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author whty
 * @version 1.0
 * @description 统一返回结构
 * @date 2023/1/18 15:02
 */
@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseResult<T> implements Serializable {

	private static final Logger logger = LoggerFactory.getLogger(ResponseResult.class);

	/**
	 * 返回编码
	 */
	private Integer code;

	/**
	 * 编码描述
	 */
	private String msg;

	/**
	 * 业务数据
	 */
	private T data;

	public static <T> ResponseResult<T> success() {
		return new ResponseResult<>(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMessage(), null);
	}

	public static <T> ResponseResult<T> success(T data) {
		return new ResponseResult<>(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMessage(), data);
	}

	public static <T> ResponseResult<T> success(String message, T data) {
		return new ResponseResult<>(ResultEnum.SUCCESS.getCode(), message, data);
	}

	public static ResponseResult<?> failed() {
		return new ResponseResult<>(ResultEnum.COMMON_FAILED.getCode(), ResultEnum.COMMON_FAILED.getMessage(), null);
	}

	public static ResponseResult<?> failed(String message) {
		return new ResponseResult<>(ResultEnum.COMMON_FAILED.getCode(), message, null);
	}

	public static ResponseResult<?> failed(IResult errorResult) {
		return new ResponseResult<>(errorResult.getCode(), errorResult.getMessage(), null);
	}

	public static ResponseResult<?> failed(IResult errorResult, String message) {
		return new ResponseResult<>(errorResult.getCode(), message, null);
	}

	public static <T> ResponseResult<T> instance(Integer code, String message, T data) {
		return new ResponseResult<>(code, message, data);
	}

	// 定义返回数据结构
	public interface IResult {

		Integer getCode();

		String getMessage();

	}

	/**
	 * 常用结果的枚举
	 */
	public enum ResultEnum implements IResult {

		SUCCESS(2001, "接口调用成功"), VALIDATE_FAILED(2002, "参数校验失败"), COMMON_FAILED(2003, "接口调用失败"), FORBIDDEN(2004,
				"没有权限访问资源");

		private final Integer code;

		private final String message;

		ResultEnum(Integer code, String message) {
			this.code = code;
			this.message = message;
		}

		@Override
		public Integer getCode() {
			return code;
		}

		@Override
		public String getMessage() {
			return message;
		}

	}

}