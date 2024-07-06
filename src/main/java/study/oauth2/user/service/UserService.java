package study.oauth2.user.service;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import study.oauth2.dto.SignUpRequest;
import study.oauth2.oauth2.user.OAuth2Provider;
import study.oauth2.user.domain.dto.FollowingRequestDto;
import study.oauth2.user.domain.dto.ProfileDto;
import study.oauth2.user.domain.entity.Auth;
import study.oauth2.user.domain.entity.Follow;
import study.oauth2.user.domain.entity.Profile;
import study.oauth2.user.domain.entity.User;
import study.oauth2.user.repository.AuthRepository;
import study.oauth2.user.repository.FollowRepository;
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
	private final FollowRepository followRepository;

	@Transactional
	public User registerUser(SignUpRequest signUpRequest) {
		if (userRepository.existsByEmail(signUpRequest.getEmail())) {
			throw new RuntimeException("Email already exists");
		}

		Auth auth = Auth.create(passwordEncoder.encode(signUpRequest.getPassword()));
		auth = authRepository.save(auth);

		User user = User.create(signUpRequest.getEmail(), auth, OAuth2Provider.LOCAL);
		return userRepository.save(user);
	}

	@Transactional
	public User registerSocialUser(String email, OAuth2Provider provider) {

		if (userRepository.existsByEmail(email)) {
			throw new RuntimeException("Email already exists");
		}

		User user = User.create(email, null, provider);

		return userRepository.save(user);
	}

	// OneToOne 조인 시 N + 1 문제를 해결하기 위해 양방향 매핑이 아닌 Id로 매핑
	@Transactional
	public void saveUserProfile(String email, ProfileDto profileDto) {
		User user = userRepository.findByEmail(email)
			.orElseThrow(() -> new UsernameNotFoundException("User not found"));
		Profile profile = ProfileDto.toEntity(user.getId(), profileDto);
		profileRepository.save(profile);
		user.setProfile(profile);
	}

	public Boolean existsByEmail(String email) throws UsernameNotFoundException {
		return userRepository.existsByEmail(email);
	}

	@Transactional
	public void followingUser(FollowingRequestDto followingRequestDto) {
		User user = userRepository.findByEmail(followingRequestDto.getFromUser())
			.orElseThrow(() -> new UsernameNotFoundException("User not found"));
		User followingUser = userRepository.findByEmail(followingRequestDto.getToUser())
			.orElseThrow(() -> new UsernameNotFoundException("Following user not found"));
		followRepository.save(Follow.create(user.getId(), followingUser.getId()));
	}
}
