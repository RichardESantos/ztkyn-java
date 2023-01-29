package org.gitee.ztkyn.web;

import java.net.SocketException;

import org.gitee.ztkyn.core.http.IpUtil;
import org.gitee.ztkyn.core.string.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

/**
 * @author whty
 * @version 1.0
 * @description
 * @date 2023/1/18 14:41
 */
@SpringBootApplication
public class ZtkynApplication {

	private static final Logger logger = LoggerFactory.getLogger(ZtkynApplication.class);

	public static void main(String[] args) throws SocketException {
		ConfigurableApplicationContext applicationContext = SpringApplication.run(ZtkynApplication.class, args);
		logger.info("项目启动成功");
		IpUtil.getLocalIp4Address().ifPresent(inet4Address -> {
			Environment env = applicationContext.getEnvironment();
			String ip = inet4Address.toString().replaceAll("/", "");
			String port = env.getProperty("local.server.port");
			String path = env.getProperty("server.servlet.context-path");
			if (StringUtil.isBlank(path)) {
				path = "";
			}
			logger.info("\n\t----------------------------------------------------------\n\t"
					+ "Application  is running! Access URLs:\n\t" + "Local访问网址: \t\thttp://localhost:" + port + path
					+ "\n\t" + "External访问网址: \thttp://" + ip + ":" + port + path + "\n\t"
					+ "文档访问网址: \t\thttp://localhost" + ":" + port + path + "/doc.html\n\t"
					+ "----------------------------------------------------------");
		});

	}

}
