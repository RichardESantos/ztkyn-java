package org.gitee.ztkyn.common.base;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.xm.Similarity;

/**
 * @author whty
 * @version 1.0
 * @description
 * @date 2023/2/27 10:29
 */
class ZtkynStringSimilarityUtilTest {

	private static final List<String> strList = new ArrayList<>();
	static {
		strList.add("京东");
		strList.add("京东公司");
		strList.add("京东有限公司");
		strList.add("京东有限责任公司");

		strList.add("天猫");
		strList.add("天猫公司");
		strList.add("天猫有限公司");
		strList.add("天猫有限责任公司");

		strList.add("教师");
	}

	@Test
	void cilinSimilarity() {
		for (int i = 0; i < strList.size(); i++) {
			for (int j = i + 1; j < strList.size(); j++) {
				String A = strList.get(i);
				String B = strList.get(j);
				System.out.println(MessageFormat.format("{0}:{1} 直接的相似度:{2}", A, B, Similarity.phraseSimilarity(A, B)));
				;
			}
		}
	}

	@Test
	void phraseSimilarity() {
	}

	@Test
	void morphoSimilarity() {
	}

	@Test
	void cosineSimilarity() {
	}

	@Test
	void hownetWordTendency() {
	}

}