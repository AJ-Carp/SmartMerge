package com.smartmerge.security;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import com.smartmerge.model.Account;
import com.smartmerge.model.Profile;
import com.smartmerge.service.AccountService;
import com.smartmerge.service.ProfileService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomOauth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final AccountService accountService;
    private final ProfileService profileService;
    private final JwtService jwtService;

    @Value("${client.url}")
    String CLIENT_URL;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, 
            Authentication authentication) throws IOException, ServletException {
    
        try {
            log.info("Authentication successful");
            /* getPrincipal() returns the authenticated user. Since they logged in via GitHub OAuth,
               Spring wraps their GitHub profile data (id, login, email, avatar_url, etc.) in an OAuth2User object. */
            // This runs for both first-time registration and returning logins.
            OAuth2User oAuth2User = (OAuth2User)authentication.getPrincipal();
            
            // Building and saving account and profile models
            Account account = createAccount(oAuth2User);
            Profile profile = createProfile(oAuth2User);

            Account savedAccount = accountService.saveAccount(account);
            Profile savedProfile = profileService.saveProfile(profile);
            log.info("Saved user account for userId={}", savedAccount.getUserId());

            // generate jwt's for client API authentication
            String jwt = jwtService.generateToken(savedAccount);
            log.info("Token generated for userId={}", savedAccount.getUserId());

            /* Secure=true + SameSite=None works on HTTPS in production and in Chrome's localhost 
            (which treats localhost as a secure context). Safari/Firefox are stricter. 
            For local dev on all browsers, use Secure=false; SameSite=Lax instead. */
            ResponseCookie responseCookie = ResponseCookie.from("jwt", jwt)
                    .httpOnly(true)
                    .secure(true)
                    .path("/")
                    .maxAge(24 * 60 * 60) // 24 hours
                    .sameSite("None")
                    .build();

            response.addHeader("Set-Cookie", responseCookie.toString());

            // Spring creates a JSESSIONID cookie during the OAuth flow to track the temporary login session.
            // Since we use JWTs instead of server-side sessions, we don't need it — delete it immediately by setting maxAge to 0.
            Cookie jsessionCookie = new Cookie("JSESSIONID", null);
            jsessionCookie.setPath("/");
            response.addCookie(jsessionCookie);

            response.sendRedirect(CLIENT_URL + "/call-back");
        } catch (Exception e) {
            log.error("Error after authentication error={}", e.getMessage());

            // delete jwt cookie and redirect back to frontend
            Cookie cookie = new Cookie("jwt", null); 
            cookie.setMaxAge(0);
            cookie.setPath("/");
            cookie.setHttpOnly(true);
            response.addCookie(cookie);
            response.sendRedirect(CLIENT_URL);
        }
    }

    public Account createAccount(OAuth2User oauth2User) {
        return Account.builder()
                .userId(oauth2User.getAttribute("id"))
                .userLogin(oauth2User.getAttribute("login"))
                .email(oauth2User.getAttribute("email"))
                .build();
    }

    public Profile createProfile(OAuth2User oauth2User) {
        return Profile.builder()
                .userId(oauth2User.getAttribute("id"))
                .login(oauth2User.getAttribute("login"))
                .avatarUrl(oauth2User.getAttribute("avatar_url"))
                .build();
    }
}
