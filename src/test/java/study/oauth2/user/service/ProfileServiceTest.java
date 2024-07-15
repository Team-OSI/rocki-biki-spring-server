package study.oauth2.user.service;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import study.oauth2.S3.service.S3Service;
import study.oauth2.user.domain.dto.ProfileResponseDto;
import study.oauth2.user.domain.entity.FileType;
import study.oauth2.user.domain.entity.Profile;
import study.oauth2.user.domain.entity.User;
import study.oauth2.user.repository.ProfileRepository;
import study.oauth2.user.repository.UserRepository;

class ProfileServiceTest {

	@Mock
	private ProfileRepository profileRepository;

	@Mock
	private UserRepository userRepository;

	@Mock
	private S3Service s3Service;

	@InjectMocks
	private ProfileService profileService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	@DisplayName("프로필 이미지 저장 성공")
	void saveUserProfile_Success() {
		// Given
		String email = "test@example.com";
		String nickname = "testNickname";
		MultipartFile profileImage = new MockMultipartFile("test.jpg", new byte[0]);

		User user = User.builder()
			.id(1L)
			.email(email)
			.build();

		when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
		when(s3Service.upload(any(MultipartFile.class), eq(FileType.IMAGE))).thenReturn("test-image-path.jpg");
		when(profileRepository.save(any(Profile.class))).thenAnswer(invocation -> invocation.getArgument(0));

		// When
		ProfileResponseDto result = profileService.saveUserProfile(email, nickname, profileImage);

		// Then
		assertNotNull(result);
		assertEquals(nickname, result.getNickname());
		assertEquals("test-image-path.jpg", result.getProfileImage());

		verify(userRepository).findByEmail(email);
		verify(s3Service).upload(profileImage, FileType.IMAGE);
		verify(profileRepository).save(any(Profile.class));
	}

	@Test
	@DisplayName("프로필 이미지 저장 실패: 사용자 없음")
	void saveUserProfile_UserNotFound() {
		// Given
		String email = "nonexistent@example.com";
		when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

		// When & Then
		assertThrows(UsernameNotFoundException.class, () ->
			profileService.saveUserProfile(email, "nickname", null));

		verify(userRepository).findByEmail(email);
		verifyNoInteractions(s3Service, profileRepository);
	}

	@Test
	@DisplayName("프로필 없이 저장 성공")
	void saveUserProfile_NoProfileImage() {
		// Given
		String email = "test@example.com";
		String nickname = "testNickname";

		User user = User.builder()
			.id(1L)
			.email(email)
			.build();

		when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
		when(profileRepository.save(any(Profile.class))).thenAnswer(invocation -> invocation.getArgument(0));

		// When
		ProfileResponseDto result = profileService.saveUserProfile(email, nickname, null);

		// Then
		assertNotNull(result);
		assertEquals(nickname, result.getNickname());
		assertNull(result.getProfileImage());

		verify(userRepository).findByEmail(email);
		verifyNoInteractions(s3Service);
		verify(profileRepository).save(any(Profile.class));
	}

	@Test
	@DisplayName("프로필 이미지 수정 성공: 이미지 변경")
	void updateUserProfile_Success_WithImage() {
		// Given
		String email = "test@example.com";
		String nickname = "newNickname";
		MultipartFile profileImage = new MockMultipartFile("test.jpg", new byte[0]);

		User user = User.builder()
			.id(1L)
			.email(email)
			.build();

		Profile profile = Profile.builder()
			.nickname("oldNickname")
			.profileImage("old-image-path.jpg")
			.build();

		user.setProfile(profile);

		when(userRepository.findByEmailWithProfile(email)).thenReturn(Optional.of(user));
		when(s3Service.update(anyString(), any(MultipartFile.class), eq(FileType.IMAGE)))
			.thenReturn("new-image-path.jpg");

		// When
		ProfileResponseDto result = profileService.updateUserProfile(email, nickname, profileImage);

		// Then
		assertNotNull(result);
		assertEquals(nickname, result.getNickname());
		assertEquals("new-image-path.jpg", result.getProfileImage());

		verify(userRepository).findByEmailWithProfile(email);
		verify(s3Service).update("old-image-path.jpg", profileImage, FileType.IMAGE);

		// 추가: User 엔티티의 변경사항이 올바르게 적용되었는지 확인
		assertEquals(nickname, user.getProfile().getNickname());
		assertEquals("new-image-path.jpg", user.getProfile().getProfileImage());

		// 추가: 변경된 User 엔티티가 저장되었는지 확인
		assertEquals(nickname, user.getProfile().getNickname());
		assertEquals("new-image-path.jpg", user.getProfile().getProfileImage());
	}

	@Test
	@DisplayName("프로필 이미지 수정 성공: 이미지 변경하지 않음")
	void updateUserProfile_Success_WithoutImage() {
		// Given
		String email = "test@example.com";
		String nickname = "newNickname";

		User user = User.builder()
			.id(1L)
			.email(email)
			.build();

		Profile profile = Profile.builder()
			.nickname("oldNickname")
			.profileImage("old-image-path.jpg")
			.build();

		user.setProfile(profile);

		when(userRepository.findByEmailWithProfile(email)).thenReturn(Optional.of(user));
		when(s3Service.update(anyString(), isNull(), eq(FileType.IMAGE)))
			.thenReturn("old-image-path.jpg");

		// When
		ProfileResponseDto result = profileService.updateUserProfile(email, nickname, null);

		// Then
		assertNotNull(result);
		assertEquals(nickname, result.getNickname());
		assertEquals("old-image-path.jpg", result.getProfileImage());

		verify(userRepository).findByEmailWithProfile(email);
		verify(s3Service).update("old-image-path.jpg", null, FileType.IMAGE);
	}

	@Test
	@DisplayName("프로필 이미지 수정 실패: 사용자 없음")
	void updateUserProfile_UserNotFound() {
		// Given
		String email = "nonexistent@example.com";
		when(userRepository.findByEmailWithProfile(email)).thenReturn(Optional.empty());

		// When & Then
		assertThrows(UsernameNotFoundException.class, () ->
			profileService.updateUserProfile(email, "nickname", null));

		verify(userRepository).findByEmailWithProfile(email);
		verifyNoInteractions(s3Service);
	}

	@Test
	@DisplayName("새로운 사운드 URL 추가 성공")
	void addUserSound_Success_NewSound() {
		// Given
		String email = "test@example.com";
		MultipartFile sound = new MockMultipartFile("test.mp3", new byte[0]);
		String oldUrl = null;
		String newSoundPath = "new-sound-path.mp3";

		Profile profile = Profile.builder()
			.id(1L)
			.userId(1L)
			.nickname("testUser")
			.userSoundUrls(new ArrayList<>())
			.build();

		User user = User.builder()
			.id(1L)
			.email(email)
			.profile(profile)
			.build();

		when(userRepository.findByEmailWithProfile(email)).thenReturn(Optional.of(user));
		when(s3Service.update(oldUrl, sound, FileType.SOUND)).thenReturn(newSoundPath);

		// When
		// profileService.addUserSound(email, sound, oldUrl);

		// Then
		verify(userRepository).findByEmailWithProfile(email);
		verify(s3Service).update(oldUrl, sound, FileType.SOUND);
		assertTrue(user.getProfile().getUserSoundUrls().contains(newSoundPath));
		assertEquals(1, user.getProfile().getUserSoundUrls().size());
	}

	@Test
	@DisplayName("기존 사운드 URL 목록에 새 URL 추가 성공")
	void addUserSound_Success_AddToExistingList() {
		// Given
		String email = "test@example.com";
		MultipartFile sound = new MockMultipartFile("test.mp3", new byte[0]);
		String oldUrl = null;
		String newSoundPath = "new-sound-path.mp3";
		List<String> existingSoundUrls = new ArrayList<>(Arrays.asList("existing-sound-1.mp3", "existing-sound-2.mp3"));

		Profile profile = Profile.builder()
			.id(1L)
			.userId(1L)
			.nickname("testUser")
			.userSoundUrls(existingSoundUrls)
			.build();

		User user = User.builder()
			.id(1L)
			.email(email)
			.profile(profile)
			.build();

		when(userRepository.findByEmailWithProfile(email)).thenReturn(Optional.of(user));
		when(s3Service.update(oldUrl, sound, FileType.SOUND)).thenReturn(newSoundPath);

		// When
		// profileService.addUserSound(email, sound, oldUrl);

		// Then
		verify(userRepository).findByEmailWithProfile(email);
		verify(s3Service).update(oldUrl, sound, FileType.SOUND);
		assertTrue(user.getProfile().getUserSoundUrls().contains(newSoundPath));
		assertEquals(3, user.getProfile().getUserSoundUrls().size());
		assertTrue(user.getProfile().getUserSoundUrls().containsAll(Arrays.asList("existing-sound-1.mp3", "existing-sound-2.mp3", newSoundPath)));
	}

	@Test
	@DisplayName("사운드 파일이 null일 때 처리")
	void addUserSound_NullSound() {
		// Given
		String email = "test@example.com";
		String oldUrl = null;

		Profile profile = Profile.builder()
			.id(1L)
			.userId(1L)
			.nickname("testUser")
			.userSoundUrls(new ArrayList<>())
			.build();

		User user = User.builder()
			.id(1L)
			.email(email)
			.profile(profile)
			.build();

		when(userRepository.findByEmailWithProfile(email)).thenReturn(Optional.of(user));

		// When
		// profileService.addUserSound(email, null, oldUrl);

		// Then
		verify(userRepository).findByEmailWithProfile(email);
		verifyNoInteractions(s3Service);
		assertFalse(user.getProfile().getUserSoundUrls().contains(null));
		assertEquals(0, user.getProfile().getUserSoundUrls().size());
	}

	@Test
	@DisplayName("사용자를 찾을 수 없을 때 예외 처리")
	void addUserSound_UserNotFound() {
		// Given
		String email = "nonexistent@example.com";
		MultipartFile sound = new MockMultipartFile("test.mp3", new byte[0]);
		String oldUrl = null;

		when(userRepository.findByEmailWithProfile(email)).thenReturn(Optional.empty());

		// When & Then
		assertThrows(UsernameNotFoundException.class, () ->
			// profileService.addUserSound(email, sound, oldUrl)
			);

		verify(userRepository).findByEmailWithProfile(email);
		verifyNoInteractions(s3Service);
	}


}

