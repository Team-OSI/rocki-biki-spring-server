package study.oauth2.user.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import study.oauth2.auth.dto.SignRequest;
import study.oauth2.exception.EmailAlreadyExistsException;
import study.oauth2.oauth2.user.OAuth2Provider;
import study.oauth2.user.domain.entity.Auth;
import study.oauth2.user.domain.entity.User;
import study.oauth2.user.repository.AuthRepository;
import study.oauth2.user.repository.UserRepository;

@Slf4j
@Lazy
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final AuthRepository authRepository;
	private final PasswordEncoder passwordEncoder;

	@Transactional
	public void registerUser(String email, String password) {
		if (userRepository.existsByEmail(email)) {
			throw new EmailAlreadyExistsException("Email already exists");
		}

		Auth auth = Auth.create(passwordEncoder.encode(password));
		auth = authRepository.save(auth);

		User user = User.create(email, auth, OAuth2Provider.LOCAL);
		userRepository.save(user);
	}

	@Transactional
	public void login(SignRequest signRequest) {
		User user = userRepository.findByEmailWithAuth(signRequest.getEmail())
			.orElseThrow(() -> new UsernameNotFoundException("User not found"));
		if (!passwordEncoder.matches(signRequest.getPassword(), user.getAuth().getPassword())) {
			throw new IllegalArgumentException("Invalid username or password");
		}

		List<GrantedAuthority> authorities = new ArrayList<>();
		// 기본 역할 설정
		authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

		Authentication authentication = new UsernamePasswordAuthenticationToken(user.getId(), user.getAuth().getPassword(), authorities);
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

	@Transactional
	public void findOrCreateUser(String email, OAuth2Provider provider) {
		userRepository.findByEmail(email)
			.orElseGet(() -> createNewUser(email, provider));
	}

	private User createNewUser(String email, OAuth2Provider provider) {
		User newUser = User.create(email, null, provider);
		return userRepository.save(newUser);
	}
}
