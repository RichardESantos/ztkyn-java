package org.gitee.ztkyn.web.common.config;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.hibernate.validator.HibernateValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @author whty
 * @version 1.0
 * @description
 * @date 2023/1/29 16:28
 */
@Component
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
