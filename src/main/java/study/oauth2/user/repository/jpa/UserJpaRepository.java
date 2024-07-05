package study.oauth2.user.repository.jpa;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import study.oauth2.user.domain.entity.User;

public interface UserJpaRepository extends JpaRepository<User, Long>{

	Optional<User> findByEmail(String email);

	boolean existsByEmail(String email);
}
