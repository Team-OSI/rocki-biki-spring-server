package study.oauth2.game.domain.entity;

import net.minidev.json.annotate.JsonIgnore;

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
import study.oauth2.user.domain.entity.User;

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

	private Long opponentId;

	private String userName;
	private String opponentName;

	private String userEmail;
	private String opponentEmail;

	private Boolean win;
	private Long totalDamage;

	private String opponentProfileImage;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "highlight_id")
	private Highlight highlight;

	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	@JoinColumn(name = "user_id", updatable = false)
	private User user;

	public void setting(User user, Highlight highlight) {
		this.user = user;
		user.getGameResults().add(this);
		this.highlight = highlight;
	}
}
