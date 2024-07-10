package study.oauth2.game.repository;

import org.springframework.stereotype.Repository;

import study.oauth2.game.repository.jpa.GameResultJpaRepository;

@Repository
public interface GameResultRepository extends GameResultJpaRepository {
}
