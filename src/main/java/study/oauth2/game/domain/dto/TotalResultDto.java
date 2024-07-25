package study.oauth2.game.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TotalResultDto {
	private Long totalWin;
	private Long totalLose;

	public static TotalResultDto create(Long totalWin, Long totalLose) {
		return TotalResultDto.builder()
			.totalWin(totalWin)
			.totalLose(totalLose)
			.build();
	}
}
