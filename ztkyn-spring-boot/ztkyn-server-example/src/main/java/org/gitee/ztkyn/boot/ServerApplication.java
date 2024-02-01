package org.gitee.ztkyn.boot;

import java.net.SocketException;
import java.text.MessageFormat;
import java.util.StringJoiner;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import org.gitee.ztkyn.core.http.IpUtil;
import org.gitee.ztkyn.core.string.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
public class ServerApplication extends SpringBootServletInitializer implements ApplicationRunner {

	private static final Logger logger = LoggerFactory.getLogger(ServerApplication.class);

	public static void main(String[] args) throws SocketException {
		ConfigurableApplicationContext applicationContext = SpringApplication.run(ServerApplication.class, args);
		Environment env = applicationContext.getEnvironment();
		String port = env.getProperty("local.server.port");
		String path = env.getProperty("server.servlet.context-path");
		if (StringUtil.isBlank(path)) {
			path = "";
		}
		StringJoiner accessUrl = new StringJoiner("\n\t");
		StringJoiner docUrl = new StringJoiner("\n\t");
		String finalPath = path;
		IpUtil.getLocalIp4AddressFromNetworkInterface().forEach(inet4Address -> {
			String ip = inet4Address.getHostAddress();
			accessUrl.add(MessageFormat.format("http://{0}:{1}{2}", ip, port, finalPath));
			docUrl.add(MessageFormat.format("http://{0}:{1}{2}/doc.html", ip, port, finalPath));
		});
		String info = """
				项目启动成功
				----------------------------------------------------------
					Application  is running!
					Access URLs:
					{0}
					Doc URLs:
					{1}
				----------------------------------------------------------
				""";
		logger.info(MessageFormat.format(info, accessUrl.toString(), docUrl.toString()));
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(ServerApplication.class);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
	}

}