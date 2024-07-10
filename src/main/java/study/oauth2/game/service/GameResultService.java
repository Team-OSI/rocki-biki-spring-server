package study.oauth2.game.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import study.oauth2.game.domain.dto.GameResultRequestDto;
import study.oauth2.game.domain.entity.GameResult;
import study.oauth2.game.repository.GameResultRepository;
import study.oauth2.user.domain.entity.User;
import study.oauth2.user.repository.UserRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class GameResultService {

	private final GameResultRepository gameResultRepository;
	private final UserRepository userRepository;

	// TODO: 게임 결과 저장
	public void saveGameResult(String userEmail, GameResultRequestDto gameResultRequestDto) {
		log.info("saveGameResult userEmail: {}", userEmail);
		User user = userRepository.findByEmailWithProfileDefault(userEmail);

		User opponent = userRepository.findByEmailWithProfileDefault(gameResultRequestDto.getOpponentEmail());

		GameResult gameResult = GameResultRequestDto.toEntity(user, opponent, gameResultRequestDto);
		gameResult.setUser(user);
		gameResultRepository.save(gameResult);
	}

	// public void setCoordinates(List<Coordinate> coordinates) throws JsonProcessingException {
	// 	this.coordinates = mapper.writeValueAsString(coordinates);
	// }
	//
	// // JSON 문자열을 좌표 데이터로 반환
	// public List<Coordinate> getCoordinates() throws JsonProcessingException {
	// 	return mapper.readValue(this.coordinates, new TypeReference<List<Coordinate>>() {});
	// }
}
