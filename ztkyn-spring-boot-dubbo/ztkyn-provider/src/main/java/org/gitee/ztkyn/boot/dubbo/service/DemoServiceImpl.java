package org.gitee.ztkyn.boot.dubbo.service;

import org.apache.dubbo.config.annotation.DubboService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author richard
 * @date 2023-07-03 11:08
 * @description DemoServiceImpl
 * @version 1.0.0
 */
@DubboService
public class DemoServiceImpl implements DemoService {

	private static final Logger logger = LoggerFactory.getLogger(DemoServiceImpl.class);

	@Override
	public String sayHello(String name) {
		return "Hello " + name;
	}

}
