package com.smartmerge.service;

import java.util.List;
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

    public Installation saveInstallation(Installation installation) {
        return installationRepository.save(installation);
    }

    public void deleteInstallationById(int id) {
        installationRepository.deleteById(id);
    }

    @Transactional
    public void deleteInstallation(int installationId) {
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
