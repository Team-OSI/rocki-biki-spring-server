package study.oauth2.oauth.user;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import study.oauth2.oauth.exception.OAuth2AuthenticationProcessingException;

@RequiredArgsConstructor
@Component
public class OAuth2UserUnlinkManager {

	private final GoogleOAuth2UserUnlink googleOAuth2UserUnlink;
	private final KakaoOAuth2UserUnlink kakaoOAuth2UserUnlink;
	private final NaverOAuth2UserUnlink naverOAuth2UserUnlink;

	public void unlink(OAuth2Provider provider, String accessToken) {
		if (OAuth2Provider.GOOGLE.equals(provider)) {
			googleOAuth2UserUnlink.unlink(accessToken);
		} else if (OAuth2Provider.NAVER.equals(provider)) {
			naverOAuth2UserUnlink.unlink(accessToken);
		} else if (OAuth2Provider.KAKAO.equals(provider)) {
			kakaoOAuth2UserUnlink.unlink(accessToken);
		} else {
			throw new OAuth2AuthenticationProcessingException(
				"Unlink with " + provider.getRegistrationId() + " is not supported");
		}
	}
}
