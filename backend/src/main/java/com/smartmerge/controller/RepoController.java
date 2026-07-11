package com.smartmerge.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.smartmerge.model.Account;
import com.smartmerge.model.Repo;
import com.smartmerge.service.RepoService;
import lombok.RequiredArgsConstructor;
import static com.smartmerge.SmartMergeConstants.REPOSITORY_ROUTE;
import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping(REPOSITORY_ROUTE)
@RequiredArgsConstructor
public class RepoController {
    
    private final RepoService repoService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Repo>> getReposByUserId(@PathVariable long userId, @AuthenticationPrincipal Account account) throws AccessDeniedException {
        if (userId == account.getUserId()) {
            return ResponseEntity.ok(repoService.getReposByUserId(userId));
        }
        throw new AccessDeniedException("user id " + userId + " does not match the authenticated user");
    }
}
