package com.smartmerge.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.smartmerge.model.Account;
import com.smartmerge.service.AccountService;
import java.io.IOException;
import java.util.Collections;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final AccountService accountService;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws IOException, ServletException {
        try {
            // URI = enpoint
            log.warn("request is going through the filter URI={}", request.getRequestURI());
            // collect cookies from the request and find the one carrying the JWT
            Cookie[] cookies = request.getCookies();
            Cookie jwtCookie = null;
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("jwt")) {
                        jwtCookie = cookie;
                    }
                }
            }

            if (jwtCookie == null) {
                log.warn("jwt cookie is not found in the request");
                filterChain.doFilter(request, response);
                return;
            }

            String jwtToken = jwtCookie.getValue();

            if (!jwtService.isTokenValid(jwtToken)) {
                log.warn("jwt is not valid");
                filterChain.doFilter(request, response);
                return;
            }

            String email = jwtService.extractSubject(jwtToken);
            Account account = accountService.findByUserEmail(email);
            if (account != null) {
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                    account, null, Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
                );
                SecurityContextHolder.getContext().setAuthentication(auth);
            }

            filterChain.doFilter(request, response);

        } catch (Exception e) {
            log.warn("Error during jwt authentication: {}", e.getMessage());
            
            // deleting existing jwt cookie
            // creating new cookie with the same name tells browser to replace the existing one
            Cookie expiredCookie = new Cookie("jwt", null);
            expiredCookie.setPath("/");
            expiredCookie.setHttpOnly(true);
            // setting max age to 0 makes it expired so it will be deleted
            expiredCookie.setMaxAge(0);

            response.addCookie(expiredCookie);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Invalid JWT\"}");
        }                          
    }
}
