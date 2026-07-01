package com.smartmerge.repository;

import com.smartmerge.model.PullRequest;
import jakarta.transaction.Transactional;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PullRequestRepository extends JpaRepository<PullRequest, Long> {

    @Transactional
    void deleteAllByInstallationId(int id);

    void deleteAllByRepoIdIn(List<Integer> repoIds);
}
