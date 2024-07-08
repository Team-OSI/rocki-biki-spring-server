package study.oauth2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;
import study.oauth2.auth.jwt.JwtAuthenticationFilter;
import study.oauth2.auth.jwt.JwtAuthorizationFilter;
import study.oauth2.auth.jwt.TokenProvider;
import study.oauth2.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import study.oauth2.oauth2.handler.OAuth2AuthenticationFailureHandler;
import study.oauth2.oauth2.handler.OAuth2AuthenticationSuccessHandler;
import study.oauth2.oauth2.service.CustomOAuth2UserService;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;
    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;
    private final TokenProvider tokenProvider;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager, tokenProvider);
        JwtAuthorizationFilter jwtAuthorizationFilter = new JwtAuthorizationFilter(authenticationManager, tokenProvider);

        http
            .csrf(AbstractHttpConfigurer::disable)
            .headers(headersConfigurer -> headersConfigurer.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
            .formLogin(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/api/hello/**", "/h2-console/**", "/api/auth/**").permitAll()
                .anyRequest().authenticated()
            )
            .sessionManagement(sessions -> sessions.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .oauth2Login(configure ->
                configure.authorizationEndpoint(config -> config.authorizationRequestRepository(httpCookieOAuth2AuthorizationRequestRepository))
                    .userInfoEndpoint(config -> config.userService(customOAuth2UserService))
                    .successHandler(oAuth2AuthenticationSuccessHandler)
                    .failureHandler(oAuth2AuthenticationFailureHandler)
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
