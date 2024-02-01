package org.gitee.ztkyn.gateway.configuration.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 存放各种用来进行加密，解密的key
 */
@Getter
@Setter
@Accessors(chain = true)
public class GateWayCryptoKeyProperties {

	/**
	 * 服务端 私有key
	 */
	private String backendPrivateKey;

	/**
	 * 服务端 公有key (需要下发到前端)
	 */
	private String backendPublicKey;

}
