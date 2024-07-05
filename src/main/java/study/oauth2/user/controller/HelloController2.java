package study.oauth2.user.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController2 {

	@GetMapping("/hello")
	public String hello() {
		return "Hello, World!";
	}
}
