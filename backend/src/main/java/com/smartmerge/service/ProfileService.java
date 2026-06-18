package com.smartmerge.service;

import org.springframework.stereotype.Service;
import com.smartmerge.model.Profile;
import com.smartmerge.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ProfileService {
    
    private final ProfileRepository profileRepository;

    public Profile saveProfile(Profile profile) {
        return profileRepository.save(profile);
    }
}
