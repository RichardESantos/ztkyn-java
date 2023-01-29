package org.gitee.ztkyn.common.base;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.hutool.core.bean.BeanUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author whty
 * @version 1.0
 * @description
 * @date 2023/1/29 13:46
 */
public class JacksonUtil {

	private static final Logger logger = LoggerFactory.getLogger(JacksonUtil.class);

	private static final ObjectMapper objectMapper = new ObjectMapper();

	private static final ObjectMapper allFieldMapper;

	private static final JavaTimeModule javaTimeModule = new JavaTimeModule();

	// 建立Json操作中的日期格式
	private static final String JSON_STANDARD_FORMAT = "yyyy-MM-dd HH:mm:ss";

	static {
		// 对象字段之列入非空
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);

		// 取消默认转换timestamps形式
		objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

		// 忽略空bean转json的错误
		objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

		// 忽略在json字符串中存在,但是在java对象中不存在对应属性的情况
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		// 启用时间模块
		objectMapper.registerModule(javaTimeModule);
		javaTimeModule.addSerializer(Instant.class,
				new InstantCustomSerializer(DateTimeFormatter.ofPattern(JSON_STANDARD_FORMAT)));

		// 对象字段之列入所有字段
		allFieldMapper = BeanUtil.copyProperties(objectMapper, ObjectMapper.class);
		allFieldMapper.setSerializationInclusion(Include.ALWAYS);
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
			return obj instanceof String ? (String) obj : mapper.writeValueAsString(obj);
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
			return obj instanceof String ? (String) obj
					: objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
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
		if (ZtkynStringUtil.isBlank(str) || clazz == null) {
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
	 * @param beanType
	 * @param <T>
	 * @return
	 */
	public static <T> List<T> json2List(String jsonData, Class<T> beanType) {
		JavaType javaType = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, beanType);

		try {
			return objectMapper.readValue(jsonData, javaType);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * string转object 用于转为 map
	 * @param jsonData
	 * @param keyType
	 * @param valueType
	 * @param <K>
	 * @param <V>
	 * @return
	 */
	public static <K, V> Map<K, V> json2Map(String jsonData, Class<K> keyType, Class<V> valueType) {
		JavaType javaType = objectMapper.getTypeFactory().constructMapType(HashMap.class, keyType, valueType);

		try {
			return objectMapper.readValue(jsonData, javaType);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	static class InstantCustomSerializer extends JsonSerializer<Instant> {

		private final DateTimeFormatter format;

		public InstantCustomSerializer(DateTimeFormatter formatter) {
			this.format = formatter;
		}

		@Override
		public void serialize(Instant instant, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
				throws IOException {
			if (instant == null) {
				return;
			}
			String jsonValue = format.format(instant.atZone(ZoneId.systemDefault()));
			jsonGenerator.writeString(jsonValue);
		}

	}

}
