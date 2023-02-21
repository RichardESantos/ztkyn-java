package org.gitee.ztkyn.core.io;

import java.io.File;

import org.junit.jupiter.api.Test;

/**
 * @author whty
 * @version 1.0
 * @description
 * @date 2023/2/20 15:15
 */
class RandomAccessFileUtilTest {

	@Test
	void findContentAndThen() {
		String filePath = "C:\\Users\\whty\\Desktop\\1.2 - 副本 - 副本 (2).txt";
		RandomAccessFileUtil.findContentAndThen(new File(filePath), "2023-02-03",
				matchResult -> "2023-04-03 =================================================== 2023-04-03");
	}

}