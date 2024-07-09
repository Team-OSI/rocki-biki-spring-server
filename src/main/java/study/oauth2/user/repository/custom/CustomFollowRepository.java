package study.oauth2.user.repository.custom;

import java.util.List;

import study.oauth2.user.domain.dto.FollowSimpleDto;

public interface CustomFollowRepository {

	List<FollowSimpleDto> findAllFollower(Long userId);
	List<FollowSimpleDto> findAllFollowing(Long userId);

}
