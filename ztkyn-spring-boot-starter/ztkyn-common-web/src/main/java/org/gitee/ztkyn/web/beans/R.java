package org.gitee.ztkyn.web.beans;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.gitee.ztkyn.web.constants.ResultCode;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

@Schema
@Getter
@Setter
@Accessors(chain = true)
public class R<T> implements Serializable {

	/**
	 * 响应编码:0 成功
	 */
	@Schema(description = "响应编码")
	private int code = 0;

	/**
	 * 消息提示
	 */
	@Schema(description = "消息提示")
	private String message;

	/**
	 * 响应数据
	 */
	@Schema(description = "响应数据")
	private T data;

	/**
	 * 请求路径
	 */
	@Schema(description = "请求路径")
	private String path;

	/**
	 * http状态码
	 */
	@Schema(description = "http状态码")
	private int httpStatus;

	/**
	 * 响应时间
	 */
	@Schema(description = "响应时间")
	private long timestamp = System.currentTimeMillis();

	public static <T> R<T> ok(T data) {
		return new R<T>().setCode(ResultCode.OK.getCode()).setMessage(ResultCode.OK.getMessage()).setData(data);
	}

	public static <T> R<T> ok(ResultCode resultCode, T data) {
		return new R<T>().setCode(resultCode.getCode()).setMessage(resultCode.getMessage()).setData(data);
	}

	public static <T> R<T> success(String message) {
		return new R<T>().setCode(ResultCode.OK.getCode()).setMessage(message);
	}

	public static <T> R<T> error() {
		return new R<T>().setCode(ResultCode.FAIL.getCode()).setMessage(ResultCode.FAIL.getMessage());
	}

	public static <T> R<T> error(String message) {
		return new R<T>().setCode(ResultCode.FAIL.getCode()).setMessage(message);
	}

}
