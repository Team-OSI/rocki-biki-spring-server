package study.oauth2.game.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import study.oauth2.game.domain.dto.CoordinateDto;
import study.oauth2.game.domain.dto.GameResultRequestDto;
import study.oauth2.game.domain.dto.GameResultResponseDto;
import study.oauth2.game.domain.dto.RecentResultDto;
import study.oauth2.game.domain.dto.ResultPagingDto;
import study.oauth2.game.service.GameResultService;
import study.oauth2.game.service.HighlightService;

@SpringBootTest
@AutoConfigureMockMvc
class GameControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private GameResultService gameResultService;

	@MockBean
	private HighlightService highlightService;

	@Autowired
	private ObjectMapper objectMapper;

	private GameResultRequestDto gameResultRequestDto;

	@BeforeEach
	void setUp() {
		gameResultRequestDto = new GameResultRequestDto(
			"opponent@example.com",
			1000L,
			"user@example.com",
			new CoordinateDto(Collections.emptyList())
		);
	}

	@Test
	@WithMockUser(username = "user@example.com")
	void saveGameResult() throws Exception {
		// Mock the response from the service
		GameResultResponseDto mockResponse = new GameResultResponseDto(1000L, 1L);
		when(gameResultService.saveGameResult(eq("user@example.com"), any(GameResultRequestDto.class)))
			.thenReturn(mockResponse);

		// Perform the request and verify the response
		mockMvc.perform(post("/api/game/result")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(gameResultRequestDto)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.totalDamage").value(mockResponse.getTotalDamage())) // replace with actual field and expected value
			.andExpect(jsonPath("$.highlightId").value(mockResponse.getHighlightId())); // replace with actual field and expected value
	}

	@Test
	@WithMockUser(username = "user@example.com")
	void getGameHighlight() throws Exception {
		CoordinateDto coordinateDto = new CoordinateDto(Collections.emptyList());
		when(highlightService.getGameHighlight(1L)).thenReturn(coordinateDto);

		mockMvc.perform(get("/api/game/highlight")
				.param("highlightId", "1"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.coordinates").isArray());
	}

	@Test
	@WithMockUser(username = "user@example.com")
	void getGameResult() throws Exception {
		Pageable pageable = PageRequest.of(0, 10, Sort.by("createdDate").descending());
		Page<RecentResultDto> page = new PageImpl<>(Collections.emptyList(), pageable, 0);
		ResultPagingDto resultPagingDto = new ResultPagingDto(0, 10, "DESC", "createdDate");

		when(gameResultService.getGameResult(eq("user@example.com"), any(ResultPagingDto.class)))
			.thenReturn(page);

		mockMvc.perform(post("/api/game/recent-result")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(resultPagingDto)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.content").isArray());
	}
}
