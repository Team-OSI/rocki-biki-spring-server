package study.oauth2.game.domain.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CoordinateDtoTest {

	@Test
	void testCoordinateDtoSerialization() throws Exception {
		// 객체 생성
		List<Coordinate> coordinates = new ArrayList<>();
		for (int i = 0; i < 20; i++) {
			coordinates.add(new Coordinate(i, i * 2, i * 3));
		}
		CoordinateDto coordinateDto = new CoordinateDto(coordinates);

		// ObjectMapper를 사용하여 객체를 JSON으로 변환
		ObjectMapper objectMapper = new ObjectMapper();
		String json = objectMapper.writeValueAsString(coordinateDto);

		// JSON 문자열 출력 (디버깅용)
		System.out.println(json);

		// JSON 문자열을 다시 객체로 변환
		CoordinateDto deserializedDto = objectMapper.readValue(json, CoordinateDto.class);

		// Assertions
		assertNotNull(deserializedDto);
		assertNotNull(deserializedDto.getCoordinates());
		assertEquals(20, deserializedDto.getCoordinates().size());

		// 각 좌표 비교
		for (int i = 0; i < 20; i++) {
			Coordinate original = coordinates.get(i);
			Coordinate deserialized = deserializedDto.getCoordinates().get(i);
			assertEquals(original.getX(), deserialized.getX());
			assertEquals(original.getY(), deserialized.getY());
			assertEquals(original.getZ(), deserialized.getZ());
		}
	}
}
