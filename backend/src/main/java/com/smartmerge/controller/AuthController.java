package com.smartmerge.controller;

import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@RequestMapping("/api/v1/auth")
@RestController
public class AuthController {
    
    @PostMapping("/logout")
    /* Spring sees HttpServletResponse (or HttpServletRequest) in a method signature and 
       automatically injects the raw request/response object for that HTTP call */
    public ResponseEntity<Map<String, String>> logout(HttpServletResponse response) {
        try {
            Cookie cookie = new Cookie("jwt", null);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setMaxAge(0);
            response.addCookie(cookie);
            return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
