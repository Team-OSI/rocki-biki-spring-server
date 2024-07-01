package study.oauth2.oauth.exception;

import org.springframework.security.core.AuthenticationException;

public class OAuth2AuthenticationProcessingException extends AuthenticationException {
	public OAuth2AuthenticationProcessingException(String msg) {
		super(msg);
	}
}
