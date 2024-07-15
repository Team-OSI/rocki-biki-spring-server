package study.oauth2.user.domain.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AudioUploadDTO {

	private MultipartFile audio0;
	private MultipartFile audio1;
	private MultipartFile audio2;
	private MultipartFile audio3;
	private MultipartFile audio4;

	private String audioUrl0;
	private String audioUrl1;
	private String audioUrl2;
	private String audioUrl3;
	private String audioUrl4;

	public List<List<Object>> getAudios() {
		List<List<Object>> audios = new ArrayList<>();

		if (audio0 != null) {
			audios.add(List.of(audio0, audioUrl0 != null ? audioUrl0 : ""));
		}
		if (audio1 != null) {
			audios.add(List.of(audio1, audioUrl1 != null ? audioUrl1 : ""));
		}
		if (audio2 != null) {
			audios.add(List.of(audio2, audioUrl2 != null ? audioUrl2 : ""));
		}
		if (audio3 != null) {
			audios.add(List.of(audio3, audioUrl3 != null ? audioUrl3 : ""));
		}
		if (audio4 != null) {
			audios.add(List.of(audio4, audioUrl4 != null ? audioUrl4 : ""));
		}

		return audios;
	}
}
