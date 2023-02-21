import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import com.vladsch.flexmark.ext.yaml.front.matter.YamlFrontMatterExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.DataHolder;
import com.vladsch.flexmark.util.data.MutableDataSet;
import org.gitee.ztkyn.core.io.FileUtil;
import org.gitee.ztkyn.core.string.StringUtil;
import org.gitee.ztkyn.markdown.flexmark.ext.yaml.front.matter.ZtkynYamlFrontMatterVisitor;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author whty
 * @version 1.0
 * @description
 * @date 2023/2/20 15:53
 */
public class MarkDownTest {

	private static final Logger logger = LoggerFactory.getLogger(MarkDownTest.class);

	final private static DataHolder OPTIONS = new MutableDataSet()
			.set(Parser.EXTENSIONS, Collections.singleton(YamlFrontMatterExtension.create())).toImmutable();

	final private static @NotNull Parser PARSER = Parser.builder(OPTIONS).build();

	final private static @NotNull HtmlRenderer RENDERER = HtmlRenderer.builder(OPTIONS).build();

	@Test
	public void readMD() {
		String filePath = "C:\\Users\\whty\\Desktop\\基本信息模板.md";
		String content = FileUtil.readUTF8Content(filePath);
		System.out.println(content);

		try (RandomAccessFile randomAccessFile = new RandomAccessFile(new File(filePath), "rw")) {
			cleanBlankLineFrontText(randomAccessFile);
			int index = 0;// 文件行号
			int lastPoint = 0;// 上一行文本对应的 filePointer
			String line;
			// 读取yaml
			while ((line = randomAccessFile.readLine()) != null) {

			}
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

	// 清理第一行有内容文本前的空行
	private static void cleanBlankLineFrontText(RandomAccessFile randomAccessFile) throws IOException {
		String line = randomAccessFile.readLine();
		if (StringUtil.isBlank(line)) {
			cleanBlankLineFrontText(randomAccessFile);
		}
		else {
			// 将第一行文本内容 + 以后所有的内容读取到内存中，再重新写入到文件中
			StringJoiner joiner = new StringJoiner("\n");
			joiner.add(line);
			while ((line = randomAccessFile.readLine()) != null) {
				joiner.add(line);
			}
			randomAccessFile.seek(0);
			randomAccessFile.writeBytes(joiner.toString());
		}
	}

	private Map<String, List<String>> getFrontMatter(String input, Parser PARSER) {
		ZtkynYamlFrontMatterVisitor visitor = new ZtkynYamlFrontMatterVisitor();
		Node document = PARSER.parse(input);
		visitor.visit(document);

		Map<String, List<String>> data = visitor.getData();
		return data;
	}

}
