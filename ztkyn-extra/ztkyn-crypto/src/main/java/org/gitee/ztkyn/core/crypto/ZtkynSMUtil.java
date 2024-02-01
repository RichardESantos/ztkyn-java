package org.gitee.ztkyn.core.crypto;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.BCUtil;
import cn.hutool.crypto.ECKeyUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.SM2;
import cn.hutool.crypto.symmetric.SM4;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.gitee.ztkyn.core.bytes.BytesUtil;

import java.nio.charset.StandardCharsets;

/**
 * 国密工具类
 */
public class ZtkynSMUtil {

	public static SM2 genSM2() {
		return SmUtil.sm2();
	}

	public static SM2 genSM2(String privateHexString, String publicHexString) {
		return SmUtil.sm2(ECKeyUtil.toSm2PrivateParams(privateHexString), ECKeyUtil.toSm2PublicParams(publicHexString));
	}

	/**
	 * 只支持HEX编码的String
	 * @param hexString
	 * @return
	 */
	public static SM2 genSm2FromPublicKey(String hexString) {
		return SmUtil.sm2(null, ECKeyUtil.toSm2PublicParams(hexString));
	}

	/**
	 * 只支持HEX编码的String
	 * @param hexString
	 * @return
	 */
	public static SM2 genSm2FromPrivateKey(String hexString) {
		return SmUtil.sm2(ECKeyUtil.toSm2PrivateParams(hexString), null);
	}

	public static String getPrivateKeyStr(SM2 sm2) {
		return BytesUtil.bytesToHexString(getPrivateKey(sm2));
	}

	/**
	 * 私钥：这个保存好，切记不要泄漏，真的泄露了就重新生成一下
	 * @param sm2
	 * @return
	 */
	public static byte[] getPrivateKey(SM2 sm2) {
		return BCUtil.encodeECPrivateKey(sm2.getPrivateKey());
	}

	public static String getPublicKeyStr(SM2 sm2) {
		return BytesUtil.bytesToHexString(getPublicKey(sm2));
	}

	/**
	 * 公钥：这个是前后端加密用的，不压缩选择带04的，不带04到时候前端会报错
	 * @param sm2
	 * @return
	 */
	public static byte[] getPublicKey(SM2 sm2) {
		return ((BCECPublicKey) sm2.getPublicKey()).getQ().getEncoded(false);
	}

	/**
	 * 使用 公钥 加密
	 * @param sm2
	 * @param content
	 * @return
	 */
	public static String encodeHex(SM2 sm2, String content) {
		return sm2.encryptHex(content, StandardCharsets.UTF_8, KeyType.PublicKey);
	}

	/**
	 * 混合加密SM2+SM4,SM4密钥放置于加密内容前端，密钥密文固定长度226位
	 * @param sm2
	 * @param content
	 * @return
	 */
	public static String mixEncodeHex(SM2 sm2, String content) {
		String sm4Key = genSM4Key();
		return encodeHex(sm2, sm4Key) + encodeHex(genSM4(sm4Key), content);
	}

	/**
	 * 使用 私钥 加密
	 * @param sm2
	 * @param cryptoContent
	 * @return
	 */
	public static String decodeHex(SM2 sm2, String cryptoContent) {
		return sm2.decryptStr(cryptoContent, KeyType.PrivateKey, StandardCharsets.UTF_8);
	}

	/**
	 * 混合解密SM2+SM4,SM4密钥放置于加密内容前端，密钥密文固定长度226位
	 * @param sm2
	 * @param cryptoContent
	 * @return
	 */
	public static String mixDecodeHex(SM2 sm2, String cryptoContent) {
		return decodeHex(genSM4(getMixSM4Key(sm2, cryptoContent)), cryptoContent.substring(226));
	}

	/**
	 * 获取SM4 key 混合解密SM2+SM4,SM4密钥放置于加密内容前端，密钥密文固定长度226位
	 * @param sm2
	 * @param cryptoContent
	 * @return
	 */
	public static String getMixSM4Key(SM2 sm2, String cryptoContent) {
		return decodeHex(sm2, cryptoContent.substring(0, 226));
	}

	/**
	 * 此方法只是为了提醒让这么用 密钥长度固定16位，加密后226位
	 * @return
	 */
	public static String genSM4Key() {
		return RandomUtil.randomString(16);
	}

	public static SM4 genSM4(String key) {
		return SmUtil.sm4(key.getBytes(StandardCharsets.UTF_8));
	}

	public static String encodeHex(SM4 sm4, String content) {
		return sm4.encryptHex(content, StandardCharsets.UTF_8);
	}

	public static String decodeHex(SM4 sm4, String cryptoContent) {
		return sm4.decryptStr(cryptoContent, StandardCharsets.UTF_8);
	}

}
