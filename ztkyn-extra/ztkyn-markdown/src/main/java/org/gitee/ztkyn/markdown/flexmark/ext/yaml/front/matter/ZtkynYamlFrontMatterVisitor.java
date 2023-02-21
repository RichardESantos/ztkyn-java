package org.gitee.ztkyn.markdown.flexmark.ext.yaml.front.matter;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.vladsch.flexmark.ext.yaml.front.matter.YamlFrontMatterBlock;
import com.vladsch.flexmark.ext.yaml.front.matter.YamlFrontMatterNode;
import com.vladsch.flexmark.ext.yaml.front.matter.YamlFrontMatterVisitor;
import com.vladsch.flexmark.ext.yaml.front.matter.YamlFrontMatterVisitorExt;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.ast.NodeVisitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author whty
 * @version 1.0
 * @description
 * @date 2023/2/21 8:27
 */
public class ZtkynYamlFrontMatterVisitor implements YamlFrontMatterVisitor {

	private static final Logger logger = LoggerFactory.getLogger(ZtkynYamlFrontMatterVisitor.class);

	final private Map<String, List<String>> data;

	final private NodeVisitor myVisitor;

	public ZtkynYamlFrontMatterVisitor() {
		myVisitor = new NodeVisitor(YamlFrontMatterVisitorExt.VISIT_HANDLERS(this));
		data = new LinkedHashMap<>();
	}

	public void visit(Node node) {
		myVisitor.visit(node);
	}

	@Override
	public void visit(YamlFrontMatterNode node) {
		data.put(node.getKey(), node.getValues());
	}

	@Override
	public void visit(YamlFrontMatterBlock node) {
		myVisitor.visitChildren(node);
	}

	public Map<String, List<String>> getData() {
		return data;
	}

}
