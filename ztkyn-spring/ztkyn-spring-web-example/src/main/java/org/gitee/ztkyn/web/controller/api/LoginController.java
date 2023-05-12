package org.gitee.ztkyn.web.controller.api;

import java.util.Objects;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.dev33.satoken.secure.BCrypt;
import cn.dev33.satoken.stp.StpUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.gitee.ztkyn.web.common.domain.ResponseResult;
import org.gitee.ztkyn.web.common.service.ZtkynUserInfoService;
import org.gitee.ztkyn.web.domain.entity.ZtkynUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author whty
 * @version 1.0.0
 * @date 2023-04-19 15:07
 * @description LoginController
 */
@Tag(name = "登录管理", description = "LoginController")
@RequestMapping("/api")
@RestController
public class LoginController {

	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

	@Resource
	private ZtkynUserInfoService<ZtkynUser, Long> userInfoService;

	/**
	 * 登录接口(支持多设备同时登录)
	 * @param userName
	 * @param password
	 * @return
	 */
	@Parameters({ @Parameter(name = "userName", description = "", in = ParameterIn.QUERY, required = true),
			@Parameter(name = "password", description = "", in = ParameterIn.QUERY, required = true),
			@Parameter(name = "device", description = "", in = ParameterIn.QUERY, required = true) })
	@Operation(summary = "登录接口(支持多设备同时登录)", description = "登录接口(支持多设备同时登录)")
	@GetMapping("/login")
	public ResponseResult<?> login(@RequestParam String userName, @RequestParam String password,
			@RequestParam String device) {
		ZtkynUser userInfo = userInfoService.getUserInfo(userName);
		if (Objects.nonNull(userInfo) && BCrypt.checkpw(password, userInfo.getPasswd())) {
			StpUtil.login(userInfo.getId(), device);
			return ResponseResult.success("登录成功", StpUtil.getTokenInfo());
		}
		return ResponseResult.failed("登录失败");
	}

}
