package study.oauth2.game.repository.custom;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import study.oauth2.game.domain.dto.ResultPagingDto;
import study.oauth2.game.domain.entity.GameResult;
import study.oauth2.user.domain.dto.FollowSimpleDto;

public interface CustomGameResultRepository {

	Page<GameResult> findAllGameResultPage(Pageable pageable, ResultPagingDto resultPagingDto);

}
