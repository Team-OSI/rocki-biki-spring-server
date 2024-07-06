package study.oauth2.user.domain.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Table(
	uniqueConstraints = @UniqueConstraint(columnNames = {"to_user", "from_user"})
)
@IdClass(Follow.PK.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Follow {

	@Id
	@Column(name = "to_user", insertable = false, updatable = false)
	private Long toUser;

	@Id
	@Column(name = "from_user", insertable = false, updatable = false)
	private Long fromUser;

	@Builder
	public Follow(Long toUser, Long fromUser) {
		this.toUser = toUser;
		this.fromUser = fromUser;
	}

	public static class PK implements Serializable {
		Long toUser;
		Long fromUser;
	}

	public static Follow create(Long toUser, Long fromUser) {
		return Follow.builder()
			.toUser(toUser)
			.fromUser(fromUser)
			.build();
	}

}
