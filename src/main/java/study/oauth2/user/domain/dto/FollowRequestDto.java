package study.oauth2.user.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
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
public class FollowRequestDto {

	@NotNull(message = "toUser cannot be null")
	@Email(message = "toUser should be valid")
	private String toUser;

}
