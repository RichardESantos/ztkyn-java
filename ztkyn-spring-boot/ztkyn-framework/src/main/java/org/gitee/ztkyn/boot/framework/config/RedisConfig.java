package org.gitee.ztkyn.boot.framework.config;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.ssl.SslBundle;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.util.ReflectionUtils;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import jakarta.annotation.Resource;
import org.gitee.ztkyn.boot.framework.distribute.lock.DistributedLock;
import org.gitee.ztkyn.boot.framework.distribute.lock.impl.RedisDistributedLock;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.api.RedissonReactiveClient;
import org.redisson.api.RedissonRxClient;
import org.redisson.config.BaseConfig;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.redisson.config.SentinelServersConfig;
import org.redisson.config.SingleServerConfig;
import org.redisson.spring.data.connection.RedissonConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author richard
 * @date 2023-06-21 11:11
 * @description RedisConfig
 * @version 1.0.0
 */
@Configuration
@ConditionalOnProperty(prefix = "ztkyn.props.redis", name = "enable", havingValue = "true")
@AutoConfigureBefore(RedisAutoConfiguration.class)
@EnableConfigurationProperties({ ZtkynProperties.class, RedissonProperties.class, RedisProperties.class })
public class RedisConfig {

	private static final Logger logger = LoggerFactory.getLogger(RedisConfig.class);

	private static final String REDIS_PROTOCOL_PREFIX = "redis://";

	private static final String REDISS_PROTOCOL_PREFIX = "rediss://";

	@Resource
	private RedissonProperties redissonProperties;

	@Resource
	private RedisProperties redisProperties;

	@Resource
	private ApplicationContext ctx;

	@Resource
	private ZtkynProperties ztkynProperties;

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
	@ConditionalOnMissingBean(RedisConnectionFactory.class)
	public RedissonConnectionFactory redissonConnectionFactory(RedissonClient redisson) {
		return new RedissonConnectionFactory(redisson);
	}

	@Bean
	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
		RedisTemplate<String, Object> template = new RedisTemplate<>();
		// Key HashKey使用String序列化
		template.setKeySerializer(RedisSerializer.string());
		template.setHashKeySerializer(RedisSerializer.string());

		// Value HashValue使用Jackson2JsonRedisSerializer序列化
		template.setValueSerializer(jackson2JsonRedisSerializer());
		template.setHashValueSerializer(jackson2JsonRedisSerializer());

		template.setConnectionFactory(redisConnectionFactory);
		return template;
	}

	@Bean
	@Lazy
	@ConditionalOnMissingBean(RedissonReactiveClient.class)
	public RedissonReactiveClient redissonReactive(RedissonClient redisson) {
		return redisson.reactive();
	}

	@Bean
	@Lazy
	@ConditionalOnMissingBean(RedissonRxClient.class)
	public RedissonRxClient redissonRxJava(RedissonClient redisson) {
		return redisson.rxJava();
	}

	/**
	 * 分布式锁实现
	 * @param redisson
	 * @return
	 */
	@Bean
	@ConditionalOnBean(RedissonClient.class)
	public DistributedLock redisDistributedLock(RedissonClient redisson) {
		return new RedisDistributedLock(redisson);
	}

	@Bean(destroyMethod = "shutdown")
	@ConditionalOnMissingBean(RedissonClient.class)
	public RedissonClient redisson() throws IOException {
		Config config;
		Method clusterMethod = ReflectionUtils.findMethod(RedisProperties.class, "getCluster");
		Method usernameMethod = ReflectionUtils.findMethod(RedisProperties.class, "getUsername");
		Method timeoutMethod = ReflectionUtils.findMethod(RedisProperties.class, "getTimeout");
		Method connectTimeoutMethod = ReflectionUtils.findMethod(RedisProperties.class, "getConnectTimeout");
		Method clientNameMethod = ReflectionUtils.findMethod(RedisProperties.class, "getClientName");
		Object timeoutValue = ReflectionUtils.invokeMethod(timeoutMethod, redisProperties);
		String prefix = getPrefix();

		Integer timeout = null;
		if (timeoutValue instanceof Duration) {
			timeout = (int) ((Duration) timeoutValue).toMillis();
		}
		else if (timeoutValue != null) {
			timeout = (Integer) timeoutValue;
		}

		Integer connectTimeout = null;
		if (connectTimeoutMethod != null) {
			Object connectTimeoutValue = ReflectionUtils.invokeMethod(connectTimeoutMethod, redisProperties);
			if (connectTimeoutValue != null) {
				connectTimeout = (int) ((Duration) connectTimeoutValue).toMillis();
			}
		}
		else {
			connectTimeout = timeout;
		}

		String clientName = null;
		if (clientNameMethod != null) {
			clientName = (String) ReflectionUtils.invokeMethod(clientNameMethod, redisProperties);
		}

		String username = null;
		if (usernameMethod != null) {
			username = (String) ReflectionUtils.invokeMethod(usernameMethod, redisProperties);
		}

		if (redissonProperties.getConfig() != null) {
			try {
				config = Config.fromYAML(redissonProperties.getConfig());
			}
			catch (IOException e) {
				try {
					config = Config.fromJSON(redissonProperties.getConfig());
				}
				catch (IOException e1) {
					e1.addSuppressed(e);
					throw new IllegalArgumentException("Can't parse config", e1);
				}
			}
		}
		else if (redissonProperties.getFile() != null) {
			try {
				InputStream is = getConfigStream();
				config = Config.fromYAML(is);
			}
			catch (IOException e) {
				// trying next format
				try {
					InputStream is = getConfigStream();
					config = Config.fromJSON(is);
				}
				catch (IOException e1) {
					e1.addSuppressed(e);
					throw new IllegalArgumentException("Can't parse config", e1);
				}
			}
		}
		else if (redisProperties.getSentinel() != null) {
			Method nodesMethod = ReflectionUtils.findMethod(RedisProperties.Sentinel.class, "getNodes");
			Object nodesValue = ReflectionUtils.invokeMethod(nodesMethod, redisProperties.getSentinel());

			String[] nodes;
			if (nodesValue instanceof String) {
				nodes = convert(prefix, Arrays.asList(((String) nodesValue).split(",")));
			}
			else {
				nodes = convert(prefix, (List<String>) nodesValue);
			}

			config = new Config();
			SentinelServersConfig c = config.useSentinelServers()
				.setMasterName(redisProperties.getSentinel().getMaster())
				.addSentinelAddress(nodes)
				.setDatabase(redisProperties.getDatabase())
				.setUsername(username)
				.setPassword(redisProperties.getPassword())
				.setClientName(clientName);
			if (connectTimeout != null) {
				c.setConnectTimeout(connectTimeout);
			}
			if (connectTimeoutMethod != null && timeout != null) {
				c.setTimeout(timeout);
			}
			initSSL(c);
		}
		else if (clusterMethod != null && ReflectionUtils.invokeMethod(clusterMethod, redisProperties) != null) {
			Object clusterObject = ReflectionUtils.invokeMethod(clusterMethod, redisProperties);
			Method nodesMethod = ReflectionUtils.findMethod(clusterObject.getClass(), "getNodes");
			List<String> nodesObject = (List) ReflectionUtils.invokeMethod(nodesMethod, clusterObject);

			String[] nodes = convert(prefix, nodesObject);

			config = new Config();
			ClusterServersConfig c = config.useClusterServers()
				.addNodeAddress(nodes)
				.setUsername(username)
				.setPassword(redisProperties.getPassword())
				.setClientName(clientName);
			if (connectTimeout != null) {
				c.setConnectTimeout(connectTimeout);
			}
			if (connectTimeoutMethod != null && timeout != null) {
				c.setTimeout(timeout);
			}
			initSSL(c);
		}
		else {
			config = new Config();

			SingleServerConfig c = config.useSingleServer()
				.setAddress(prefix + redisProperties.getHost() + ":" + redisProperties.getPort())
				.setDatabase(redisProperties.getDatabase())
				.setUsername(username)
				.setPassword(redisProperties.getPassword())
				.setClientName(clientName);
			if (connectTimeout != null) {
				c.setConnectTimeout(connectTimeout);
			}
			if (connectTimeoutMethod != null && timeout != null) {
				c.setTimeout(timeout);
			}
			initSSL(c);
		}
		return Redisson.create(config);
	}

	private void initSSL(BaseConfig<?> config) {
		Method getSSLMethod = ReflectionUtils.findMethod(RedisProperties.class, "getSsl");
		if (getSSLMethod == null) {
			return;
		}

		RedisProperties.Ssl ssl = redisProperties.getSsl();
		if (ssl.getBundle() == null) {
			return;
		}

		ObjectProvider<SslBundles> provider = ctx.getBeanProvider(SslBundles.class);
		SslBundles bundles = provider.getIfAvailable();
		if (bundles == null) {
			return;
		}
		SslBundle b = bundles.getBundle(ssl.getBundle());
		if (b == null) {
			return;
		}
		config.setSslCiphers(b.getOptions().getCiphers());
		config.setSslProtocols(b.getOptions().getEnabledProtocols());
		config.setSslTrustManagerFactory(b.getManagers().getTrustManagerFactory());
		config.setSslKeyManagerFactory(b.getManagers().getKeyManagerFactory());
	}

	private String getPrefix() {
		String prefix = REDIS_PROTOCOL_PREFIX;
		Method isSSLMethod = ReflectionUtils.findMethod(RedisProperties.class, "isSsl");
		Method getSSLMethod = ReflectionUtils.findMethod(RedisProperties.class, "getSsl");
		if (isSSLMethod != null) {
			if ((Boolean) ReflectionUtils.invokeMethod(isSSLMethod, redisProperties)) {
				prefix = REDISS_PROTOCOL_PREFIX;
			}
		}
		else if (getSSLMethod != null) {
			Object ss = ReflectionUtils.invokeMethod(getSSLMethod, redisProperties);
			if (ss != null) {
				Method isEnabledMethod = ReflectionUtils.findMethod(ss.getClass(), "isEnabled");
				Boolean enabled = (Boolean) ReflectionUtils.invokeMethod(isEnabledMethod, ss);
				if (enabled) {
					prefix = REDISS_PROTOCOL_PREFIX;
				}
			}
		}
		return prefix;
	}

	private String[] convert(String prefix, List<String> nodesObject) {
		List<String> nodes = new ArrayList<>(nodesObject.size());
		for (String node : nodesObject) {
			if (!node.startsWith(REDIS_PROTOCOL_PREFIX) && !node.startsWith(REDISS_PROTOCOL_PREFIX)) {
				nodes.add(prefix + node);
			}
			else {
				nodes.add(node);
			}
		}
		return nodes.toArray(new String[0]);
	}

	private InputStream getConfigStream() throws IOException {
		org.springframework.core.io.Resource resource = ctx.getResource(redissonProperties.getFile());
		return resource.getInputStream();
	}

}
