package study.oauth2.game.domain.dto;

import org.springframework.security.crypto.password.NoOpPasswordEncoder;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import study.oauth2.game.domain.entity.GameResult;
import study.oauth2.user.domain.entity.User;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GameResultRequestDto {

	private String opponentEmail;
	private Long totalDamage;

	private String winner;

	public static GameResult toEntity(User user, User opponent, GameResultRequestDto requestDto) {
		String winner = user.getEmail().equals(requestDto.getWinner()) ? user.getProfile().getNickname() : opponent.getProfile().getNickname();
		return GameResult.builder()
			.opponentId(opponent.getId())
			.userEmail(user.getEmail())
			.opponentEmail(opponent.getEmail())
			.userName(user.getProfile().getNickname())
			.opponentName(opponent.getProfile().getNickname())
			.winner(winner)
			.totalDamage(requestDto.getTotalDamage())
			.build();
	}
}
