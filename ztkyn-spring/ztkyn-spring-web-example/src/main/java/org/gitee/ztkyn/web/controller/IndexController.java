package org.gitee.ztkyn.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author whty
 * @version 1.0
 * @description controller 模板
 * @date 2023/1/18 15:29
 */
@Tag(name = "controller 模板", description = "controller 模板")
@RestController("/index")
public class IndexController {
	private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

	/**
	 * 参数校验
	 * @param param
	 * @return
	 */
	@Parameter(name = "param", description = "", in = ParameterIn.QUERY, required = true)
	@Operation(summary = "参数校验", description = "参数校验")
	@Parameter(name = "param", description = "", in = ParameterIn.QUERY, required = true)
	@PostMapping("validateParam")
	public String validateParam(@RequestParam("param") String param) {
		return "SUCCESS";
	}
}
