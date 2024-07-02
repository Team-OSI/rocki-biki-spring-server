package study.oauth2.oauth2.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import study.oauth2.dto.SignUpRequest;
import study.oauth2.user.domain.entity.User;
import study.oauth2.user.service.UserService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

	private final UserService userService;

	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@RequestBody SignUpRequest signUpRequest) {
		User user = userService.registerUser(
			signUpRequest.getEmail(),
			signUpRequest.getPassword()
		);
		return ResponseEntity.ok("User registered successfully");
	}
}
