package study.oauth2.user.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import study.oauth2.user.domain.entity.Auth;

public interface AuthJpaRepository extends JpaRepository<Auth, Long>{
}
