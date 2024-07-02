package study.oauth2.user.repository.jpa;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import study.oauth2.user.domain.entity.User;

public interface UserJpaRepository extends JpaRepository<User, Long>{

	@Query("SELECT u FROM User u "
		+ "JOIN FETCH u.auth "
		+ "JOIN FETCH u.profile "
		+ "WHERE u.email = :email")
	Optional<User> findByEmail(@Param("email") String email);

	boolean existsByEmail(String email);
}
