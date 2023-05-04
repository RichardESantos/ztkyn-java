package org.gitee.ztkyn.core.string;

import java.util.Random;

import org.gitee.ztkyn.core.regex.RegexUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author whty
 * @version 1.0
 * @description
 * @date 2023/1/31 13:40
 */
public class ColorUtil {

	private static final Logger logger = LoggerFactory.getLogger(ColorUtil.class);

	private static final String regHex = "^#([0-9a-fA-f]{3}|[0-9a-fA-f]{6})$";

	private static final String regRgb = "^(RGB\\(|rgb\\()([0-9]{1,3},){2}[0-9]{1,3}\\)$";

	private static final String regRepRgb = "(rgb|\\(|\\)|RGB)*";

	/**
	 * 随机生成颜色代码
	 * @return
	 */
	public static String genHex() {
		// 红色
		String red;
		// 绿色
		String green;
		// 蓝色
		String blue;
		// 生成随机对象
		Random random = new Random();
		// 生成红色颜色代码
		red = Integer.toHexString(random.nextInt(256)).toUpperCase();
		// 生成绿色颜色代码
		green = Integer.toHexString(random.nextInt(256)).toUpperCase();
		// 生成蓝色颜色代码
		blue = Integer.toHexString(random.nextInt(256)).toUpperCase();

		// 判断红色代码的位数
		red = red.length() == 1 ? "0" + red : red;
		// 判断绿色代码的位数
		green = green.length() == 1 ? "0" + green : green;
		// 判断蓝色代码的位数
		blue = blue.length() == 1 ? "0" + blue : blue;
		// 生成十六进制颜色值
		return "#" + red + green + blue;
	}

	/**
	 * 颜色十六进制转颜色RGB
	 * @param hex
	 * @return
	 */
	public static String hex2Rgb(String hex) {
		StringBuilder sb = new StringBuilder();

		if (!isHex(hex)) {
			logger.error("颜色十六进制格式 【{}】 不合法，请确认！", hex);
			return null;
		}

		String c = RegexUtil.replaceAll(hex.toUpperCase(), "#", StringUtil.emptyStr);

		String r = Integer.parseInt((c.length() == 3 ? c.charAt(0) + c.substring(0, 1) : c.substring(0, 2)), 16)
				+ StringUtil.emptyStr;
		String g = Integer.parseInt((c.length() == 3 ? c.charAt(1) + c.substring(1, 2) : c.substring(2, 4)), 16)
				+ StringUtil.emptyStr;
		String b = Integer.parseInt((c.length() == 3 ? c.charAt(2) + c.substring(2, 3) : c.substring(4, 6)), 16)
				+ StringUtil.emptyStr;

		sb.append("RGB(").append(r).append(",").append(g).append(",").append(b).append(")");

		return sb.toString();
	}

	/**
	 * 颜色RGB转十六进制
	 * @param rgb
	 * @return
	 */
	public static String rgb2Hex(String rgb) {
		StringBuilder sb = new StringBuilder();

		if (isRgb(rgb)) {
			logger.error("颜色 RGB 格式【{}】 不合法，请确认！", rgb);
			return null;
		}

		String r = Integer.toHexString(getRed(rgb)).toUpperCase();
		String g = Integer.toHexString(getGreen(rgb)).toUpperCase();
		String b = Integer.toHexString(getBlue(rgb)).toUpperCase();

		sb.append("#");
		sb.append(r.length() == 1 ? "0" + r : r);
		sb.append(g.length() == 1 ? "0" + g : g);
		sb.append(b.length() == 1 ? "0" + b : b);

		return sb.toString();
	}

	public static boolean isHex(String hex) {
		return RegexUtil.isMatch(regHex, hex);
	}

	/**
	 * <strong>获取颜色RGB红色值</strong><br>
	 */
	public static int getRed(String rgb) {
		return Integer.parseInt(getRGB(rgb)[0]);
	}

	/**
	 * <strong>获取颜色RGB绿色值</strong><br>
	 */
	public static int getGreen(String rgb) {
		return Integer.parseInt(getRGB(rgb)[1]);
	}

	/**
	 * <strong>获取颜色RGB蓝色值</strong><br>
	 */
	public static int getBlue(String rgb) {
		return Integer.parseInt(getRGB(rgb)[2]);
	}

	/**
	 * <strong>获取颜色RGB数组</strong><br>
	 */
	public static String[] getRGB(String rgb) {
		return RegexUtil.replaceAll(StringUtil.strip(rgb), regRepRgb, StringUtil.emptyStr).split(",");
	}

	public static boolean isRgb(String rgb) {
		boolean r = getRed(rgb) >= 0 && getRed(rgb) <= 255;
		boolean g = getGreen(rgb) >= 0 && getGreen(rgb) <= 255;
		boolean b = getBlue(rgb) >= 0 && getBlue(rgb) <= 255;

		return isRgbFormat(rgb) && r && g && b;
	}

	public static boolean isRgbFormat(String rgb) {
		return RegexUtil.isMatch(regRgb, StringUtil.strip(rgb));
	}

}
