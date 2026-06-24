package com.smartmerge.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;
import com.smartmerge.model.Repo;

// used by InstallationEventHandler and RepoEventHandler
@Component
public class RepoMapper {

    public List<Repo> createRepos(List<Map<String, Object>> repoData, Map<String, Object> installationData, Map<String, Object> accountData) {
        List<Repo> repos = new ArrayList<>();

        for (Map<String, Object> repo : repoData) {
            repos.add(Repo.builder()
                .repoId((int)repo.get("id"))
                .userId((int)accountData.get("id"))
                .installationId((int)installationData.get("id"))
                .repoName((String)repo.get("full_name"))
                .isPrivate((boolean)repo.get("private"))
                .build()
            );
        }
        return repos;
    }
}
