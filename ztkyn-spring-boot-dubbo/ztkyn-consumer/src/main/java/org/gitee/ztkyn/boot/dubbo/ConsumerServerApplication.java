package org.gitee.ztkyn.boot.dubbo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author richard
 * @date 2023-07-03 11:13
 * @description ServerApplication
 * @version 1.0.0
 */
@EnableDubbo
@SpringBootApplication
public class ConsumerServerApplication {

	private static final Logger logger = LoggerFactory.getLogger(ConsumerServerApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(ConsumerServerApplication.class, args);
	}

}
