package org.gitee.ztkyn.web.controller;

import java.util.Objects;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.dev33.satoken.secure.BCrypt;
import cn.dev33.satoken.stp.StpUtil;
import jakarta.annotation.Resource;
import org.gitee.ztkyn.web.common.ZtkynUserInfoService;
import org.gitee.ztkyn.web.common.domain.ResponseResult;
import org.gitee.ztkyn.web.common.domain.ZtkynUserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author whty
 * @version 1.0.0
 * @date 2023-04-19 15:07
 * @description LoginController
 */
@RequestMapping("/api/sys")
@RestController
public class LoginController {

	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

	@Resource
	private ZtkynUserInfoService userInfoService;

	@GetMapping("/login")
	public ResponseResult<?> login(@RequestParam String userName, @RequestParam String password) {
		ZtkynUserInfo userInfo = userInfoService.getUserInfo(userName);
		if (Objects.nonNull(userInfo) && BCrypt.checkpw(password, userInfo.getPasswd())) {
			StpUtil.login(userInfo.getUserId());
			return ResponseResult.success("登录成功", StpUtil.getTokenInfo());
		}
		return ResponseResult.failed("登录失败");
	}

}
