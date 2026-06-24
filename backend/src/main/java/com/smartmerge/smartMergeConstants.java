package com.smartmerge;

public interface SmartMergeConstants {
    // routes
    String API_PREFIX = "api/v1";
    String PROFILE_ROUTE = API_PREFIX + "/profile";
    String GITHUB_WEB_HOOK_ROUTE = API_PREFIX + "/webhook";
    String REPOSITORY_ROUTE = API_PREFIX + "/repository";
    String PULL_REQUEST_ROUTE = API_PREFIX + "/pull-requests";
    String AUTH_ROUTE = API_PREFIX + "/auth";
    String LOGOUT_ROUTE = AUTH_ROUTE + "/logout";
    // api constants
    String BEARER = "Bearer ";
    String AUTHORIZATION = "Authorization";
    String GITHUB_REQUEST_BODY_TYPE = "application/vnd.github+json";
    String GITHUB_BASE_URL = "https://api.github.com";
    String ACCEPT = "Accept";
    String GITHUB_EMAIL_ENDPOINT = "https://api.github.com/user/emails";
}


