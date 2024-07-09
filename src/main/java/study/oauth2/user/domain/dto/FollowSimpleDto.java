package study.oauth2.user.domain.dto;

import com.querydsl.core.annotations.QueryProjection;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FollowSimpleDto {

	private Long userId;
	private String nickname;
	private String email;
	private String profileImage;


	@QueryProjection
	public FollowSimpleDto(Long userId, String nickname, String email, String profileImage) {
		this.userId = userId;
		this.nickname = nickname;
		this.email = email;
		this.profileImage = profileImage;
	}
}