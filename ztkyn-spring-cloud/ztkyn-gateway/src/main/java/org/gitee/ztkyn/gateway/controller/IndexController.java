package org.gitee.ztkyn.gateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping
@RestController
public class IndexController {

	@GetMapping("/index")
	public String index() {
		return "Hello world";
	}

}
