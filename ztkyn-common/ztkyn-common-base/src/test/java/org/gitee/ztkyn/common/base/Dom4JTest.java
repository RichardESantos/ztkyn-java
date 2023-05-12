package org.gitee.ztkyn.common.base;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author whty
 * @version 1.0.0
 * @date 2023-05-05 16:41
 * @description Dom4JTest
 */
public class Dom4JTest {

	private static final Logger logger = LoggerFactory.getLogger(Dom4JTest.class);

	String fileUrl = "C:\\Users\\whty\\Desktop\\预算管理驾驶舱 - 副本.frm";

	@Test
	void cleanEmptyElement() {
		SAXReader reader = new SAXReader();
		try {
			Document document = reader.read(Paths.get(fileUrl).toFile());
			Element rootElement = document.getRootElement();
			Iterator<Element> elementIterator = rootElement.elementIterator();
			elementIter(elementIterator);

			// 保存清理之后的文件
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(Paths.get(fileUrl + "_new").toFile()));
			document.write(bufferedWriter);
			bufferedWriter.flush();
			bufferedWriter.close();
		}
		catch (DocumentException | IOException e) {
			throw new RuntimeException(e);
		}
	}

	private static void elementIter(Iterator<Element> elementIterator) {
		while (elementIterator.hasNext()) {
			Element next = elementIterator.next();
			List<Element> elements = next.elements();
			List<Attribute> attributes = next.attributes();
			boolean isEmpty = elements.isEmpty() && attributes.isEmpty();
			String nodeName = next.getName();
			logger.info("节点名称:【{}】,是否为空: {}", nodeName, isEmpty);
			if ("IM".equals(nodeName)) {
				// 删除当前节点
				next.detach();
			}
			if (isEmpty) {
				// 删除当前节点
				next.detach();
			}
			else {
				elementIter(elements.iterator());
			}

		}
	}

}
