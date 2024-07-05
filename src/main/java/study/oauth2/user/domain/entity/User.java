package study.oauth2.user.domain.entity;

import static jakarta.persistence.EnumType.*;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.oauth2.oauth2.user.OAuth2Provider;

@Entity
@Builder
@Getter
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

	@Id
	@Column(name = "user_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "email", unique = true, nullable = false)
	private String email;
	@Column(name = "provider", nullable = false)
	@Enumerated(value = STRING)
	private OAuth2Provider provider;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "auth_id")
	private Auth auth;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "profile_id")
	private Profile profile;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "social_id")
	private Social social;

	public static User create(String email, Auth auth, Profile profile, OAuth2Provider provider) {
		if (provider == OAuth2Provider.LOCAL) {
			return User.builder()
				.email(email)
				.provider(provider)
				.auth(auth)
				.profile(profile)
				.build();
		} else {
			return User.builder()
				.email(email)
				.provider(provider)
				.profile(profile)
				.build();
		}
	}
}