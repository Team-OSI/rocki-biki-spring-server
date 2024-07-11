package study.oauth2.game.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GameResultResponseDto {

	private Long totalDamage;
	private Long highlightId;

	public static GameResultResponseDto of(Long totalDamage, Long highlightId) {
		return new GameResultResponseDto(totalDamage, highlightId);
	}
}
