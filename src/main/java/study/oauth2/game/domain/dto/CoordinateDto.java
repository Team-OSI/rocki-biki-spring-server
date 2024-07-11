package study.oauth2.game.domain.dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CoordinateDto {

	private List<Coordinate> coordinates = new ArrayList<>();

	public static CoordinateDto of(String coordinatesJson, ObjectMapper objectMapper) throws JsonProcessingException {
		return objectMapper.readValue(coordinatesJson, CoordinateDto.class);
	}

}
