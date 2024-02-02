package org.gitee.ztkyn.gateway.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.gitee.ztkyn.core.function.DataFlexHandler;
import org.gitee.ztkyn.gateway.configuration.properties.ZtkynGateWayProperties;
import org.gitee.ztkyn.web.beans.R;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 加密相关的接口
 */
@Tag(name = "加密相关的接口", description = "加密相关的接口")
@RestController
public class CryptoController {

	@Resource
	private ZtkynGateWayProperties gateWayProperties;

	/**
	 * 获取最新的加密密匙
	 * @return
	 */
	@Operation(summary = "获取最新的加密密匙", description = "获取最新的加密密匙")
	@GetMapping("/crypto")
	public R<String> crypto() {
		return DataFlexHandler.notNull(gateWayProperties.getCryptoKey())
			.ifTrueAndConvert(gateWayCryptoKeyProperties -> R.ok(gateWayCryptoKeyProperties.getBackendPublicKey()),
					R.success());
	}

}
