package study.oauth2.user.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import study.oauth2.user.domain.entity.Follow;

public interface FollowJpaRepository extends JpaRepository<Follow, Follow.PK> {

	Long countByToUser(Long toUser);
	Long countByFromUser(Long fromUser);

	void deleteByToUserAndFromUser(Long toUser, Long fromUser);
}
