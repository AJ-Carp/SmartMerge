package com.smartmerge.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smartmerge.model.Account;
import com.smartmerge.model.Profile;
import com.smartmerge.service.ProfileService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class ProfileController {

    private final ProfileService profileService;
    
    @GetMapping("/profile")
    public ResponseEntity<Profile> getProfile(@AuthenticationPrincipal Account account) {
        Profile profile = profileService.getProfileByUserId(account.getUserId());
        if (profile == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(profile);
    }
}
