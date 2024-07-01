package study.oauth2.oauth.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OAuth2Provider {
	GOOGLE("google"),
	FACEBOOK("facebook"),
	GITHUB("github"),
	NAVER("naver"),
	KAKAO("kakao");

	private final String registrationId;
}
