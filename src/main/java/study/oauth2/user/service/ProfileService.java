package study.oauth2.user.service;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import study.oauth2.S3Image.service.S3ImageService;
import study.oauth2.user.domain.dto.ProfileDto;
import study.oauth2.user.domain.dto.ProfileResponseDto;
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
	private final S3ImageService s3ImageService;

	@Transactional
	public void saveUserProfile(String email, String nickname, MultipartFile profileImage) {
		User user = userRepository.findByEmail(email)
			.orElseThrow(() -> new UsernameNotFoundException("User not found"));
		String imagePath = null;
		if (profileImage != null) {
			imagePath = s3ImageService.upload(profileImage);
		}
		Profile profile = ProfileDto.toEntity(user.getId(), nickname, imagePath);
		profileRepository.save(profile);
		user.setProfile(profile);
	}

	@Transactional
	public void updateUserProfile(String email, String nickname, MultipartFile profileImage) {
		User user = userRepository.findByEmailWithProfile(email)
			.orElseThrow(() -> new UsernameNotFoundException("User not found"));
		String updateImage = s3ImageService.update(user.getProfile().getProfileImage(), profileImage);
		user.getProfile().update(nickname, updateImage);

	}

	public ProfileResponseDto getUserProfile(String userEmail) {
		User user = userRepository.findByEmailWithProfile(userEmail)
			.orElseThrow(() -> new UsernameNotFoundException("User not found"));
		if (user.getProfile() == null) {
			throw new UsernameNotFoundException("Profile not found");
		}
		return ProfileResponseDto.of(user.getProfile().getNickname(), user.getProfile().getProfileImage());
	}
}
