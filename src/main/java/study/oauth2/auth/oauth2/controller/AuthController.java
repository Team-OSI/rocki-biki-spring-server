package study.oauth2.auth.oauth2.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import study.oauth2.auth.local.dto.SignRequest;
import study.oauth2.user.service.UserService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

	private final UserService userService;

	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignRequest signRequest) {
		userService.registerUser(signRequest.getEmail(), signRequest.getPassword());
		return ResponseEntity.ok("User registered successfully");
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@Valid @RequestBody SignRequest signRequest) {
		userService.login(signRequest);
		return ResponseEntity.ok("User login successfully");
	}
}
