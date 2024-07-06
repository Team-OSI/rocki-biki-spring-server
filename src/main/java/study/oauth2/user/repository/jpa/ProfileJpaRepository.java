package study.oauth2.user.repository.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import study.oauth2.user.domain.entity.Profile;

public interface ProfileJpaRepository extends JpaRepository<Profile, Long>{

	@Query("SELECT p FROM Profile p "
		+ "INNER JOIN Follow f ON f.toUser = p.userId "
		+ "WHERE f.fromUser = :userId")
	List<Profile> findAllByFromUser(@Param("userId") Long userId);

	@Query("SELECT p FROM Profile p "
		+ "INNER JOIN Follow f ON f.fromUser = p.userId "
		+ "WHERE f.toUser = :userId")
	List<Profile> findAllByToUser(@Param("userId") Long userId);

}
