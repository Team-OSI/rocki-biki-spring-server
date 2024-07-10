package study.oauth2.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import io.lettuce.core.dynamic.annotation.Param;
import study.oauth2.user.domain.entity.User;
import study.oauth2.user.repository.jpa.UserJpaRepository;

@Repository
public interface UserRepository extends UserJpaRepository {
}
