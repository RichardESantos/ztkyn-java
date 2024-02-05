package org.gitee.ztkyn.gateway.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.Setter;
import org.gitee.ztkyn.web.beans.R;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

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
	 * 测试表单（POST+pojo）
	 * @param user
	 * @return
	 */
	@Operation(summary = "测试表单（POST+pojo）", description = "测试表单（POST+pojo）")
	@PostMapping(value = "/cryptoFormData", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public R<String> cryptoFormData(UserEntity user) {
		return R.ok("Hello world");
	}

	/**
	 * 测试表单（POST）
	 * @param user
	 * @return
	 */
	@Operation(summary = "测试表单（POST）", description = "测试表单（POST）")
	@PostMapping(value = "/cryptoFormData2", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public R<String> cryptoFormData2(String user, String passwd) {
		return R.ok("Hello world");
	}

	/**
	 * 测试表单（POST） 可以解析到 /test/cryptoFormData3?user=string&passwd=string 这种url 里面的参数，无法解析
	 * 在body中的参数
	 * @param user
	 * @return
	 */
	@Operation(summary = "测试表单（POST）", description = "测试表单（POST）")
	@PostMapping(value = "/cryptoFormData3")
	public R<String> cryptoFormData3(@RequestParam("user") String user, @RequestParam("passwd") String passwd) {
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
