package study.oauth2.game.domain.dto;

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
	// private Long totalDamage;

	private Boolean win;
	// private CoordinateDto coordinates;

	public static GameResult toEntity(User user, User opponent, GameResultRequestDto requestDto) {
		return GameResult.builder()
			.userEmail(user.getEmail())
			.opponentEmail(opponent.getEmail())
			.myProfile(user.getProfile())
			.opponentProfile(opponent.getProfile())
			.win(requestDto.getWin())
			// .totalDamage(requestDto.getTotalDamage())
			.build();
	}

	// public static Highlight toHighlightEntity(GameResultRequestDto requestDto, ObjectMapper objectMapper) throws
	// 	JsonProcessingException {
	// 	String coordinatesJson = objectMapper.writeValueAsString(requestDto.getCoordinates());
	// 	return Highlight.builder()
	// 		.coordinates(coordinatesJson)
	// 		.build();
	// }
}
