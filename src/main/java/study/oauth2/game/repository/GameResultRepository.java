package study.oauth2.game.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import study.oauth2.game.repository.custom.CustomGameResultRepository;
import study.oauth2.game.repository.jpa.GameResultJpaRepository;

@Repository
public interface GameResultRepository extends GameResultJpaRepository, CustomGameResultRepository {

	@Query("SELECT COUNT(gr) FROM GameResult gr WHERE gr.userEmail = :userEmail AND gr.win = true")
	Long countWinByUserEmail(String userEmail);

	@Query("SELECT COUNT(gr) FROM GameResult gr WHERE gr.userEmail = :userEmail AND gr.win = true")
	Long countLoseByUserEmail(String userEmail);
}
