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
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import study.oauth2.config.BaseEntity;
import study.oauth2.auth.oauth2.user.OAuth2Provider;

@ToString
@Entity
@Builder
@Getter
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = "user_id")})
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

	@Id
	@Column(name = "user_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "email", unique = true, nullable = false)
	private String email;
	@Column(name = "provider", nullable = false)
	@Enumerated(value = STRING)
	private OAuth2Provider provider;

	@Column(name = "status", nullable = false)
	@Enumerated(value = STRING)
	private MemberStatus status;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "auth_id")
	private Auth auth;

	@Setter
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "profile_id")
	private Profile profile;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "social_id")
	private Social social;

	public static User create(String email, Auth auth, OAuth2Provider provider) {
		if (provider == OAuth2Provider.LOCAL) {
			return User.builder()
				.email(email)
				.provider(provider)
				.status(MemberStatus.ACTIVE)
				.auth(auth)
				.build();
		} else {
			return User.builder()
				.email(email)
				.provider(provider)
				.status(MemberStatus.ACTIVE)
				.build();
		}
	}
}