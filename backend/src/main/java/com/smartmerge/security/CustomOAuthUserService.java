package com.smartmerge.security;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import lombok.extern.slf4j.Slf4j;
import static com.smartmerge.smartMergeConstants.GITHUB_EMAIL_ENDPOINT;

@Slf4j
@Service
public class CustomOAuthUserService extends DefaultOAuth2UserService {
    
    private final String GITHUB_REQUEST_BODY_TYPE = "application/vnd.github+json";

    /* when user logs in via github, github sends back info (attributes) about them. If the info does 
       not contain the email (because it was set to private) then we fetch it from githubs api and add 
       it to the attributes */
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // extract user
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String nameAttrKey = userRequest.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();
        
        // get the attributes
        Map<String, Object> userAttributes = new HashMap<>(oAuth2User.getAttributes());
        Object email = userAttributes.get("email");

        if (email == null || ((String)email).isEmpty()) {
            getUserEmail(userRequest.getAccessToken().getTokenValue(), userAttributes);
        }

        return new DefaultOAuth2User(oAuth2User.getAuthorities(), userAttributes, nameAttrKey);
    }

    private void getUserEmail(String tokenValue, Map<String, Object> userAttributes) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set(HttpHeaders.AUTHORIZATION, "token " + tokenValue);
            httpHeaders.set(HttpHeaders.AUTHORIZATION, GITHUB_REQUEST_BODY_TYPE);
            HttpEntity<Void> httpEntity = new HttpEntity<>(httpHeaders);

            ResponseEntity<List<Map<String, Object>>> response  = restTemplate.exchange(
                GITHUB_EMAIL_ENDPOINT,
                HttpMethod.GET,
                httpEntity,
                new ParameterizedTypeReference<List<Map<String, Object>>>() {}

            );

            List<Map<String, Object>> emails = response.getBody();
            if (emails.isEmpty()) {
                return;
            }

            // each email comes as own map
            for (Map<String, Object> map : emails) {
                boolean isPrimary = (boolean)map.get("primary");
                String email = (String)map.get("email");

                if (isPrimary && email != null && !email.isEmpty()) {
                    userAttributes.put("email", email);
                }
            }
        } catch (Exception e) {
            log.error("Failed to fetch user email {}", e.getMessage());
        }
    }
}
