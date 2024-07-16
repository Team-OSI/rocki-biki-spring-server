package study.oauth2.user.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Entity
@Table(name = "user_sound_urls")
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserSoundUrl {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "profile_id")
	private Profile profile;

	@Column(name = "sound_url")
	private String url;

	public static UserSoundUrl create(Profile profile, String url) {
		return UserSoundUrl.builder()
			.profile(profile)
			.url(url)
			.build();
	}
}
