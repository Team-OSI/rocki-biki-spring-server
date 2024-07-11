package study.oauth2.user.repository.jpa;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import io.lettuce.core.dynamic.annotation.Param;
import lombok.extern.slf4j.Slf4j;
import study.oauth2.user.domain.entity.User;

public interface UserJpaRepository extends JpaRepository<User, Long>{

	Optional<User> findByEmail(String email);

	default User findByEmailDefault(String email) {
		return findByEmail(email).orElseThrow(() -> new IllegalArgumentException("해당 이메일로 가입된 유저가 없습니다."));
	}

	boolean existsByEmail(String email);

	@Query("SELECT u FROM User u LEFT JOIN FETCH u.auth WHERE u.email = :email")
	Optional<User> findByEmailWithAuth(@Param("email") String email);

	@Query("SELECT u FROM User u LEFT JOIN FETCH u.profile WHERE u.email = :email")
	Optional<User> findByEmailWithProfile(@Param("email") String email);

	default User findByEmailWithProfileDefault(String email) {
		return findByEmailWithProfile(email).orElseThrow(() -> new IllegalArgumentException("해당 이메일로 가입된 유저가 없습니다."));
	}
}
