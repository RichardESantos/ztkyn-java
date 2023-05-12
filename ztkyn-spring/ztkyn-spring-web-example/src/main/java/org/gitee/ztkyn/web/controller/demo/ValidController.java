package org.gitee.ztkyn.web.controller.demo;

import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.gitee.ztkyn.web.common.config.RestResponse;
import org.gitee.ztkyn.web.common.config.validation.Update;
import org.gitee.ztkyn.web.common.domain.ResponseResult;
import org.gitee.ztkyn.web.domain.example.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author whty
 * @version 1.0
 * @description controller 模板
 * @date 2023/1/18 15:29
 */
@Validated
@Tag(name = "参数校验模板(controller)", description = "controller 模板")
@Controller
@RequestMapping("/validController")
public class ValidController {

	private static final Logger logger = LoggerFactory.getLogger(ValidController.class);

	/**
	 * requestParam/PathVariable参数校验 GET请求一般会使用requestParam/PathVariable传参。如果参数比较多 (比如超过 6
	 * 个)，还是推荐使用DTO对象接收。否则，推荐将一个个参数平铺到方法入参中。在这种情况下，必须在Controller类上标注@Validated注解，并在入参上声明约束注解
	 * (如@Min等)。如果校验失败，会抛出ConstraintViolationException异常。
	 */

	/**
	 * // @PathVariable 参数校验
	 * @param num
	 * @return
	 */
	@Parameter(name = "num", description = "", in = ParameterIn.PATH, required = true)
	@Operation(summary = "@PathVariable 参数校验", description = "")
	@GetMapping("/{num}")
	public Integer detail(@PathVariable("num") @Min(1) @Max(20) Integer num) {
		return num * num;
	}

	/**
	 * // @RequestParam 参数校验
	 * @param email
	 * @return
	 */
	@Parameter(name = "email", description = "", in = ParameterIn.QUERY, required = true)
	@Operation(summary = "@RequestParam 参数校验", description = "")
	@GetMapping("/getByEmail")
	public String getByAccount(@RequestParam @NotBlank @Email String email) {
		return email;
	}

	// ----------------------------------------------------
	/**
	 * POST、PUT请求一般会使用requestBody传递参数，这种情况下，后端使用 DTO 对象进行接收。只要给 DTO
	 * 对象加上@Validated注解就能实现自动参数校验。 这种情况下，使用@Valid和@Validated都可以。
	 */

	/**
	 * @param userDTO
	 * @return
	 */
	@PostMapping("/save")
	@ResponseBody
	@RestResponse
	public ResponseResult<?> saveUser(@RequestBody @Validated UserDTO userDTO) {
		return ResponseResult.success();
	}

	/**
	 * 分组校验
	 * @param userDTO
	 * @return
	 */
	@PostMapping("/update")
	@ResponseBody
	@RestResponse
	public ResponseResult<?> updateUser(@RequestBody @Validated(Update.class) UserDTO userDTO) {
		return ResponseResult.success();
	}

}
