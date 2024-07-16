package study.oauth2.user.repository.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import study.oauth2.user.domain.entity.UserSoundUrl;

public interface UserSoundUrlJpaRepository extends JpaRepository<UserSoundUrl, Long> {

	@Modifying
	@Query("UPDATE UserSoundUrl u SET u.url = :newUrl WHERE u.profile.id = :profileId AND u.url IN :oldUrls")
	int bulkUpdateSoundUrls(@Param("profileId") Long profileId, @Param("newUrl") String newUrl, @Param("oldUrls") List<String> oldUrls);
}

