package study.oauth2.user.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Auth {

	@Id
	@Column(name = "auth_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String password;

	public static Auth create(String password) {
		return Auth.builder()
			.password(password)
			.build();
	}
}
