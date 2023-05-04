package org.gitee.ztkyn.core.regex;

/**
 * @author whty
 * @version 1.0
 * @description 正则表达式规则 参考自 <a href="https://hub.yzuu.cf/any86/any-rule">any-rule</a>
 * @date 2023/2/8 17:01
 */
public enum RegexRule {

	CHINESE_ADDRESS("中国地址",
			"(?<province>[^省]+自治区|.*?省|.*?行政区|.*?市)(?<city>[^市]+自治州|.*?地区|.*?行政单位|.+盟|市辖区|.*?市|.*?县)(?<county>[^县]+县|.+区|.+市|.+旗|.+海域|.+岛)?(?<town>[^区]+区|.+镇)?(?<village>.*)"),
	NET_URI("网址(URL)",
			"/^(((ht|f)tps?):\\/\\/)?([^!@#$%^&*?.\\s-]([^!@#$%^&*?.\\s]{0,63}[^!@#$%^&*?.\\s])?\\.)+[a-z]{2,6}\\/?/"),
	SOCIAL_CREDIT_CODE("统一社会信用代码", "/^[0-9A-HJ-NPQRTUWXY]{2}\\d{6}[0-9A-HJ-NPQRTUWXY]{10}$/"),
	THUNDER_LINK("迅雷链接", "/^thunderx?:\\/\\/[a-zA-Z\\d]+=$/"),
	STOCK_CODE_A_SHARE("股票代码(A股)", "/^(s[hz]|S[HZ])(000[\\d]{3}|002[\\d]{3}|300[\\d]{3}|600[\\d]{3}|60[\\d]{4})$/"),
	BASE64("base64格式",
			"/^\\s*data:(?:[a-z]+\\/[a-z0-9-+.]+(?:;[a-z-]+=[a-z0-9-]+)?)?(?:;base64)?,([a-z0-9!$&',()*+;=\\-._~:@/?%\\s]*?)\\s*$/i"),
	CASH("数字/货币金额（支持负数、千分位分隔符）", "/^-?\\d{1,3}(,\\d{3})*(\\.\\d{1,2})?$/"),
	CHINESE_NAME("中文姓名", "/^(?:[\\u4e00-\\u9fa5·]{2,16})$/"),
	ENGLISH_NAME("英文姓名", "/(^[a-zA-Z][a-zA-Z\\s]{0,20}[a-zA-Z]$)/"), CHINESE_PLATE_NUMBER("车牌号(新能源+非新能源)",
			"/^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领][A-HJ-NP-Z][A-HJ-NP-Z0-9]{4,5}[A-HJ-NP-Z0-9挂学警港澳]$/");

	private final String regex;

	RegexRule(String desc, String regex) {
		this.regex = regex;
	}

	public String getRegex() {
		return regex;
	}

}
