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

	private Boolean win;
	private Long totalDamage;

	private String opponentProfileImage;
	private Long highlightId;

	public static RecentResultDto toDto(GameResult gameResult) {
		return RecentResultDto.builder()
			.userName(gameResult.getUserName())
			.opponentName(gameResult.getOpponentName())
			.userEmail(gameResult.getUserEmail())
			.opponentEmail(gameResult.getOpponentEmail())
			.win(gameResult.getWin())
			.totalDamage(gameResult.getTotalDamage())
			.opponentProfileImage(gameResult.getOpponentProfileImage())
			.highlightId(gameResult.getHighlight().getId())
			.build();
	}
}
