package org.example.test.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

	private static final Logger logger = LoggerFactory.getLogger(MainController.class);

	@GetMapping("/login")
	public String login() {
		logger.info("Accessing the login page");
		return "home/login";
	}

	@GetMapping("/index")
	public String home() {
		logger.info("Accessing the home page");
		return "home/index";
	}
}
