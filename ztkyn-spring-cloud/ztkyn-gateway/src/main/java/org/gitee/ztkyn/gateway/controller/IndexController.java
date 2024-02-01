package org.gitee.ztkyn.gateway.controller;

import jakarta.annotation.Resource;
import lombok.Getter;
import lombok.Setter;
import org.gitee.ztkyn.core.function.DataFlexHandler;
import org.gitee.ztkyn.gateway.configuration.properties.GateWayCryptoKeyProperties;
import org.gitee.ztkyn.gateway.configuration.properties.ZtkynGateWayProperties;
import org.springframework.web.bind.annotation.*;

@RequestMapping
@RestController
public class IndexController {

	@Resource
	private ZtkynGateWayProperties gateWayProperties;

	@GetMapping("/crypto")
	public String crypto() {
		return DataFlexHandler.notNull(gateWayProperties.getCryptoKey())
			.ifTrueAndConvert(GateWayCryptoKeyProperties::getBackendPublicKey);
	}

	@GetMapping("/index")
	public String index() {
		return "Hello world";
	}

	@GetMapping("/cryptoForm")
	public String cryptoForm(@RequestParam("user") String user, @RequestParam("passwd") String passwd) {
		return "Hello world";
	}

	@PostMapping("/cryptoJson")
	public String cryptoJson(@RequestBody UserEntity user) {
		return "Hello world";
	}

	@Getter
	@Setter
	public static class UserEntity {

		String user;

		String passwd;

	}

}
