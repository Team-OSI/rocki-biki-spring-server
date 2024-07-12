package study.oauth2.auth.oauth2.handler;

import static study.oauth2.auth.oauth2.HttpCookieOAuth2AuthorizationRequestRepository.*;

import java.io.IOException;
import java.util.Optional;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import study.oauth2.auth.local.jwt.TokenProvider;
import study.oauth2.auth.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import study.oauth2.auth.oauth2.service.OAuth2UserPrincipal;
import study.oauth2.auth.oauth2.user.OAuth2Provider;
import study.oauth2.auth.oauth2.user.OAuth2UserUnlinkManager;
import study.oauth2.auth.oauth2.util.CookieUtils;
import study.oauth2.user.service.UserService;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;
    private final OAuth2UserUnlinkManager oAuth2UserUnlinkManager;
    private final TokenProvider tokenProvider;
    private final UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException {

        try {
            ResponseCookie accessTokenCookie = tokenProvider.createToken(authentication);
            response.addHeader(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
            log.info("Access token cookie added to response: {}", accessTokenCookie.toString());
        } catch (Exception e) {
            log.error("Error while creating or setting access token cookie", e);
        }

        String targetUrl = determineTargetUrl(request, response, authentication);

        if (response.isCommitted()) {
            logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
            return;
        }

        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) {
        Optional<String> redirectUri = CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
            .map(Cookie::getValue);
        String targetUrl = redirectUri.orElse(getDefaultTargetUrl());
        String mode = CookieUtils.getCookie(request, MODE_PARAM_COOKIE_NAME)
            .map(Cookie::getValue)
            .orElse("");
        OAuth2UserPrincipal principal = getOAuth2UserPrincipal(authentication);
        if (principal == null) {
            return UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("error", "Login failed")
                .build().toUriString();
        }
        if ("login".equalsIgnoreCase(mode)) {
            userService.findOrCreateUser(principal.getUserInfo().getEmail(), principal.getUserInfo().getProvider());
            log.info("email={}, name={}, nickname={}, accessToken={}", principal.getUserInfo().getEmail(),
                principal.getUserInfo().getName(),
                principal.getUserInfo().getNickname(),
                principal.getUserInfo().getAccessToken()
            );
            return targetUrl; // URL 파라미터 없이 targetUrl만 반환
        } else if ("unlink".equalsIgnoreCase(mode)) {
            String accessToken = principal.getUserInfo().getAccessToken();
            OAuth2Provider provider = principal.getUserInfo().getProvider();
            // TODO: DB 삭제
            // TODO: 리프레시 토큰 삭제
            oAuth2UserUnlinkManager.unlink(provider, accessToken);
            return targetUrl; // URL 파라미터 없이 targetUrl만 반환
        }
        return targetUrl; // 기본적으로 targetUrl 반환
    }

    private OAuth2UserPrincipal getOAuth2UserPrincipal(Authentication authentication) {
        Object principal = authentication.getPrincipal();

        if (principal instanceof OAuth2UserPrincipal) {
            return (OAuth2UserPrincipal) principal;
        }
        return null;
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }
}
