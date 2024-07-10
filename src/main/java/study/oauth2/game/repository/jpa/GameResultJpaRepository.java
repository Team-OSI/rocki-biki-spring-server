package study.oauth2.game.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import study.oauth2.game.domain.entity.GameResult;

public interface GameResultJpaRepository extends JpaRepository<GameResult, Long>{
}
