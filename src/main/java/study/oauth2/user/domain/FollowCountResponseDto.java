package study.oauth2.user.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
public class FollowCountResponseDto {

	private Long followingCount;
	private Long followerCount;

	public static FollowCountResponseDto create(Long followingCount, Long followerCount) {
		return FollowCountResponseDto.builder()
			.followingCount(followingCount)
			.followerCount(followerCount)
			.build();
	}
}
