package org.gitee.ztkyn.boot.framework.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import me.ahoo.cosid.annotation.CosId;
import org.hibernate.validator.HibernateValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author richard
 * @date 2023-06-21 09:20
 * @description CoreConfg
 * @version 1.0.0
 */
@CosId
@EnableConfigurationProperties(ZtkynProperties.class)
@Configuration
public class CoreConfig {

	private static final Logger logger = LoggerFactory.getLogger(CoreConfig.class);

	/**
	 * Spring Validation默认会校验完所有字段，然后才抛出异常。可以通过一些简单的配置，开启Fail Fast模式，一旦校验失败就立即返回。
	 * @return
	 */
	@Bean
	public Validator validator() {
		ValidatorFactory validatorFactory = Validation.byProvider(HibernateValidator.class)
			.configure()
			.failFast(true)
			.buildValidatorFactory();
		return validatorFactory.getValidator();
	}

}
