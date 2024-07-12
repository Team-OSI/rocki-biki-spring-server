package study.oauth2.user.domain.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
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
@Table(name = "profile", uniqueConstraints = {@UniqueConstraint(columnNames = "user_id")})
public class Profile {

	@Id
	@Column(name = "profile_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "user_id", unique = true, nullable = false)
	private Long userId;

	private String nickname;
	private String profileImage;

	@Getter
	@ElementCollection
	@CollectionTable(name = "user_sound_urls", joinColumns = @JoinColumn(name = "profile_id"))
	@Column(name = "user_sound_urls")
	private List<String> userSoundUrls = new ArrayList<>();

	public static Profile create(Long userId, String nickname, String profileImage) {
		return Profile.builder()
			.userId(userId)
			.nickname(nickname)
			.profileImage(profileImage)
			.build();
	}

	public void update(String nickname, String profileImage) {
		this.nickname = nickname;
		this.profileImage = profileImage;
	}

	public void addSoundUrl(String soundUrl) {
		this.userSoundUrls.add(soundUrl);
	}
}
