package study.oauth2.user.service;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import study.oauth2.oauth2.user.OAuth2Provider;
import study.oauth2.user.domain.entity.Auth;
import study.oauth2.user.domain.entity.Profile;
import study.oauth2.user.domain.entity.User;
import study.oauth2.user.repository.AuthRepository;
import study.oauth2.user.repository.ProfileRepository;
import study.oauth2.user.repository.UserRepository;

@Slf4j
@Lazy
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final AuthRepository authRepository;
	private final ProfileRepository profileRepository;
	private final PasswordEncoder passwordEncoder;

	@Transactional
	public User registerUser(String email, String password) {
		if (userRepository.existsByEmail(email)) {
			throw new RuntimeException("Email already exists");
		}

		Auth auth = Auth.create(passwordEncoder.encode(password));
		auth = authRepository.save(auth);

		Profile profile = Profile.create();
		profile = profileRepository.save(profile);

		User user = User.create(email, auth, profile, OAuth2Provider.LOCAL);
		return userRepository.save(user);
	}

	@Transactional
	public User registerSocialUser(String email, String nickname, OAuth2Provider provider) {

		if (userRepository.existsByEmail(email)) {
			throw new RuntimeException("Email already exists");
		}

		Profile profile = Profile.create();
		profile = profileRepository.save(profile);
		log.info("provider: {}", provider);

		User user = User.create(email, null, profile, provider);
		return userRepository.save(user);
	}

	public Boolean existsByEmail(String email) throws UsernameNotFoundException {
		return userRepository.existsByEmail(email);
	}
}
