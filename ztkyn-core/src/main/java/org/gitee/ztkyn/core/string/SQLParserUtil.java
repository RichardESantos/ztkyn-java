package org.gitee.ztkyn.core.string;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.Data;
import lombok.experimental.Accessors;
import org.gitee.ztkyn.core.exception.AssertUtil;
import org.gitee.ztkyn.core.regex.RegexRule;
import org.gitee.ztkyn.core.regex.RegexUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author whty
 * @version 1.0
 * @description
 * @date 2023/2/10 10:46
 */
public class SQLParserUtil {

	private static final Logger logger = LoggerFactory.getLogger(SQLParserUtil.class);

	public static SQLAnalysis parseSQL(String sql) {
		try {
			Matcher matcher = Pattern.compile(RegexRule.MYSQL_QUERY_GROUP_BY_LIMIT.getRegex()).matcher(sql);
			List<SQLAnalysis> sqlAnalysisList = new ArrayList<>();
			while (matcher.find()) {
				SQLAnalysis sqlAnalysis = new SQLAnalysis().setSelect(matcher.group("select"))
					.setFrom(matcher.group("from"))
					.setWhere(matcher.group("where"))
					.setLimit(matcher.group("limit"));
				sqlAnalysisList.add(sqlAnalysis);
			}
			AssertUtil.expect(sqlAnalysisList.size(), 1, "SQL 解析错误");
			return sqlAnalysisList.get(0);
		}
		catch (Exception exception) {
			logger.error("SQL 解析错误", exception);
		}
		return null;
	}

	@Data
	@Accessors(chain = true)
	public static class SQLAnalysis {

		private String select;

		private String from;

		private String where;

		private String limit;

		public SQLAnalysis addWhereCondition(String whereCondition) {
			if (StringUtil.isNotBlank(getWhere())) {
				AssertUtil.expect(RegexUtil.isMatch("^(where|WHERE).*?", whereCondition), false, "已经存在where 查询条件");
				String tmpWhere = getWhere().replaceAll("^(where|WHERE)", "");
				// tmpWhere+= "where 1=1 and "+ whereCondition.replaceAll("")
			}
			else {
				this.setWhere(whereCondition);
			}
			return this;
		}

	}

}
