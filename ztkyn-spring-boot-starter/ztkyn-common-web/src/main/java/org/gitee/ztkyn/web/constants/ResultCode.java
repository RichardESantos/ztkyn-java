package org.gitee.ztkyn.web.constants;

import cn.hutool.core.text.CharSequenceUtil;
import lombok.Getter;

/**
 * @author chenlei
 * @className ResultCode
 * @description 自定义错误码
 * @company CDFT
 * @date 2022-04-29 10:56
 */
public enum ResultCode {

	/**
	 * 成功
	 */
	OK(0, "success"), FAIL(10000, "fail"), ALERT(9999, "alert"),

	ACCESS_DENIED(4031, "AbstractAccessDecisionManager.accessDenied"),
	ACCESS_DENIED_BLACK_LIMITED(4032, "ResultCode.access_denied_black_limited"),
	ACCESS_DENIED_WHITE_LIMITED(4033, "ResultCode.access_denied_white_limited"),

	/**
	 * oauth2返回码
	 */
	SIGNATURE_DENIED(10001, "signature_denied"), BAD_CREDENTIALS(10002, "bad_credentials"),
	ACCOUNT_DISABLED(10003, "account_disabled"), ACCOUNT_EXPIRED(10004, "account_expired"),
	CREDENTIALS_EXPIRED(10005, "credentials_expired"), ACCOUNT_LOCKED(10006, "account_locked"),
	INVALID_TOKEN(20001, "invalid_token"), INVALID_SCOPE(20001, "invalid_scope"),
	INVALID_REQUEST(20002, "invalid_request"), INVALID_CLIENT(20003, "invalid_client"),
	INVALID_GRANT(20004, "invalid_grant"), REDIRECT_URI_MISMATCH(20005, "redirect_uri_mismatch"),
	UNAUTHORIZED_CLIENT(20006, "unauthorized_client"), EXPIRED_TOKEN(20007, "expired_token"),
	UNSUPPORTED_GRANT_TYPE(20008, "unsupported_grant_type"),
	UNSUPPORTED_RESPONSE_TYPE(20009, "unsupported_response_type"),

	BAD_REQUEST(40000, "bad_request"), VALID_FAIL(40001, "valid_fail"), NOT_FOUND(40004, "not_found"),
	METHOD_NOT_ALLOWED(40005, "method_not_allowed"), MEDIA_TYPE_NOT_ACCEPTABLE(40006, "media_type_not_acceptable"),
	TOO_MANY_REQUESTS(40029, "too_many_requests"), RPC_FALLBACK(40030, "rpc_fallback"),

	UNAUTHORIZED(40001, "unauthorized"),

	ACCESS_DENIED_AUTHORITY_EXPIRED(40033, "access_denied_authority_expired"),
	ACCESS_DENIED_UPDATING(40034, "access_denied_updating"), ACCESS_DENIED_DISABLED(40035, "access_denied_disabled"),

	TOKEN_ERROR(40036, "当前令牌无效"),
	/**
	 * 系统错误
	 */
	ERROR(50000, "error"), ERROR_UNKOWN(50001, "error_unkown"), GATEWAY_TIMEOUT(50004, "gateway_timeout"),
	SERVICE_UNAVAILABLE(50003, "service_unavailable"), TENANT_DENIED(50005, "tenant_denied");

	@Getter
	private int code;

	private String message;

	ResultCode() {
	}

	ResultCode(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public static ResultCode getResultEnum(int code) {
		for (ResultCode type : ResultCode.values()) {
			if (type.getCode() == code) {
				return type;
			}
		}
		return ERROR;
	}

	public static ResultCode getResultEnum(String message) {
		for (ResultCode type : ResultCode.values()) {
			if (null != type && null != type.getMessage() && type.getMessage().equals(message)) {
				return type;
			}
		}
		return ERROR;
	}

	public String getMessage() {
		return CharSequenceUtil.isBlank(message) ? "" : message;
	}

}
