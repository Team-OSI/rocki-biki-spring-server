package study.oauth2.user.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import study.oauth2.S3.service.S3Service;
import study.oauth2.user.domain.dto.AudioUploadDTO;
import study.oauth2.user.domain.dto.ProfileDto;
import study.oauth2.user.domain.dto.ProfileResponseDto;
import study.oauth2.user.domain.dto.UserInfoResponseDto;
import study.oauth2.user.domain.entity.FileType;
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
	private final S3Service s3Service;

	@Transactional
	public ProfileResponseDto saveUserProfile(String email, String nickname, MultipartFile profileImage) {
		User user = userRepository.findByEmail(email)
			.orElseThrow(() -> new UsernameNotFoundException("User not found"));
		String imagePath = null;
		if (profileImage != null) {
			imagePath = s3Service.upload(profileImage, FileType.IMAGE);
		}
		Profile profile = ProfileDto.toEntity(user.getId(), nickname, imagePath);
		profileRepository.save(profile);
		user.setProfile(profile);
		return ProfileResponseDto.of(profile.getNickname(), profile.getProfileImage());
	}

	@Transactional
	public ProfileResponseDto updateUserProfile(String email, String nickname, MultipartFile profileImage) {
		User user = userRepository.findByEmailWithProfile(email)
			.orElseThrow(() -> new UsernameNotFoundException("User not found"));
		String updateImage = s3Service.update(user.getProfile().getProfileImage(), profileImage, FileType.IMAGE);
		user.getProfile().update(nickname, updateImage);
		return ProfileResponseDto.of(user.getProfile().getNickname(), user.getProfile().getProfileImage());
	}

	public ProfileResponseDto getUserProfile(String userEmail) {
		User user = userRepository.findByEmailWithProfile(userEmail)
			.orElseThrow(() -> new UsernameNotFoundException("User not found"));
		if (user.getProfile() == null) {
			throw new UsernameNotFoundException("Profile not found");
		}
		return ProfileResponseDto.of(user.getProfile().getNickname(), user.getProfile().getProfileImage());
	}

	@Transactional
	public void addUserSound(String email, AudioUploadDTO audioUploadDTO) {
		User user = userRepository.findByEmailWithProfile(email)
			.orElseThrow(() -> new UsernameNotFoundException("User not found"));
		audioUploadDTO.getAudios().forEach(audio -> {
			MultipartFile audioFile = (MultipartFile) audio.get(0);
			String audioUrl = (String) audio.get(1);
			if (audioFile != null) {
				if (audioUrl.isEmpty()) {
					log.info("=========upload=========");
					String url = s3Service.upload(audioFile, FileType.SOUND);
					user.getProfile().addSoundUrl(url);
				} else if (user.getProfile().getUserSoundUrls().contains(audioUrl)) {
					log.info("=========update=========");
					String newUrl = s3Service.update(audioUrl, audioFile, FileType.SOUND);
					user.getProfile().updateSoundUrl(audioUrl, newUrl);
				}
			}
		});
	}

	public UserInfoResponseDto getUserInfo(String email) {
		User user = userRepository.findByEmailWithProfile(email)
			.orElseThrow(() -> new UsernameNotFoundException("User not found"));
		Profile profile = user.getProfile();
		return UserInfoResponseDto.of(profile.getNickname(), profile.getProfileImage(), profile.getUserSoundUrls());
	}

	@Transactional
	public void deleteUserSound(String email, String url) {
		User user = userRepository.findByEmailWithProfile(email)
			.orElseThrow(() -> new UsernameNotFoundException("User not found"));

		Profile profile = user.getProfile();
		if (profile == null) {
			throw new IllegalStateException("User profile not found");
		}

		List<String> soundUrls = profile.getUserSoundUrls();
		if (soundUrls == null || !soundUrls.contains(url)) {
			throw new IllegalArgumentException("Sound URL not found");
		}

		s3Service.deleteImageFromS3(url);
	}

	public List<String> getUserSound(String email) {
		User user = userRepository.findByEmailWithProfile(email)
			.orElseThrow(() -> new UsernameNotFoundException("User not found"));

		Profile profile = user.getProfile();
		return profile.getUserSoundUrls();


	}
}
