package study.oauth2.user.repository;

import org.springframework.stereotype.Repository;

import study.oauth2.user.repository.jpa.UserJpaRepository;

@Repository
public interface UserRepository extends UserJpaRepository {
}
