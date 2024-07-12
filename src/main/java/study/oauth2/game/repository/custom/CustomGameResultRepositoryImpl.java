package study.oauth2.game.repository.custom;

import static study.oauth2.game.domain.entity.QGameResult.*;
import static study.oauth2.user.domain.entity.QFollow.*;
import static study.oauth2.user.domain.entity.QProfile.*;
import static study.oauth2.user.domain.entity.QUser.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import study.oauth2.game.domain.dto.ResultPagingDto;
import study.oauth2.game.domain.entity.GameResult;
import study.oauth2.game.domain.entity.QGameResult;
import study.oauth2.user.domain.dto.FollowSimpleDto;
import study.oauth2.user.domain.dto.QFollowSimpleDto;

@RequiredArgsConstructor
public class CustomGameResultRepositoryImpl implements CustomGameResultRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public Page<GameResult> findAllGameResultPage(Pageable pageable, ResultPagingDto resultPagingDto) {
		List<GameResult> gameResults = queryFactory
			.selectFrom(gameResult)
			.leftJoin(gameResult.user, user)
			.leftJoin(user.profile, profile)
			.where(user.email.eq(resultPagingDto.getUserEmail()))
			.orderBy(getOrderSpecifier(resultPagingDto))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		JPAQuery<Long> countQuery = queryFactory
			.select(gameResult.count())
			.from(gameResult)
			.where(user.email.eq(resultPagingDto.getUserEmail()));

		return PageableExecutionUtils.getPage(gameResults, pageable, countQuery::fetchOne);

	}
	private OrderSpecifier<?> getOrderSpecifier(ResultPagingDto resultPagingDto) {
		PathBuilder<GameResult> entityPath = new PathBuilder<>(GameResult.class, "gameResult");
		if (resultPagingDto.getSort().equals("ACS")) {
			return new OrderSpecifier(Order.ASC, entityPath.get(resultPagingDto.getSortField()));
		} else {
			return new OrderSpecifier(Order.DESC, entityPath.get(resultPagingDto.getSortField()));
		}
	}
}
