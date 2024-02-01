package org.gitee.ztkyn.gateway.controller;

import jakarta.annotation.Resource;
import lombok.Getter;
import lombok.Setter;
import org.gitee.ztkyn.core.function.DataFlexHandler;
import org.gitee.ztkyn.gateway.configuration.properties.GateWayCryptoKeyProperties;
import org.gitee.ztkyn.gateway.configuration.properties.ZtkynGateWayProperties;
import org.gitee.ztkyn.web.beans.R;
import org.springframework.web.bind.annotation.*;

@RequestMapping
@RestController
public class IndexController {

	@Resource
	private ZtkynGateWayProperties gateWayProperties;

	/**
	 * 获取最新的加密密匙
	 * @return
	 */
	@GetMapping("/crypto")
	public String crypto() {
		return DataFlexHandler.notNull(gateWayProperties.getCryptoKey())
			.ifTrueAndConvert(GateWayCryptoKeyProperties::getBackendPublicKey);
	}

	/**
	 * 测试
	 * @return
	 */
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
	@GetMapping("/cryptoForm")
	public R<String> cryptoForm(@RequestParam("user") String user, @RequestParam("passwd") String passwd) {
		return R.ok("Hello world");
	}

	/**
	 * 测试json
	 * @param user
	 * @return
	 */
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
