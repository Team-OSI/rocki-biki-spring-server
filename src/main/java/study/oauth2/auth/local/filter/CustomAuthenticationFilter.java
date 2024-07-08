package study.oauth2.auth.local.filter;

import java.io.IOException;

import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import study.oauth2.auth.local.dto.SignRequest;
import study.oauth2.auth.local.jwt.TokenProvider;

@Slf4j
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	private final AuthenticationManager authenticationManager;
	private final TokenProvider tokenProvider;

	public CustomAuthenticationFilter(AuthenticationManager authenticationManager, TokenProvider tokenProvider) {
		this.authenticationManager = authenticationManager;
		this.tokenProvider = tokenProvider;
		setFilterProcessesUrl("/api/auth/login");
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
		try {
			SignRequest loginRequest = new ObjectMapper().readValue(request.getInputStream(), SignRequest.class);
			Authentication authentication = new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());
			return authenticationManager.authenticate(authentication);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException,
		ServletException {
		log.info("Login successful");
		ResponseCookie jwtCookie = tokenProvider.createToken(authResult);
		response.addHeader("Set-Cookie", jwtCookie.toString());

		// JSON 응답 추가
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write("{\"message\":\"Login successful\"}");
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write("{\"error\":\"Authentication failed\"}");
	}
}
