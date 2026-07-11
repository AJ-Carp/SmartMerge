package com.smartmerge.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.smartmerge.exception.RepoNotFoundException;
import com.smartmerge.model.Repo;
import com.smartmerge.repository.PullRequestRepository;
import com.smartmerge.repository.RepoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class RepoService {
    
    private final RepoRepository repoRepository;
    private final PullRequestRepository pullRequestRepository;

    public Repo getRepo(long repoId) {
        return repoRepository.findById(repoId)
                .orElseThrow(() -> new RepoNotFoundException("No repository found for id: " + repoId));
    }
    
    public List<Repo> saveAllRepos(List<Repo> repos) {
        return repoRepository.saveAll(repos);
    }

    public List<Repo> getReposByUserId(long userId) {
        return repoRepository.findAllByUserId(userId);
    }

    @Transactional
    public void deleteAllRepos(List<Long> repoIds) {
        // delete all PRs with any of these repoIds
        pullRequestRepository.deleteAllByRepoIdIn(repoIds);
        repoRepository.deleteAllById(repoIds);
    }
}
