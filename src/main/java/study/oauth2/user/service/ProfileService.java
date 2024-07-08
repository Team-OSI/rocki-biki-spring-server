package study.oauth2.user.service;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import study.oauth2.user.domain.dto.ProfileDto;
import study.oauth2.user.domain.entity.Profile;
import study.oauth2.user.domain.entity.User;
import study.oauth2.user.repository.ProfileRepository;
import study.oauth2.user.repository.UserRepository;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProfileService {

	private final ProfileRepository profileRepository;
	private final UserRepository userRepository;

	// OneToOne 조인 시 N + 1 문제를 해결하기 위해 양방향 매핑이 아닌 Id로 매핑
	@Transactional
	public void saveUserProfile(String email, ProfileDto profileDto) {
		User user = userRepository.findByEmail(email)
			.orElseThrow(() -> new UsernameNotFoundException("User not found"));
		Profile profile = ProfileDto.toEntity(user.getId(), profileDto);
		profileRepository.save(profile);
		user.setProfile(profile);
	}

}
