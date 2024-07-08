package study.oauth2.auth.local.handler;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import study.oauth2.auth.local.jwt.TokenProvider;

@Slf4j
@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	private final TokenProvider jwtTokenProvider;
	private static final long ACCESS_TOKEN_EXPIRE_TIME_IN_MILLISECONDS = 24 * 60 * 60 * 1000; // 1 day

	public CustomAuthenticationSuccessHandler(TokenProvider jwtTokenProvider) {
		this.jwtTokenProvider = jwtTokenProvider;
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

		log.info("Login successful");
		ResponseCookie cookie = jwtTokenProvider.createToken(authentication);
		response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

		// JSON 응답 반환
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write("{\"message\": \"Login successful\"}");
	}
}
