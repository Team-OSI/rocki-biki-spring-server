package study.oauth2.game.domain.dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import study.oauth2.game.domain.entity.GameResult;
import study.oauth2.game.domain.entity.Highlight;
import study.oauth2.user.domain.entity.User;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GameResultRequestDto {

	private String opponentEmail;
	private Long totalDamage;

	private Boolean win;
	private CoordinateDto coordinates;

	public static GameResult toGameResultEntity(User user, User opponent, GameResultRequestDto requestDto) {
		return GameResult.builder()
			.opponentId(opponent.getId())
			.userEmail(user.getEmail())
			.opponentEmail(opponent.getEmail())
			.userName(user.getProfile().getNickname())
			.opponentName(opponent.getProfile().getNickname())
			.win(requestDto.getWin())
			.totalDamage(requestDto.getTotalDamage())
			.build();
	}

	public static Highlight toHighlightEntity(GameResultRequestDto requestDto, ObjectMapper objectMapper) throws
		JsonProcessingException {
		String coordinatesJson = objectMapper.writeValueAsString(requestDto.getCoordinates());
		return Highlight.builder()
			.coordinates(coordinatesJson)
			.build();
	}
}
