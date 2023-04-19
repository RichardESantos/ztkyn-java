package org.gitee.ztkyn.common.base;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.collections.impl.list.mutable.FastList;
import org.eclipse.collections.impl.map.mutable.UnifiedMap;
import org.gitee.ztkyn.common.base.collection.ZtkynMapUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author whty
 * @version 1.0
 * @description Jackson 工具类
 * @date 2023/1/29 13:46
 */
public class JacksonUtil {

	private static final Logger logger = LoggerFactory.getLogger(JacksonUtil.class);

	private static final Map<String, JavaType> javaTypeMap = ZtkynMapUtil.createUnifiedMap(64);

	/**
	 * 默认使用的 ObjectMapper
	 */
	public static final ObjectMapper objectMapper = new ObjectMapper();

	/**
	 * 默认使用的 TypeFactory
	 */
	public static final TypeFactory typeFactory = objectMapper.getTypeFactory();

	/**
	 * 基础类 对应的 TypeReference
	 */
	public static TypeReference<String> stringTypeReference = new TypeReference<String>() {
	};

	/**
	 * 基础的对应 Map<String,Object>
	 */
	public static JavaType stringObjectMapJavaType = getMapWithStringKeyJavaType(Object.class);

	public static JavaType stringJavaType = typeFactory.constructType(stringTypeReference);

	private static final ObjectMapper allFieldMapper = new ObjectMapper();

	static {
		// 配置 ObjectMapper
		JacksonConfiguration.configuration(objectMapper);
		JacksonConfiguration.configuration(allFieldMapper);
		// 对象字段之列入所有字段
		allFieldMapper.setSerializationInclusion(Include.ALWAYS);

	}

	/**
	 * 基础的对应 Map<String,T>
	 * @param tClass
	 * @return
	 * @param <T>
	 */
	public static <T> JavaType getMapWithStringKeyJavaType(Class<T> tClass) {
		return javaTypeMap.computeIfAbsent(tClass.getTypeName(),
				s -> typeFactory.constructMapType(UnifiedMap.class, String.class, tClass));
	}

	/**
	 * Object转json字符串
	 * @param obj
	 * @param <T>
	 * @return
	 */
	public static <T> String obj2String(T obj) {
		return toJsonString(obj, objectMapper);
	}

	/**
	 * Object转json字符串（包含所有字段，不会自动去除空值）
	 * @param obj
	 * @param <T>
	 * @return
	 */
	public static <T> String obj2StringWithAllField(T obj) {
		return toJsonString(obj, allFieldMapper);
	}

	private static <T> String toJsonString(T obj, ObjectMapper mapper) {
		if (obj == null) {
			return null;
		}
		try {
			if (obj instanceof String) {
				return (String) obj;
			}
			else {
				return mapper.writeValueAsString(obj);
			}
		}
		catch (Exception e) {
			logger.error("Parse object to String error", e);
			return null;
		}
	}

	/**
	 * Object转json字符串并格式化美化
	 * @param obj
	 * @param <T>
	 * @return
	 */
	public static <T> String obj2StringPretty(T obj) {
		if (obj == null) {
			return null;
		}
		try {
			if (obj instanceof String) {
				return (String) obj;
			}
			else {
				return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
			}
		}
		catch (Exception e) {
			logger.error("Parse object to String error", e);
			return null;
		}
	}

	/**
	 * string转object
	 * @param str json字符串
	 * @param clazz 被转对象class
	 * @param <T>
	 * @return
	 */
	public static <T> T string2Obj(String str, Class<T> clazz) {
		if (StringUtils.isBlank(str) || clazz == null) {
			return null;
		}
		try {
			return clazz.equals(String.class) ? (T) str : objectMapper.readValue(str, clazz);
		}
		catch (IOException e) {
			logger.error("Parse String to Object error", e);
			return null;
		}
	}

	/**
	 * string转object 用于转为集合对象
	 * @param jsonData
	 * @param tClass
	 * @param <T>
	 * @return
	 */
	public static <T> List<T> json2List(String jsonData, Class<T> tClass) {
		if (StringUtils.isBlank(jsonData) || tClass == null) {
			return null;
		}
		JavaType javaType = objectMapper.getTypeFactory().constructCollectionLikeType(FastList.class, tClass);

		try {
			return objectMapper.readValue(jsonData, javaType);
		}
		catch (Exception e) {
			logger.error("Parse String to Object error", e);
		}
		return null;
	}

	/**
	 * string转object 用于转为集合对象
	 * @param jsonData
	 * @param paramType
	 * @return
	 * @param <T>
	 */
	public static <T> List<T> json2List(String jsonData, JavaType paramType) {
		if (StringUtils.isBlank(jsonData) || paramType == null) {
			return null;
		}
		JavaType javaType = objectMapper.getTypeFactory().constructCollectionLikeType(FastList.class, paramType);

		try {
			return objectMapper.readValue(jsonData, javaType);
		}
		catch (Exception e) {
			logger.error("Parse String to Object error", e);
		}
		return null;
	}

	/**
	 * string转object 用于转为 map,不适用于 多层 map 嵌套的情形
	 * @param jsonData
	 * @param keyType
	 * @param valueType
	 * @param <K>
	 * @param <V>
	 * @return
	 */
	public static <K, V> Map<K, V> json2Map(String jsonData, Class<K> keyType, Class<V> valueType) {
		if (StringUtils.isBlank(jsonData) || keyType == null || valueType == null) {
			return null;
		}
		JavaType javaType = objectMapper.getTypeFactory().constructMapType(UnifiedMap.class, keyType, valueType);

		try {
			return objectMapper.readValue(jsonData, javaType);
		}
		catch (Exception e) {
			logger.error("Parse String to Object error", e);
		}
		return null;
	}

	/**
	 * string转object 用于转为 map,适用于 多层 map 嵌套的情形
	 * @param jsonData
	 * @param javaType
	 * @return
	 * @param <K>
	 * @param <V>
	 */
	public static <K, V> Map<K, V> json2Map(String jsonData, JavaType javaType) {
		if (StringUtils.isBlank(jsonData) || javaType == null) {
			return null;
		}
		try {
			return objectMapper.readValue(jsonData, javaType);
		}
		catch (JsonProcessingException e) {
			logger.error("Parse String to Object error", e);
		}
		return null;
	}

	/**
	 * 多层嵌套 Map 反序列化 示例
	 * @return
	 */
	public static Map<String, Map<String, Map<String, JacksonExampleDomain>>> json2DeepMapExample() {
		Map<String, Map<String, Map<String, JacksonExampleDomain>>> sourceMap = new HashMap<>();
		for (int i = 0; i < 10; i++) {
			Map<String, Map<String, JacksonExampleDomain>> secondMap = new HashMap<>();
			for (int j = 0; j < 10; j++) {
				Map<String, JacksonExampleDomain> thirdMap = new HashMap<>();
				for (int k = 0; k < 10; k++) {
					thirdMap.put("third" + k, new JacksonExampleDomain().setName("JacksonExampleDomain" + k));
				}
				secondMap.put("second" + j, thirdMap);
			}
			sourceMap.put("one" + i, secondMap);
		}
		String json = JacksonUtil.obj2String(sourceMap);
		// 构造反序列化 数据结构
		JavaType thirdType = typeFactory.constructParametricType(HashMap.class, String.class,
				JacksonExampleDomain.class);
		JavaType secondType = typeFactory.constructParametricType(HashMap.class, stringJavaType, thirdType);
		JavaType javaType = typeFactory.constructParametricType(HashMap.class, stringJavaType, secondType);
		Map<String, Map<String, Map<String, JacksonExampleDomain>>> targetMap = json2Map(json, javaType);
		logger.info("打印反序列化结果{}", targetMap);
		return targetMap;
	}

}

@Data
@Accessors(chain = true)
class JacksonExampleDomain {

	private String name;

	private String address;

}
