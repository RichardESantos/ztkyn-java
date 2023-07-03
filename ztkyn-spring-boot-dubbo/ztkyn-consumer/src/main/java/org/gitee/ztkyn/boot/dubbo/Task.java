package org.gitee.ztkyn.boot.dubbo;

import java.util.Date;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import org.apache.dubbo.config.annotation.DubboReference;
import org.gitee.ztkyn.boot.dubbo.service.DemoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author richard
 * @date 2023-07-03 11:16
 * @description Task
 * @version 1.0.0
 */
@Component
public class Task implements CommandLineRunner {

	private static final Logger logger = LoggerFactory.getLogger(Task.class);

	@DubboReference
	private DemoService demoService;

	@Override
	public void run(String... args) throws Exception {
		String result = demoService.sayHello("world");
		System.out.println("Receive result ======> " + result);

		new Thread(() -> {
			while (true) {
				try {
					Thread.sleep(1000);
					System.out.println(new Date() + " Receive result ======> " + demoService.sayHello("world"));
				}
				catch (InterruptedException e) {
					e.printStackTrace();
					Thread.currentThread().interrupt();
				}
			}
		}).start();
	}

}
