package study.oauth2.user.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import study.oauth2.user.domain.entity.Profile;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProfileDto {

	@NotNull(message = "Nickname cannot be null")
	@Email(message = "Nickname should be valid")
	private String nickname;
	private String profileImage;

	public static Profile toEntity(Long userId, ProfileDto profileDto) {
		return Profile.builder()
			.userId(userId)
			.nickname(profileDto.getNickname())
			.profileImage(profileDto.getProfileImage())
			.build();
	}
}
