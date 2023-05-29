package org.gitee.ztkyn.core.json;

import java.util.List;

import org.gitee.ztkyn.core.string.StringUtil;
import org.noear.snack.ONode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author whty
 * @date 2023-04-13 10:21
 * @description Json Path 工具类
 * @version 1.0.0
 */
public class JsonPathUtil {

	private static final Logger logger = LoggerFactory.getLogger(JsonPathUtil.class);

	/**
	 * 查找多个节点的值
	 * @param json
	 * @param jsonPathFilter
	 * @param tClass
	 * @return
	 * @param <T>
	 */
	public static <T> List<T> findNodeListValue(String json, String jsonPathFilter, Class<T> tClass) {
		if (StringUtil.isBlank(json) || StringUtil.isBlank(jsonPathFilter)) {
			return null;
		}
		ONode oNode = ONode.loadStr(json);
		return oNode.select(jsonPathFilter).toObjectList(tClass);
	}

	/**
	 * 查找单个节点的值
	 * @param json
	 * @param jsonPathFilter
	 * @param tClass
	 * @return
	 * @param <T>
	 */
	public static <T> T findNodeValue(String json, String jsonPathFilter, Class<T> tClass) {
		if (StringUtil.isBlank(json) || StringUtil.isBlank(jsonPathFilter)) {
			return null;
		}
		ONode oNode = ONode.loadStr(json);
		return oNode.select(jsonPathFilter).toObject(tClass);
	}

	/**
	 * 指定指定节点的值
	 * @param json
	 * @param jsonPathFilter
	 * @param newValue
	 * @return
	 */
	public static String updateNodeValue(String json, String jsonPathFilter, Object newValue) {
		if (StringUtil.isBlank(json) || StringUtil.isBlank(jsonPathFilter)) {
			return null;
		}
		ONode oNode = ONode.loadStr(json);
		return oNode.select(jsonPathFilter).forEach(n -> n.val(newValue)).toJson();
	}

}
