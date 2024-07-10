package study.oauth2.game.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import study.oauth2.game.domain.entity.Highlight;

public interface HighlightJpaRepository extends JpaRepository<Highlight, Long> {
}
