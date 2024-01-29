package org.gitee.ztkyn.core.crypto;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.SM2;
import org.gitee.ztkyn.core.bytes.BytesUtil;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.KeyPair;
import java.util.Arrays;

public class SM4Test {

	private static final Logger logger = LoggerFactory.getLogger(SM4Test.class);

	@Test
	public void testSM2() {
		String text = "我是一段测试aaaa";

		KeyPair pair = SecureUtil.generateKeyPair("SM2");
		byte[] privateKey = pair.getPrivate().getEncoded();
		byte[] publicKey = pair.getPublic().getEncoded();

		String privateHexString = BytesUtil.bytesToHexString(privateKey);
		String publicHexString = BytesUtil.bytesToHexString(publicKey);
		logger.info("\nprivateKey:{}\npublicKey:{}", privateHexString, publicHexString);
		logger.info("\nprivateKey:{}\npublicKey:{}", Arrays.toString(privateKey), Arrays.toString(publicKey));
		logger.info("\nprivateKey:{}\npublicKey:{}", Arrays.toString(BytesUtil.hexStringToBytes(privateHexString)),
				Arrays.toString(BytesUtil.hexStringToBytes(publicHexString)));
		// logger.info("\nprivateKey:{}\npublicKey:{}", new String(privateKey,
		// StandardCharsets.UTF_8),
		// new String(publicKey, StandardCharsets.UTF_8));

		SM2 sm2 = SmUtil.sm2(privateKey, publicKey);
		// 公钥加密，私钥解密
		String encryptStr = sm2.encryptBcd(text, KeyType.PublicKey);
		String decryptStr = StrUtil.utf8Str(sm2.decryptFromBcd(encryptStr, KeyType.PrivateKey));
		System.out.println(decryptStr);

	}

}
