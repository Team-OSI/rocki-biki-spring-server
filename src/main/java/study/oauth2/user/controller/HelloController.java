package study.oauth2.user.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
public class HelloController {

	@GetMapping("/hello")
	public String hello() {
		return "Hello, World!";
	}
}
