package org.gitee.ztkyn.gateway.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.Setter;
import org.gitee.ztkyn.web.beans.R;
import org.springframework.web.bind.annotation.*;
import retrofit2.http.POST;

/**
 * 测试接口（非正式接口）
 */
@Tag(name = "测试接口（非正式接口）", description = "测试接口（非正式接口）")
@RequestMapping("/test")
@RestController
public class IndexController {

	/**
	 * 测试
	 * @return
	 */
	@Operation(summary = "测试", description = "测试")
	@GetMapping("/index")
	public R<String> index() {
		return R.ok("Hello world");
	}

	/**
	 * 测试表单
	 * @param user
	 * @param passwd
	 * @return
	 */
	@Parameters({ @Parameter(name = "user", description = "", in = ParameterIn.QUERY, required = true),
			@Parameter(name = "passwd", description = "", in = ParameterIn.QUERY, required = true) })
	@Operation(summary = "测试表单", description = "测试表单")
	@GetMapping("/cryptoForm")
	public R<String> cryptoForm(@RequestParam("user") String user, @RequestParam("passwd") String passwd) {
		return R.ok("Hello world");
	}

	/**
	 * 测试表单
	 * @param user
	 * @param passwd
	 * @return
	 */
	@Operation(summary = "测试表单", description = "测试表单")
	@POST("/cryptoFormData")
	public R<String> cryptoFormData(@RequestParam("user") String user, @RequestParam("passwd") String passwd) {
		return R.ok("Hello world");
	}

	/**
	 * 测试json
	 * @param user
	 * @return
	 */
	@Operation(summary = "测试json", description = "测试json")
	@PostMapping("/cryptoJson")
	public R<String> cryptoJson(@RequestBody UserEntity user) {
		return R.ok("Hello world");
	}

	@Getter
	@Setter
	public static class UserEntity {

		String user;

		String passwd;

	}

}
