package study.oauth2.game.service;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import study.oauth2.game.domain.dto.CoordinateDto;
import study.oauth2.game.domain.entity.Highlight;
import study.oauth2.game.repository.HighlightRepository;

@Service
@RequiredArgsConstructor
public class HighlightService {

	private final HighlightRepository highlightRepository;
	private final ObjectMapper objectMapper;

	public CoordinateDto getGameHighlight(Long highlightId) throws JsonProcessingException {
		Highlight highlight = highlightRepository.findById(highlightId)
			.orElseThrow(() -> new IllegalArgumentException("해당 하이라이트가 존재하지 않습니다."));
		return CoordinateDto.of(highlight.getCoordinates(), objectMapper);
	}
}
