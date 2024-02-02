package org.gitee.ztkyn.core.json;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONConfig;
import cn.hutool.json.JSONObject;

/**
 * JSONObject 工具类
 */
public class JsonObjectUtil {

	public static final JSONConfig jsonConfig = new JSONConfig().setNatureKeyComparator().setIgnoreNullValue(true);

	public static JSONObject sortFullJson(JSONObject jsonObject) {
		JSONObject newValue = new JSONObject(jsonConfig);
		jsonObject.entrySet().forEach(stringObjectEntry -> {
			Object entryValue = stringObjectEntry.getValue();
			if (entryValue instanceof JSONObject) {
				stringObjectEntry.setValue(sortFullJson((JSONObject) entryValue));
			}
			else if (entryValue instanceof JSONArray) {
				stringObjectEntry.setValue(sortFullJson((JSONArray) entryValue));
			}
		});
		jsonObject.entrySet()
			.stream()
			.sorted((o1, o2) -> o1.getKey().compareToIgnoreCase(o2.getKey()))
			.toList()
			.forEach(stringObjectEntry -> newValue.set(stringObjectEntry.getKey(), stringObjectEntry.getValue()));
		return newValue;
	}

	public static JSONArray sortFullJson(JSONArray jsonArray) {
		JSONArray newValue = new JSONArray();
		jsonArray.forEach(object -> {
			if (object instanceof JSONObject) {
				newValue.add(sortFullJson((JSONObject) object));
			}
			else if (object instanceof JSONArray) {
				newValue.add(sortFullJson((JSONArray) object));
			}
			else {
				newValue.add(object);
			}
		});
		return newValue;
	}

}
