package study.oauth2.game.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import study.oauth2.game.domain.entity.GameResult;

@Getter
@Setter
@Builder
public class RecentResultDto {

	private String userName;
	private String opponentName;

	private String userEmail;
	private String opponentEmail;

	private String userProfileImage;
	private String opponentProfileImage;

	private Boolean win;
	// private Long totalDamage;

	public static RecentResultDto toDto(GameResult gameResult) {
		return RecentResultDto.builder()
			.userName(gameResult.getMyProfile().getNickname())
			.opponentName(gameResult.getOpponentProfile().getNickname())
			.userEmail(gameResult.getUserEmail())
			.opponentEmail(gameResult.getOpponentEmail())
			.win(gameResult.getWin())
			.userProfileImage(gameResult.getMyProfile().getProfileImage())
			.opponentProfileImage(gameResult.getOpponentProfile().getProfileImage())
			// .totalDamage(gameResult.getTotalDamage())
			// .highlightId(gameResult.getHighlight().getId())
			.build();
	}
}
