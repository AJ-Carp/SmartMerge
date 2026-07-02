package com.smartmerge.service;

import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import com.smartmerge.model.Installation;
import com.smartmerge.model.Repo;
import com.smartmerge.repository.InstallationRepository;
import com.smartmerge.repository.PullRequestRepository;
import com.smartmerge.repository.RepoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class InstallationService {

    private final InstallationRepository installationRepository;
    private final RepoRepository repoRepository;
    private final PullRequestRepository pullRequestRepository;

    public Installation getInstallation(long id) {
        return installationRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("installation not found"));
    }

    @Transactional
    public void deleteInstallation(long installationId) {
        installationRepository.deleteById(installationId);
        repoRepository.deleteAllByInstallationId(installationId);
        pullRequestRepository.deleteAllByInstallationId(installationId);
    }

    @Transactional
    public void saveInstallationAndRepos(Installation installation, List<Repo> repos) {
        installationRepository.save(installation);
        repoRepository.saveAll(repos);
    }
}
