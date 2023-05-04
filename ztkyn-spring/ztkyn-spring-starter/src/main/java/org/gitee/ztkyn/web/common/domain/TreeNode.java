package org.gitee.ztkyn.web.common.domain;

import java.io.Serializable;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.gitee.ztkyn.common.base.collection.ECollectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 树节点，所有需要实现树节点的，都需要继承该类
 *
 * T 为 ID 类型, E 为 节点元素类型
 *
 * @author whty
 * @version 1.0
 * @description
 * @date 2023/3/10 9:16
 */
@Data
public class TreeNode<T, E> implements Serializable {

	private static final Logger logger = LoggerFactory.getLogger(TreeNode.class);

	/**
	 * 主键
	 */
	@Schema(description = "id")
	@NotNull(message = "ID不能为空")
	private T id;

	/**
	 * 上级ID,如果为顶级节点，填充
	 */
	@Schema(description = "上级ID")
	@NotNull(message = "ID不能为空")
	private T pid;

	/**
	 * 当前节点数据内容，用来显示当前节点名称等
	 */
	@Schema(description = "当前节点数据内容，用来显示当前节点名称等")
	private E nodeData;

	/**
	 * 子节点列表
	 */
	private List<TreeNode<T, E>> children = ECollectionUtil.createFastList();

}
