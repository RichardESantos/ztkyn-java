package org.gitee.ztkyn.web.common.config;

import java.time.Duration;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

/**
 * @author whty
 * @version 1.0
 * @description redis 配置类
 * @date 2023/3/8 16:26
 */
@EnableCaching
@ConditionalOnClass(RedisOperations.class)
@ConfigurationProperties(prefix = "spring.cache.redis")
@Component
public class RedisConfiguration {

	private static final Logger logger = LoggerFactory.getLogger(RedisConfiguration.class);

	private Duration timeToLive = Duration.ZERO;

	public void setTimeToLive(Duration timeToLive) {
		this.timeToLive = timeToLive;
	}

	@Bean
	public Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
		objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL);

		Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(
				objectMapper, Object.class);
		return jackson2JsonRedisSerializer;
	}

	@Bean
	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
		RedisTemplate<String, Object> template = new RedisTemplate<>();
		// Key HashKey使用String序列化
		template.setKeySerializer(RedisSerializer.string());
		template.setHashKeySerializer(RedisSerializer.string());

		// Value HashValue使用Jackson2JsonRedisSerializer序列化
		template.setValueSerializer(jackson2JsonRedisSerializer());
		template.setHashValueSerializer(jackson2JsonRedisSerializer());

		template.setConnectionFactory(factory);
		return template;
	}

}
