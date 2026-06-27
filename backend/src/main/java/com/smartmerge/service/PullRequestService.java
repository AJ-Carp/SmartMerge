package com.smartmerge.service;

import org.springframework.stereotype.Service;

import com.smartmerge.model.PullRequest;
import com.smartmerge.repository.PullRequestRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PullRequestService {
    
    private final PullRequestRepository pullRequestRepository;

    public PullRequest savePullRequest(PullRequest pullRequest) {
        return pullRequestRepository.save(pullRequest);
    }

    public void deletePRsByInstallationId(int installationId) {
        pullRequestRepository.deleteAllByInstallationId(installationId);
    }
}
