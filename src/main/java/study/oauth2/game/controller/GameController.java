package study.oauth2.game.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import study.oauth2.game.domain.dto.CoordinateDto;
import study.oauth2.game.domain.dto.GameResultRequestDto;
import study.oauth2.game.domain.dto.GameResultResponseDto;
import study.oauth2.game.domain.dto.RecentResultDto;
import study.oauth2.game.domain.dto.ResultPagingDto;
import study.oauth2.game.service.GameResultService;
import study.oauth2.game.service.HighlightService;

@RestController
@RequestMapping("/api/game")
@RequiredArgsConstructor
public class GameController {

	private final GameResultService gameResultService;
	private final HighlightService highlightService;

	@PostMapping("/result")
	public ResponseEntity<?> saveGameResult(
		@AuthenticationPrincipal UserDetails userDetails,
		@RequestBody @Valid GameResultRequestDto gameResultRequestDto
	) throws JsonProcessingException {
		GameResultResponseDto gameResultResponseDto = gameResultService.saveGameResult(userDetails.getUsername(), gameResultRequestDto);
		return ResponseEntity.ok(gameResultResponseDto);
	}

	@GetMapping("/highlight")
	public ResponseEntity<?> getGameHighlight(@RequestParam("highlightId") Long highlightId) throws
		JsonProcessingException {
		CoordinateDto gameHighlight = highlightService.getGameHighlight(highlightId);
		return ResponseEntity.ok(gameHighlight);
	}

	@PostMapping("/recent-result")
	public ResponseEntity<?> getGameResult(
		@AuthenticationPrincipal UserDetails userDetails,
		@Valid @RequestBody ResultPagingDto postPagingDto
	) {
		Page<RecentResultDto> page = gameResultService.getGameResult(userDetails.getUsername(), postPagingDto);
		return ResponseEntity.ok(page);
	}
}
