package org.gitee.ztkyn.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.gitee.ztkyn.web.common.config.RestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author whty
 * @version 1.0
 * @description restController 模板
 * @date 2023/1/18 15:29
 */
@Tag(name = "restController 模板", description = "restController 模板")
@RestController
@RequestMapping("/indexRest")
@RestResponse
public class IndexRestController {

	private static final Logger logger = LoggerFactory.getLogger(IndexRestController.class);

	/**
	 * 参数校验
	 * @param param
	 * @return
	 */
	@Parameter(name = "param", description = "", in = ParameterIn.QUERY, required = true)
	@Operation(summary = "参数校验", description = "参数校验")
	@PostMapping("validateParam")
	public String validateParam(@RequestParam("param") String param) {
		return "SUCCESS";
	}

}
