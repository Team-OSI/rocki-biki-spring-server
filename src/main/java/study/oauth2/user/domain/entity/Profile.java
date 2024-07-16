package study.oauth2.user.domain.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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

	@OneToMany(mappedBy = "profile")
	private List<UserSoundUrl> userSoundUrls = new ArrayList<>();

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

	public UserSoundUrl addSoundUrl(String soundUrl) {
		UserSoundUrl userSoundUrl = UserSoundUrl.create(this, soundUrl);
		this.userSoundUrls.add(userSoundUrl);
		return userSoundUrl;
	}

	public void deleteSoundUrl(String soundUrl) {
		this.userSoundUrls.removeIf(userSoundUrl -> userSoundUrl.getUrl().equals(soundUrl));
	}

	public void updateSoundUrl(String oldUrl, String newUrl) {
		this.userSoundUrls.stream()
			.filter(userSoundUrl -> userSoundUrl.getUrl().equals(oldUrl))
			.findFirst()
			.ifPresent(userSoundUrl -> userSoundUrl.setUrl(newUrl));
	}

	public List<String> getSoundUrls() {
		return this.userSoundUrls.stream()
			.map(UserSoundUrl::getUrl)
			.toList();
	}
}
