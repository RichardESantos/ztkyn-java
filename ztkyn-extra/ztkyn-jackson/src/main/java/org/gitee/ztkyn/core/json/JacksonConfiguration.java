package org.gitee.ztkyn.core.json;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.datatype.eclipsecollections.EclipseCollectionsModule;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.gitee.ztkyn.core.date.DateStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author richard
 * @version 1.0
 * @description Jackson 配置类
 * @date 2023/3/8 11:15
 */
class JacksonConfiguration {

	private static final Logger logger = LoggerFactory.getLogger(JacksonConfiguration.class);

	public static void configuration(ObjectMapper objectMapper) {
		final JavaTimeModule javaTimeModule = new JavaTimeModule();

		// 建立Json操作中的日期格式
		final String JSON_STANDARD_FORMAT = DateStyle.yyyy_MM_dd_HH_mm_ss.getFormatter();
		// 对象字段之列入非空
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);

		// 取消默认转换timestamps形式
		objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

		// 忽略空bean转json的错误
		objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

		// 忽略在json字符串中存在,但是在java对象中不存在对应属性的情况
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		// 启用时间模块,guava,eclipse-collection
		objectMapper.registerModule(javaTimeModule)
			.registerModule(new GuavaModule())
			.registerModule(new EclipseCollectionsModule());
		javaTimeModule.addSerializer(Instant.class,
				new InstantCustomSerializer(DateTimeFormatter.ofPattern(JSON_STANDARD_FORMAT)));
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
