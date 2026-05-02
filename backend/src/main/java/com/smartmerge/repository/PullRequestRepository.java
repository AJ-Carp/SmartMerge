package com.smartmerge.repository;

import com.smartmerge.model.PullRequest;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PullRequestRepository extends JpaRepository<PullRequest, Integer> {

    @Transactional
    void deleteAllByInstallationId(int id);
}
