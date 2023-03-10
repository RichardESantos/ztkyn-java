package org.gitee.ztkyn.web.common.util;

import java.util.List;
import java.util.Objects;

import org.gitee.ztkyn.common.base.collection.ZtkynListUtil;
import org.gitee.ztkyn.web.common.domain.TreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 树形结构工具类，如：菜单、机构等
 *
 * @author whty
 * @version 1.0
 * @description
 * @date 2023/3/10 9:29
 */
public class TreeUtil {

	private static final Logger logger = LoggerFactory.getLogger(TreeUtil.class);

	/**
	 * 根据pid，构建树节点 (单棵树)
	 */
	public static <T, E> List<TreeNode<T, E>> buildOneTree(List<TreeNode<T, E>> treeNodes, T pid) {
		List<TreeNode<T, E>> treeList = ZtkynListUtil.createFastList();
		treeNodes.stream().filter(node -> Objects.equals(pid, node.getPid())).findFirst().ifPresent(rootNode -> {
			findChildren(treeNodes, rootNode);
		});
		return treeList;
	}

	/**
	 * 根据pid，构建树节点 (多树)
	 */
	@SafeVarargs
	public static <T, E> List<TreeNode<T, E>> buildMultiTree(List<TreeNode<T, E>> treeNodes, T... pids) {
		List<TreeNode<T, E>> treeList = ZtkynListUtil.createFastList();
		for (T pid : pids) {
			treeNodes.stream().filter(node -> Objects.equals(pid, node.getPid())).findFirst().ifPresent(rootNode -> {
				findChildren(treeNodes, rootNode);
			});
		}
		return treeList;
	}

	/**
	 * 查找子节点
	 */
	private static <T, E> void findChildren(List<TreeNode<T, E>> treeNodes, TreeNode<T, E> rootNode) {
		treeNodes.stream().filter(node -> Objects.equals(node.getPid(), rootNode.getId())).forEach(node -> {
			rootNode.getChildren().add(node);
			// 查找当前节点的下一层级节点
			findChildren(treeNodes, node);
		});
	}

}
