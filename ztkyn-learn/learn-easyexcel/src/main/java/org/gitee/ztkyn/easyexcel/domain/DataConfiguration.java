package org.gitee.ztkyn.easyexcel.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author richard
 * @date 2023-05-30 15:09
 * @description DataConfiguration
 * @version 1.0.0
 */
@Setter
@Getter
@Accessors(chain = true)
public class DataConfiguration {

	private static final Logger logger = LoggerFactory.getLogger(DataConfiguration.class);

	/**
	 * 表头名称,优先使用表头名称
	 */
	private String headerName;

	/**
	 * 表头序号
	 */
	private Integer headerIndex;

	/**
	 * 对应字段的名称
	 */
	private String valueName;

	/**
	 * 数据类型
	 */
	private String valueType;

}
