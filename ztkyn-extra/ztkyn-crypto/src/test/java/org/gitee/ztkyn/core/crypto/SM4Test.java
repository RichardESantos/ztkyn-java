package org.gitee.ztkyn.core.crypto;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.BCUtil;
import cn.hutool.crypto.ECKeyUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.SM2;
import cn.hutool.crypto.digest.SM3;
import cn.hutool.crypto.symmetric.SM4;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.gitee.ztkyn.core.bytes.BytesUtil;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.security.KeyPair;

public class SM4Test {

	private static final Logger logger = LoggerFactory.getLogger(SM4Test.class);

	@Test
	public void testSM2() {
		String text = "我是一段测试aaaa";

		SM2 sm2 = ZtkynSMUtil.genSM2();
		String privateKeyStr = ZtkynSMUtil.getPrivateKeyStr(sm2);
		String publicKeyStr = ZtkynSMUtil.getPublicKeyStr(sm2);
		System.out.println(privateKeyStr);
		System.out.println(publicKeyStr);
		// 公钥加密，私钥解密
		String encryptStr = sm2.encryptBcd(text, KeyType.PublicKey);
		String decryptStr = StrUtil.utf8Str(sm2.decryptFromBcd(encryptStr, KeyType.PrivateKey));
		System.out.println(decryptStr);

	}

	@Test
	public void testSM3() {
		String text = "我是一段测试aaaa";
		for (int i = 0; i < 100; i++) {
			SM3 sm3 = SM3.create();
			System.out.println(sm3.digestHex(text));
		}
	}

	@Test
	public void testSM24() {
		String text = "私钥：这个保存好，切记不要泄漏，真的泄露了就重新生成一下";

		KeyPair pair = SecureUtil.generateKeyPair("SM2");

		// 私钥：这个保存好，切记不要泄漏，真的泄露了就重新生成一下
		byte[] privateKey = BCUtil.encodeECPrivateKey(pair.getPrivate());
		// 公钥：这个是前后端加密用的，不压缩选择带04的，不带04到时候前端会报错
		byte[] publicKey = ((BCECPublicKey) pair.getPublic()).getQ().getEncoded(false);

		String privateHexString = BytesUtil.bytesToHexString(privateKey);
		String publicHexString = BytesUtil.bytesToHexString(publicKey);
		System.out.println(privateHexString);
		System.out.println(publicHexString);

		SM2 pubSM2 = SmUtil.sm2(null, ECKeyUtil.toSm2PublicParams(publicKey));
		SM2 priSM2 = SmUtil.sm2(ECKeyUtil.toSm2PrivateParams(privateKey), null);

		System.out.println("--------------------------------");
		// 固定长度的SM4 key
		String sm4Key = RandomUtil.randomString(16);
		System.out.println(sm4Key);
		SM4 sm4 = SmUtil.sm4(sm4Key.getBytes(StandardCharsets.UTF_8));

		// sm4加密内容，sm2加密 sm4 ky
		String encodeText = sm4.encryptHex(text);

		String signKey = pubSM2.encryptHex(sm4Key, KeyType.PublicKey);
		System.out.println(encodeText);
		System.out.println(signKey);
		System.out.println("--------------------------------");
		// sm2 解密 sm4 key, sm4 解密内容
		String deSignKey = priSM2.decryptStr(signKey, KeyType.PrivateKey);
		System.out.println(deSignKey);
		SM4 decodeSm4 = SmUtil.sm4(deSignKey.getBytes(StandardCharsets.UTF_8));
		String decodeText = decodeSm4.decryptStr(encodeText);
		System.out.println(decodeText);

	}

}
