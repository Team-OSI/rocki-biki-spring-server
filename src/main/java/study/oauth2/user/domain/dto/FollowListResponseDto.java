package study.oauth2.user.domain.dto;

import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FollowListResponseDto {
	private List<FollowSimpleDto> following;
	private List<FollowSimpleDto> follower;

	public static FollowListResponseDto of(List<FollowSimpleDto> following, List<FollowSimpleDto> follower) {
		return new FollowListResponseDto(following, follower);
	}
}
