package org.gitee.ztkyn.core.regex;

/**
 * @author whty
 * @version 1.0
 * @description
 * @date 2023/2/8 17:01
 */
public enum RegexRule {

	CHINESE_ADDRESS("中国地址",
			"(?<province>[^省]+自治区|.*?省|.*?行政区|.*?市)(?<city>[^市]+自治州|.*?地区|.*?行政单位|.+盟|市辖区|.*?市|.*?县)(?<county>[^县]+县|.+区|.+市|.+旗|.+海域|.+岛)?(?<town>[^区]+区|.+镇)?(?<village>.*)");

	private final String regex;

	RegexRule(String desc, String regex) {
		this.regex = regex;
	}

	public String getRegex() {
		return regex;
	}

}
