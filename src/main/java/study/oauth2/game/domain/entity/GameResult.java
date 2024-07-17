package study.oauth2.game.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.oauth2.config.BaseEntity;
import study.oauth2.user.domain.entity.Profile;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GameResult extends BaseEntity {

	@Id
	@Column(name = "game_result_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "profile_id")
	private Profile myProfile;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "opponent_profile_id")
	private Profile opponentProfile;


	private String userEmail;
	private String opponentEmail;

	private Boolean win;
	private Long totalDamage;

}
