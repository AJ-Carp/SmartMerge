package com.smartmerge.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.smartmerge.model.Account;
import com.smartmerge.model.PullRequest;
import com.smartmerge.model.Repo;
import com.smartmerge.service.PullRequestService;
import com.smartmerge.service.RepoService;

import lombok.RequiredArgsConstructor;
import static com.smartmerge.SmartMergeConstants.PULL_REQUEST_ROUTE;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping(PULL_REQUEST_ROUTE)
@RequiredArgsConstructor
public class PullRequestController {
    
    private final PullRequestService pullRequestService;
    private final RepoService repoService;

    @GetMapping("/repository/{repoId}")
    public ResponseEntity<List<PullRequest>> getPullRequestsByRepoId(@PathVariable long repoId, @AuthenticationPrincipal Account account) throws AccessDeniedException{
        Repo repo = repoService.getRepo(repoId);
        if (repo.getUserId() == account.getUserId()) {
            return ResponseEntity.ok(pullRequestService.getPullRequestsByRepoId(repoId));
        }
        throw new AccessDeniedException("Repository with id " + repoId + " does not belong to user");
    }
}
