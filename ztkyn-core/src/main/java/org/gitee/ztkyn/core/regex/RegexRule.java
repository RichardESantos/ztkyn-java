package org.gitee.ztkyn.core.regex;

/**
 * @author whty
 * @version 1.0
 * @description 正则表达式规则
 * @date 2023/2/8 17:01
 */
public enum RegexRule {

	CHINESE_ADDRESS("中国地址",
			"(?<province>[^省]+自治区|.*?省|.*?行政区|.*?市)(?<city>[^市]+自治州|.*?地区|.*?行政单位|.+盟|市辖区|.*?市|.*?县)(?<county>[^县]+县|.+区|.+市|.+旗|.+海域|.+岛)?(?<town>[^区]+区|.+镇)?(?<village>.*)"),
	MYSQL_QUERY_GROUP_BY_HAVING_LIMIT("MySQL 查询语句解析（select ... from ... where ... limit...）(不校验 SQL 语法)", ""),
	MYSQL_QUERY_GROUP_BY_LIMIT("MySQL 查询语句解析（select ... from ... where ... limit...）(不校验 SQL 语法)", ""),
	MYSQL_QUERY_GROUP_BY("MySQL 查询语句解析（select ... from ... where ... limit...）(不校验 SQL 语法)", ""),
	MYSQL_QUERY_LIMIT("MySQL 查询语句解析（select ... from ... where ... limit...）(不校验 SQL 语法)", ""),
	MYSQL_QUERY_WHERE("MySQL 查询语句解析（select ... from ... where ... limit...）(不校验 SQL 语法)", ""),
	MYSQL_QUERY_DELETE("MySQL 查询语句解析（delete ... from ... where ...）(不校验 SQL 语法)", ""),
	MYSQL_QUERY_UPDATE("MySQL 查询语句解析（update ... from ... where ...）(不校验 SQL 语法)", ""),

	;

	private final String regex;

	RegexRule(String desc, String regex) {
		this.regex = regex;
	}

	public String getRegex() {
		return regex;
	}

}
