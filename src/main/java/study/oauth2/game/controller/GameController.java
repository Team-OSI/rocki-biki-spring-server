package study.oauth2.game.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import study.oauth2.game.domain.dto.CoordinateRequestDto;
import study.oauth2.game.domain.dto.GameResultRequestDto;
import study.oauth2.game.service.GameResultService;

@RestController
@RequestMapping("/api/game")
@RequiredArgsConstructor
public class GameController {

	private final GameResultService gameResultService;

	@PostMapping("/result")
	public ResponseEntity<?> saveGameResult(
		@AuthenticationPrincipal UserDetails userDetails,
		@RequestBody @Valid GameResultRequestDto gameResultRequestDto
	) {
		// TODO: Redis에 저장되어있던 game record 가져오기
		gameResultService.saveGameResult(userDetails.getUsername() ,gameResultRequestDto);
		// TODO: 저장 후 Redis에 저장되어있던 game record 삭제
		// TODO: totalDamage와 Highlight Id 반환
		return ResponseEntity.ok().build();
	}

	@PostMapping("/record")
	public void recordGameResult(
		@AuthenticationPrincipal UserDetails userDetails,
		@RequestBody @Valid CoordinateRequestDto coordinateRequestDto
	) {
		// TODO: 노드에서 1초 분량의 버퍼에 담긴 데이터를 CoordinateRequestDto로 받아오기
		// TODO: userDetails.getUsername()으로 Redis Key 생성
		// TODO: Redis에 game record 저장
	}

}
