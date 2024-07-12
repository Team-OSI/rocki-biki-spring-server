package study.oauth2.user.domain.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Builder;

@Builder
public class UserInfoResponseDto {

	private String nickname;
	private String profileImage;
	@Builder.Default
	private List<String> userSoundUrls = new ArrayList<>();

	public static UserInfoResponseDto of(String nickname, String profileImage, List<String> userSoundUrls) {
		return UserInfoResponseDto.builder()
			.nickname(nickname)
			.profileImage(profileImage)
			.userSoundUrls(userSoundUrls)
			.build();
	}

}
