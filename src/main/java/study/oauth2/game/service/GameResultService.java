package study.oauth2.game.service;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import study.oauth2.game.domain.dto.GameResultRequestDto;
import study.oauth2.game.domain.dto.GameResultResponseDto;
import study.oauth2.game.domain.entity.GameResult;
import study.oauth2.game.domain.entity.Highlight;
import study.oauth2.game.repository.GameResultRepository;
import study.oauth2.game.repository.HighlightRepository;
import study.oauth2.user.domain.entity.User;
import study.oauth2.user.repository.UserRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class GameResultService {

	private final GameResultRepository gameResultRepository;
	private final HighlightRepository highlightRepository;
	private final UserRepository userRepository;
	private final ObjectMapper objectMapper;

	public GameResultResponseDto saveGameResult(String userEmail, GameResultRequestDto gameResultRequestDto) throws
		JsonProcessingException {

		User user = userRepository.findByEmailWithProfileDefault(userEmail);
		User opponent = userRepository.findByEmailWithProfileDefault(gameResultRequestDto.getOpponentEmail());
		GameResult gameResult = GameResultRequestDto.toGameResultEntity(user, opponent, gameResultRequestDto);

		Highlight highlight = GameResultRequestDto.toHighlightEntity(gameResultRequestDto, objectMapper);
		highlightRepository.save(highlight);
		gameResult.setting(user, highlight);
		gameResultRepository.save(gameResult);
		// TODO: Highlight 좌표 결과 객체 -> JSON 문자열로 변환하여 저장
		return GameResultResponseDto.of(gameResult.getTotalDamage(), gameResult.getHighlight().getId());
	}
}
