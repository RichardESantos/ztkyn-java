package org.gitee.ztkyn.core.bytes;

/**
 * bytes 数组 与 hexString 互转工具类
 */
public class BytesUtil {

	/**
	 * 将字节数组转换为十六进制字符串。
	 * @param bytes 要转换的字节数组
	 * @return 转换后的十六进制字符串
	 */
	public static String bytesToHexString(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (byte b : bytes) {
			sb.append(String.format("%02X", b));
		}
		return sb.toString();
	}

	/**
	 * 将十六进制字符串转换为字节数组。
	 * @param hexString 要转换的十六进制字符串
	 * @return 转换后的字节数组
	 */
	public static byte[] hexStringToBytes(String hexString) {
		byte[] bytes = new byte[hexString.length() / 2];
		for (int i = 0; i < hexString.length(); i += 2) {
			bytes[i / 2] = (byte) Integer.parseInt(hexString.substring(i, i + 2), 16);
		}
		return bytes;
	}

}
