package study.oauth2.user.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProfileResponseDto {

	private String nickname;
	private String profileImage;

	public static ProfileResponseDto of(String nickname, String profileImage) {
		return ProfileResponseDto.builder()
			.nickname(nickname)
			.profileImage(profileImage)
			.build();
	}
}
