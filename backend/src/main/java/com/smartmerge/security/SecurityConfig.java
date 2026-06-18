package com.smartmerge.security;

import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Value("${client.url}")
    private String CLIENT_URL;

    private final CustomOAuthUserService CustomOAuthUserService;
    private final CustomOauth2SuccessHandler CustomOauth2SuccessHandler;
    private final JwtAuthFilter JwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(requests -> requests
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .requestMatchers("/api/v1/webhook/github", "/api/v1/auth/logout").permitAll()
                .anyRequest().authenticated()
            )
            .oauth2Login(oauth -> oauth
                // Step 1 — GitHub redirects back with an auth code. Spring exchanges it for
                // an access token, then calls customOauthUserService.loadUser() to fetch the
                // user's GitHub profile.
                .userInfoEndpoint(userInfo -> userInfo.userService(CustomOAuthUserService))
                // Step 2 — "on success": Spring calls this only after loadUser() completes
                // without error. Here we save the user to the DB, mint a JWT, and set it
                // as an httpOnly cookie before redirecting to the frontend.
                .successHandler(CustomOauth2SuccessHandler)
            );
        http.addFilterBefore(JwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOriginPatterns(List.of(CLIENT_URL));
        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        corsConfiguration.setAllowedHeaders(List.of("*"));
        corsConfiguration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }
}