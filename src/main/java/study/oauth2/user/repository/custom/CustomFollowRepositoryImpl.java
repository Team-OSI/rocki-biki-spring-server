package study.oauth2.user.repository.custom;

import static study.oauth2.user.domain.entity.QFollow.*;
import static study.oauth2.user.domain.entity.QProfile.*;
import static study.oauth2.user.domain.entity.QUser.*;

import java.util.List;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import study.oauth2.user.domain.dto.FollowSimpleDto;
import study.oauth2.user.domain.dto.QFollowSimpleDto;
import study.oauth2.user.domain.entity.QFollow;
import study.oauth2.user.domain.entity.QUser;

@RequiredArgsConstructor
public class CustomFollowRepositoryImpl implements CustomFollowRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public List<FollowSimpleDto> findAllFollowing(Long userId) {

		return queryFactory
			.select(new QFollowSimpleDto(
				user.id,
				profile.nickname,
				user.email,
				profile.profileImage))
			.from(follow)
			.join(user).on(follow.toUser.eq(user.id))
			.join(profile).on(user.id.eq(profile.userId))
			.where(follow.fromUser.eq(userId))
			.fetch();
	}

	@Override
	public List<FollowSimpleDto> findAllFollower(Long userId) {
		return queryFactory
			.select(new QFollowSimpleDto(
				user.id,
				profile.nickname,
				user.email,
				profile.profileImage))
			.from(follow)
			.join(user).on(follow.fromUser.eq(user.id))
			.join(profile).on(user.id.eq(profile.userId))
			.where(follow.toUser.eq(userId))
			.fetch();
	}
}
