package org.gitee.ztkyn.core.io;

import java.nio.file.Paths;

import org.gitee.ztkyn.core.string.CharsetUtil;
import org.junit.jupiter.api.Test;

/**
 * @author whty
 * @version 1.0
 * @description
 * @date 2023/1/17 9:08
 */
class FileUtilTest {

	@Test
	void readAllLines() {
		String path = "D:\\各种文档\\项目文档\\行业知识图谱管理平台BlueKG\\电影数据\\事件抽取数据\\data\\dev.json";
		// FileUtil.readAllLines(path, StandardCharsets.UTF_8);
		path = "F:\\Download\\Edge\\神魔书.txt";

		String content = FileUtil.readContent(Paths.get(path).toFile(), CharsetUtil.GB18030);
		FileUtil.writeContent(path + "_bak", content, CharsetUtil.GBK, false);
	}

}