package org.gitee.ztkyn.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static java.lang.Thread.currentThread;

@RestController
@RequestMapping("/ttl")
public class TtlController {

	private static final Logger logger = LoggerFactory.getLogger(TtlController.class);

	@GetMapping("/ttl")
	public String ttl() {
		logger.info("           controller in: tid: " + currentThread().getId() + ", time: " + TIME.get());
		ttlService.ttl();
		asyncService.async();
		poolUseService.poolUse();
		return "ok";
	}

}
